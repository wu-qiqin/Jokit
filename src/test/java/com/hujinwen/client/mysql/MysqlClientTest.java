package com.hujinwen.client.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

class MysqlClientTest {
    private static final Logger logger = LogManager.getLogger(MysqlClient.class);

    @Test
    void select() throws SQLException, IOException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final MysqlClient mysqlClient = new MysqlClient("10.0.0.38", "fbimysql", "Fbimysql@12426");
        mysqlClient.useDB("fbi");
//        final List<Student> results = mysqlClient.select("select * from fbcm_computer", Student.class);
//        mysqlClient.close();
    }

}