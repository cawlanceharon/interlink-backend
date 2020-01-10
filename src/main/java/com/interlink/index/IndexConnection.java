package com.interlink.index;

import java.sql.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mariadb.jdbc.*;

public class IndexConnection {

	String DATABASE = "jdbc:mysql://127.0.0.1/interlink";
	String USERNAME = "root";
	String PASSWORD = "password1";

	public Connection initialize() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");

        Connection connection = DriverManager.getConnection(
        		DATABASE, USERNAME, PASSWORD);

        return connection;
	}
}