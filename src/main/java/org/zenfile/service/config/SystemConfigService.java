package org.zenfile.service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zenfile.mapper.SystemConfigMapper;
import org.zenfile.utils.RedisStorageUtils;

import javax.annotation.Resource;

/**
 * 系统配置业务类
 */
@Service
@Slf4j
public class SystemConfigService {

    @Resource
    SystemConfigMapper systemConfigMapper;

    @Resource
    RedisStorageUtils redisStorageUtils;

    /**
     * 获取系统的域名，尝试从Redis中获得
     * @return 系统域名
     */
    public String getDomain() {
        String title = "domain";
        // 尝试从Redis中获得
        String domain = redisStorageUtils.getSystemConfig(title);
        if(domain == null) {
            domain = getSystemConfigByTitle(title);
            // 存储到Redis中
            redisStorageUtils.saveSystemConfig(title, domain);
            log.info("从数据空查询系统域名为{}, 并存储到Redis。", domain);
        }
        return domain;
    }

    /**
     * 封装系统的请求
     * @param title 系统设置名称
     */
    private String getSystemConfigByTitle(String title){
       return systemConfigMapper.getSystemConfigByTitle(title);
    }
}
