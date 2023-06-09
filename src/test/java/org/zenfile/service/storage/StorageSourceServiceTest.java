package org.zenfile.service.storage;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.zenfile.Utils.TestConstant;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class StorageSourceServiceTest {


    @Resource
    StorageSourceService storageSourceService;

    @Test
    public void getStorageKeyByIdTest(){
        String storageKeyById = storageSourceService.getStorageKeyById(1L);
        log.debug("{}getStorageKeyByIdTest {}", TestConstant.TEST_DEBUG, storageKeyById);
    }


    @Test
    public void getStorageIdByKeyTest(){
        Long key123 = storageSourceService.getStorageIdByKey("Test123");
        log.debug("{} key12345获取到的Id是{}", TestConstant.TEST_DEBUG, key123);
    }

}
