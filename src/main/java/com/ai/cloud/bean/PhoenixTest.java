package com.ai.cloud.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by pc on 2017-05-20.
 */
public class PhoenixTest {

    public static void main(String[] args) throws Exception{
        PhoenixTest test = new PhoenixTest();
        Connection conn = test.getConnection();
        Statement stmt = conn.createStatement();
        String sql = "select * from user_trace_02 where pk like '003%'";
        ResultSet set = stmt.executeQuery(sql);
        while(set.next()){
            System.out.println(set.getString("SERIAL_NUMBER"));
        }
        conn.close();
    }

    public Connection getConnection(){
        Connection connection = null;
        String driver = "org.apache.phoenix.jdbc.PhoenixDriver";
        String url = "jdbc:phoenix:bogon:2181";

        try{
            Class.forName(driver);

        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            connection = DriverManager.getConnection(url);
        } catch(Exception e){
            e.printStackTrace();
        }

        return connection;
    }
}
