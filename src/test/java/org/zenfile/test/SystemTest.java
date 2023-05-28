package org.zenfile.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
@Slf4j
public class SystemTest {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Test
    public void RedisTest(){
        String ping = (String) redisTemplate.opsForValue().get("ping");
        log.debug("[lae] ping - {}", ping);
    }

}
