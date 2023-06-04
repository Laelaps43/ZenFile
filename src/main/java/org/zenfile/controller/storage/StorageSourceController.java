package org.zenfile.controller.storage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zenfile.convert.StorageSourceConvert;
import org.zenfile.model.ResultJson;
import org.zenfile.model.storage.dto.StorageSourceDto;
import org.zenfile.model.storage.entity.StorageSource;
import org.zenfile.model.storage.enums.StorageTypeEnum;
import org.zenfile.model.storage.param.StorageParam;
import org.zenfile.model.storage.result.StorageSourceResult;
import org.zenfile.service.storage.StorageSourceService;
import org.zenfile.service.storage.base.AbstractBaseFileService;
import org.zenfile.service.storage.context.StorageSourceContext;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "存储源模块")
@RestController
@RequestMapping("/storage")
@Slf4j
public class StorageSourceController {

    @Resource
    private StorageSourceService storageSourceService;

    @Resource
    private StorageSourceConvert storageSourceConvert;

    @Resource
    private StorageSourceContext storageSourceContext;

    @GetMapping("list")
    @ApiOperation(value = "获取所有的存储源", notes = "获取所有的存储源，已一个List的方式返回。")
    public ResultJson<List<StorageSourceResult>> storageList(){
        List<StorageSource> storageSources = storageSourceService.getAllStorageSource();

        List<StorageSourceResult> storageSourceResults = storageSourceConvert.storageSourceToResult(storageSources);

        storageSourceResults.parallelStream().forEach(storageSourceResult -> {
            String key = storageSourceResult.getKey();
            AbstractBaseFileService<StorageParam> fileService = storageSourceContext.getStorageSourceServiceByStorageKey(key);
            List<Long> sourceStorageInfo = fileService.getSourceStorageInfo();
            storageSourceResult.setUsed(sourceStorageInfo.get(1));
            storageSourceResult.setTotal(sourceStorageInfo.get(0));
        });

        return ResultJson.getSuccessData(storageSourceResults);
    }

    @ApiOperation(value = "获取指定存储源参数", notes = "根据给定的Key来获取指定的存储源参数")
    @GetMapping("/{storageKey}")
    public ResultJson<StorageSourceDto> getStorageItemByKey(@PathVariable String storageKey){
        StorageSourceDto storageItemByKey = storageSourceService.getStorageItemByKey(storageKey);
        AbstractBaseFileService<StorageParam> fileService = storageSourceContext.getStorageSourceServiceByStorageKey(storageKey);
        List<Long> sourceStorageInfo = fileService.getSourceStorageInfo();
        storageItemByKey.setUsed(sourceStorageInfo.get(1));
        storageItemByKey.setTotal(sourceStorageInfo.get(0));
        return ResultJson.getSuccessData(storageItemByKey);
    }

//    public ResultJson<String> saveStorageSource(@RequestBody )

    @ApiOperation(value = "获取存储支持的类型", notes = "返回存储源类型，数组的方式返回。")
    @GetMapping("/types")
    public ResultJson<StorageTypeEnum[]> supportStorageType(){
        return ResultJson.getSuccessData(StorageTypeEnum.values());
    }

    @ApiOperation(value = "判断Key是否存在", notes = "传入一个Key，都会返回一个Boolean类型")
    @GetMapping("/exist/key")
    public ResultJson<Boolean> existKey(String storageKey){
        Boolean exist = storageSourceService.existKey(storageKey);
        return ResultJson.getSuccessData(exist);
    }

}
