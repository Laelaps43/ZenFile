package org.zenfile.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.zenfile.Utils.TestConstant;
import org.zenfile.mapper.StorageSourceConfigMapper;
import org.zenfile.model.storage.entity.StorageSourceConfig;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class StorageSourceConfigServiceTest {

    @Resource
    StorageSourceConfigMapper storageSourceConfigMapper;

    @Resource
    StorageSourceConfigService storageSourceConfigService;

    @Test
    public void getStorageSourceConfigByStorageIdTest(){
        List<StorageSourceConfig> storageSourceConfigByStorageId = storageSourceConfigService.getStorageSourceConfigByStorageId(1L);
        log.debug("{}-{}", TestConstant.TEST_DEBUG, storageSourceConfigByStorageId);
    }
}
