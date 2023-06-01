package org.zenfile.service.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.zenfile.Utils.TestConstant;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class SystemConfigServiceTest {

    @Resource
    SystemConfigService systemConfigService;

    @Test
    public void getDomainTest(){
        String domain = systemConfigService.getDomain();
        log.debug("{}getDomainTest {}", TestConstant.TEST_DEBUG, domain);
    }
}
