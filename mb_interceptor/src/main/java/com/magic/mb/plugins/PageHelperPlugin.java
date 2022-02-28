package com.magic.mb.plugins;

import com.magic.mb.bean.Page;
import com.magic.mb.config.PageHelp;
import com.magic.mb.utils.ThreadLocalUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author laoma
 * @create 2022-02-27 21:09
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class PageHelperPlugin implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(PageHelperPlugin.class);


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("-------pageHelperInterceptor-------");
        // 获得 sql 语句，拼接 limit
        MetaObject metaObject = SystemMetaObject.forObject(invocation);
        String sql = (String) metaObject.getValue("target.delegate.boundSql.sql");
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.lastIndexOf(";"));
        }
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("target.delegate.mappedStatement");
        String namespace = mappedStatement.getId();
        //com.magic.mb.mapper.UserMapper.selectList
        String clazzName = namespace.substring(0, namespace.lastIndexOf("."));
        String methodName = namespace.substring(namespace.lastIndexOf(".") + 1, namespace.length());
        // Method[] methods = Class.forName(clazzName).getMethods();
        Method method = Class.forName(clazzName).getDeclaredMethod(methodName);
        // for (Method method : methods) {
        // if (methodName.equals(method.getName())) {
        PageHelp annotation = method.getAnnotation(PageHelp.class);
        if (annotation != null) {
            // 分页
            Page page = ThreadLocalUtils.getAndRemove();
            String countSql = "select count(1) " + sql.substring(sql.indexOf("from"));
            // JDBC 操作
            // 1 Connection PrepareStatment
            Connection conn = (Connection) invocation.getArgs()[0];
            PreparedStatement preparedStatement = conn.prepareStatement(countSql);

            // ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("target.delegate.parameterHandler");
            // parameterHandler.setParameters(preparedStatement);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                page.setTotalSize(resultSet.getInt(1));
            }
            String newSql = sql + " limit " + page.getFirstItem() + "," + page.getPageCount();
            metaObject.setValue("target.delegate.boundSql.sql", newSql);
        }
        // }
        // }
        return invocation.proceed();
    }

}
