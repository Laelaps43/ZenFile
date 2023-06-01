package org.zenfile.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.zenfile.Utils.TestConstant;

@SpringBootTest
@Slf4j
public class SystemTest {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void RedisTest(){
        stringRedisTemplate.opsForValue().set("zenFile", "zenfile.cloud");
        String zenFile = stringRedisTemplate.opsForValue().get("zenFile");
        log.debug("{}{}", TestConstant.TEST_DEBUG, zenFile);
    }

}
