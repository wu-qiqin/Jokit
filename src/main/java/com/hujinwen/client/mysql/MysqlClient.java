package com.hujinwen.client.mysql;

import com.hujinwen.utils.ReflectUtils;
import com.hujinwen.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hu-jinwen on 2020/7/22
 * <p>
 * TODO 这里待优化，使用连接池
 */
public class MysqlClient implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(MysqlClient.class);

    private final String host;

    private final String port;

    private final String user;

    private final String password;

    private String db;

    private static final MessageFormat URL_FORMAT = new MessageFormat("jdbc:mysql://{0}:{1}/{2}");

    private static final Map<String, Connection> CONNECTIONS = new HashMap<>();

    public MysqlClient(String host, String user, String password) {
        this(host, "3306", user, password);
    }

    public MysqlClient(String host, String port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void useDB(String db) throws SQLException {
        this.db = db;

        if (!CONNECTIONS.containsKey(db)) {
            final Connection connection = DriverManager.getConnection(URL_FORMAT.format(new Object[]{host, port, db}), user, password);
            CONNECTIONS.put(db, connection);
        }
        logger.info("Database changed! current -> {}", db);
    }

    public <T> List<T> select(String sql, Class<T> clazz) throws SQLException {
        List<T> results = new ArrayList<>();
        try (
                PreparedStatement statement = CONNECTIONS.get(db).prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                final T instance = clazz.newInstance();

                for (Field field : ReflectUtils.getDeclaredFields(clazz)) {
                    final String colName = StringUtils.humpToUnderline(field.getName());
                    final Object obj = resultSet.getObject(colName);
                    final Method setMethod = ReflectUtils.findSetMethod(clazz, field);
                    if (setMethod != null) {
                        setMethod.invoke(instance, obj);
                    }
                }
                results.add(instance);
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return results;
    }

    @Override
    public void close() throws IOException {
        for (Connection connection : CONNECTIONS.values()) {
            try {
                connection.close();
            } catch (SQLException se) {
                logger.warn("Connection close failed!", se);
            }
        }
    }
}
