package org.zenfile.service.file;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.zenfile.Utils.TestConstant;
import org.zenfile.model.file.entity.FileItem;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class FileServiceTest {

    @Resource
    FileService fileService;

    /**
     *  getFileItemStorage测试方法
     */
    @Test
    public void getFileItemStorageTest(){
        FileItem fileItemStorage = fileService.getFileItemStorage("/a", 1L);
        log.debug("{} {}", TestConstant.TEST_DEBUG, fileItemStorage);
    }
}
