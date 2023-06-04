package org.zenfile.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.zenfile.model.file.entity.FileItem;
import org.zenfile.model.storage.entity.StorageSource;
import org.zenfile.model.storage.entity.StorageSourceConfig;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RedisStorageUtils {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    // 系统存储前缀
    private static final String SYSTEM_CONFIG = "zenfile:system";

    // 存储源前缀
    private static final String STORAGE_SOURCE = "zenfile:storage";

    // 存储源Key前缀
    private static final String STORAGE_SOURCE_KEY = "zenfile:storage:key";

    // 存储源配置前缀
    private static final String STORAGE_SOURCE_CONFIG = "zenfile:storage:config";

    // 文件前缀
    private static final String FILE = "zenfile:file";

    /**
     * 获取系统的配置，保存类型为key为 zenfile:system:{title}
     * @param title 传送过来的配置标题
     */
    public String getSystemConfig(String title) {
        return stringRedisTemplate.opsForValue().get(StrUtil.format("{}:{}", SYSTEM_CONFIG, title));
    }

    /**
     * 存储系统的配置，保存的类型为 zenfile:system:{title}
     * @param title
     * @param domain
     */
    public void saveSystemConfig(String title, String domain) {
        stringRedisTemplate.opsForValue().set(StrUtil.format("{}:{}", SYSTEM_CONFIG, title), domain);
    }

    /**
     * 获取存储的存储源的对象
     * @param storageId 存储源Id
     */
    public StorageSource getStorageSourceById(Long storageId) {
        return (StorageSource) redisTemplate.opsForValue().get(StrUtil.format("{}:id:{}", STORAGE_SOURCE,storageId));
    }

    /**
     * 保存到Redis的存储源对象, key为 zenfile:storage:{id}
     * @param storageSource 存储源对象
     */
    public void setStorageSource(StorageSource storageSource) {
        redisTemplate.opsForValue().set(StrUtil.format("{}:{}",STORAGE_SOURCE, storageSource.getId()), storageSource);
    }

    /**
     * 将一序列StorageSource存储到Redis中
     * @param storageSources
     */
    public void setStorageSourceList(List<StorageSource> storageSources){
        Map<String, List<StorageSource>> collect = storageSources.stream().collect(Collectors.toMap(storageSource -> {
            return StrUtil.format("{}:{}", STORAGE_SOURCE, storageSource.getId());
        }, storageSource -> storageSources));
        redisTemplate.opsForValue().multiSet(collect);
    }

    /**
     * 根据给定的存储元以及文件路径，从zenfile:file:id (Map)中获取对于的对象
     * @param pathAndName 存储过来的路径为Key
     * @param storageId 存储过来的Id
     */
    public FileItem getFileItemByPathAndStorage(String pathAndName, Long storageId) {
        return (FileItem) redisTemplate.opsForHash().get(StrUtil.format("{}:{}", FILE, storageId), pathAndName);
    }

    /**
     * 根据给定的存储源和FileItem中的路径 + 名字，将对象放置在 zenfile:file:id (Map)中
     * pathAndName : fileItem (JSON)
     */

    public void setFileItemAndStorage(Long storageId, FileItem fileItem){
        String pathAndName = StringUtils.concat(fileItem.getPath(), fileItem.getName());
        redisTemplate.opsForHash().put(StrUtil.format("{}:{}",FILE,storageId), pathAndName, fileItem);
    }

    /**
     * 获取指定存储源的配置列表 key : zenfile:storage:config:{id} (List)
     * @param storageSourceId 存储源Id
     */
    public List<StorageSourceConfig> getStorageSourceConfigListByStorageId(Long storageSourceId) {
        List<Object> range = redisTemplate.opsForList().range(StrUtil.format("{}:{}", STORAGE_SOURCE_CONFIG, storageSourceId), 0, -1);
        if(range != null){
            return range.parallelStream().map(StorageSourceConfig.class::cast).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 将存储源id:storageSourceId 所有的配置存储到Redis中
     */
    public void setStorageSourceConfigListByStorageId(Long storageSourceId, List<StorageSourceConfig> configList) {
        // 这里为传入一个List集合去并不能将每一个对象添加到Redis中，而是与一个数组的方式添加进去
        redisTemplate.opsForList().rightPushAll(StrUtil.format("{}:{}", STORAGE_SOURCE_CONFIG, storageSourceId), configList.toArray());
    }

    /**
     * 通过key来获取存储源，因为在Redis中是id:source存储，所有我们必须先获取key对应的id
     * 任何在获取id对应的source，因此这里使用lua脚本获取
     * @param key 需要查询的Key
     * @return 查询的结果
     */
    public StorageSource getStorageSourceByKey(String key) {
        DefaultRedisScript<StorageSource> script = new DefaultRedisScript<>();
        script.setResultType(StorageSource.class);
        script.setScriptSource(new ResourceScriptSource(
                new ClassPathResource("./lua/getStorageSourceByKey.lua")
        ));
        String key1 = StrUtil.format("{}:{}", STORAGE_SOURCE_KEY, key);
        String key2 = StrUtil.format("{}:",STORAGE_SOURCE);
        log.debug("通过Key获取存储源lua脚本执行中... {} {}", key1, key2);
        return redisTemplate.execute(script, List.of(key1, key2));
    }

    /**
     * 通过Key存储storageSource，有问题需要注意，redis中是key 对应 id 不存在
     * 还是 id 对应 storageSource 不存在
     * 因为这里是采用的是字符串存储，所有会覆盖旧值，所有直接执行添加
     * @param storageSource 存储源
     */
    public void setStorageSourceByKey(StorageSource storageSource) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(StrUtil.format("{}:{}", STORAGE_SOURCE, storageSource.getId()), storageSource);
        map.put(StrUtil.format("{}:{}", STORAGE_SOURCE_KEY, storageSource.getKey()), storageSource.getId());
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 根据获取Key 获取指定的Id
     * @param storageKey 存储源Key
     * @return 存储源Id
     */
    public Long getStorageSourceIdByKey(String storageKey) {
        return (Long) redisTemplate.opsForValue().get(storageKey);
    }
}
