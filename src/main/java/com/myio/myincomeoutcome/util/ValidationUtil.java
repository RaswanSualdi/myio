package com.myio.myincomeoutcome.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Component
public class ValidationUtil {
    private final Validator validator;

    @Autowired
    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object obj) {
        Set<ConstraintViolation<Object>> result = validator.validate(obj);
        if (!result.isEmpty()) {
            throw new ConstraintViolationException(result);
        }
    }

}
