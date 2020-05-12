package com.example.catalog.service.ref;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypeLocator;
import org.springframework.expression.spel.support.StandardTypeLocator;

import java.util.Arrays;
import java.util.List;

/**
 * Decorator for StandardTypeLocator only allowing access to certain fixed types.
 * https://github.com/thymeleaf/thymeleaf-itutorial/blob/2.1-master/src/main/java/org/thymeleaf/itutorial/WhitelistTypeLocator.java
 */
public class WhitelistTypeLocator implements TypeLocator {

    private static final List<String> whitelist = Arrays.asList("java.lang.Math", "Math", "java.util.Date", "ca.ryerson.common.Constants");

    private final StandardTypeLocator standardTypeLocator = new StandardTypeLocator();

    public WhitelistTypeLocator() {
    }

    public Class<?> findType(String typeName) throws EvaluationException {
        if (notInWhitelist(typeName)) {
            throw new EvaluationException("Forbidden type: " + typeName);
        }
        return standardTypeLocator.findType(typeName);
    }

    private boolean notInWhitelist(String typeName) {
        return !whitelist.contains(typeName);
    }
}
