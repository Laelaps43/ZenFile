package org.zenfile.service.storage.context;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.zenfile.model.storage.param.StorageParam;
import org.zenfile.service.storage.base.AbstractBaseFileService;
import org.zenfile.service.storage.impl.BaseLocalServiceImpl;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class StorageSourceContextTest {

    @Resource
    StorageSourceContext sourceContext;

    @Test
    public void getInitParamTest(){
        AbstractBaseFileService baseLocalService = new BaseLocalServiceImpl();
        sourceContext.getInitParam(1L,baseLocalService);

    }
}
