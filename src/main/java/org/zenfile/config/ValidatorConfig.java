package org.zenfile.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


@Configuration
public class ValidatorConfig {

    /**
     * failFast(true)或 addProperty("hibernate.validator.fail_fast", "true")设置为快速失败模式，
     * 快速失败模式在校验过程中，当遇到第一个不满足条件的参数时就立即返回，不再继续后面参数的校验。
     */
    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory();

        return validatorFactory.getValidator();
    }
}
