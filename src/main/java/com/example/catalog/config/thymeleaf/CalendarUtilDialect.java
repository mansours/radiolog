package com.example.catalog.config.thymeleaf;

import com.example.catalog.utilities.CalendarUtil;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import java.util.Collections;
import java.util.Set;

public class CalendarUtilDialect extends AbstractDialect implements IExpressionObjectDialect {

    protected CalendarUtilDialect() {
        super("Calendar Util Dialect");
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return new IExpressionObjectFactory() {

            @Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("cal");
            }

            @Override
            public Object buildObject(IExpressionContext context, String expressionObjectName) {
                return new CalendarUtil();
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
    }
}
