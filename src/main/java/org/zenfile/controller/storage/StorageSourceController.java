package org.zenfile.controller.storage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zenfile.convert.StorageSourceConvert;
import org.zenfile.model.ResultJson;
import org.zenfile.model.storage.dto.StorageSourceDto;
import org.zenfile.model.storage.entity.StorageSource;
import org.zenfile.model.storage.enums.StorageTypeEnum;
import org.zenfile.model.storage.param.StorageParam;
import org.zenfile.model.storage.request.StorageSourceRequest;
import org.zenfile.model.storage.result.StorageSourceResult;
import org.zenfile.service.storage.StorageSourceService;
import org.zenfile.service.storage.base.AbstractBaseFileService;
import org.zenfile.service.storage.context.StorageSourceContext;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
            log.debug("获取到的FileService：{}", fileService);
            List<Long> sourceStorageInfo = fileService.getSourceStorageInfo();
            storageSourceResult.setUsed(sourceStorageInfo.get(1));
            storageSourceResult.setTotal(sourceStorageInfo.get(0));
        });

        return ResultJson.getSuccessData(storageSourceResults);
    }

    @PostMapping("/save")
    @ApiOperation(value = "添加或修改存储源", notes = "添加指定的存储源，或修改存储配置")
    public ResultJson<String> saveStorageSource(@Validated @RequestBody StorageSourceRequest request){
        StorageSource storageSource = storageSourceService.saveStorageSource(request);
        // 初始化存储源
        storageSourceContext.init(storageSource);
        return ResultJson.getSuccessData(storageSource.getKey());
    }

    @ApiOperation(value = "获取存储支持的类型", notes = "返回存储源类型，数组的方式返回。")
    @GetMapping("/types")
    public ResultJson<StorageTypeEnum[]> supportStorageType(){
        return ResultJson.getSuccessData(StorageTypeEnum.values());
    }

    @ApiOperation(value = "判断Key是否存在", notes = "传入一个Key，都会返回一个Boolean类型")
    @GetMapping("/exist/key")
    public ResultJson<Boolean> existKey(@NotNull @Pattern(regexp = "[A-Z][a-z][0-9]") String storageKey){
        Boolean exist = storageSourceService.existKey(storageKey);
        return ResultJson.getSuccessData(exist);
    }

    @ApiOperation(value = "获取指定存储源参数", notes = "根据给定的Key来获取指定的存储源参数")
    @GetMapping("/{storageKey}")
    public ResultJson<StorageSourceDto> getStorageItemByKey(@PathVariable String storageKey){
        StorageSourceDto storageItemByKey = storageSourceService.getStorageItemByKey(storageKey);
        log.debug("根据Key获取到的StorageSourceDto为：{}", storageItemByKey);
        AbstractBaseFileService<StorageParam> fileService = storageSourceContext.getStorageSourceServiceByStorageKey(storageKey);
        List<Long> sourceStorageInfo = fileService.getSourceStorageInfo();
        storageItemByKey.setUsed(sourceStorageInfo.get(1));
        storageItemByKey.setTotal(sourceStorageInfo.get(0));
        return ResultJson.getSuccessData(storageItemByKey);
    }
}
