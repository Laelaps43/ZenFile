package org.zenfile.Utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.zenfile.utils.StringUtils;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class StringUtilsTest {

    @Test
    public void separatePathTest() {
        String[] strings = StringUtils.separatePathAndName("/abc/abc.jpg");
        String[] strings1 = StringUtils.separatePathAndName("/abc");
        String[] strings2 = StringUtils.separatePathAndName("/");

        log.debug("{} /abc/abc.jpg: {}, /abc: {}, /: {} ", TestConstant.TEST_DEBUG,
                strings, strings1, strings2);
    }

    @Test
    public void concatTest(){
        String a = StringUtils.concat("/", "a");
        String concat = StringUtils.concat("/a", "abc.jpg");

        log.debug("{} '/' + 'a' : {}, '/a' + 'abc.jpg' : {}",TestConstant.TEST_DEBUG, a, concat);
    }
}