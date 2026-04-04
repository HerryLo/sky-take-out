package com.liuheng.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class SqlErrorInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            return invocation.proceed();
        } catch (Exception e) {
            // 获取SQL信息
            MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            Object parameter = invocation.getArgs()[1];

            log.error("SQL执行异常 - Mapper: {}", ms.getId());
            log.error("参数: {}", parameter);
            log.error("异常详情: ", e);

            // 提取数据库原始错误信息
            Throwable cause = e.getCause();
            while (cause != null) {
                log.error("Caused by: {}", cause.toString());
                cause = cause.getCause();
            }

            throw e; // 重新抛出原异常
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
