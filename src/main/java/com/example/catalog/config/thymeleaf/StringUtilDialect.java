package com.example.catalog.config.thymeleaf;

import com.example.catalog.utilities.CalendarUtil;
import com.example.catalog.utilities.StringUtil;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

public class StringUtilDialect extends AbstractDialect implements IExpressionObjectDialect {

    protected StringUtilDialect() {
        super("String Util Dialect");
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {

            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("str");
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                return new StringUtil();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }
}
