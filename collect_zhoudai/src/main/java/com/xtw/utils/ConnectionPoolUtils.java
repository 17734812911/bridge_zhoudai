package com.xtw.utils;

public class ConnectionPoolUtils {

	private ConnectionPoolUtils() {
	}; // 私有静态方法

	private static ConnectionPool poolInstance = null;


	public static ConnectionPool GetPoolInstance() {
		//Map<String,Object> map = info();
		if (poolInstance == null) {
			poolInstance = new ConnectionPool("com.mysql.cj.jdbc.Driver",
					"jdbc:mysql://128.0.0.19:3306/zhoudai_bridge?serverTimezone=UTC","root","123456" );//+(String) ((Map<String, Object>)map.get("user")).get("dataBaseName"), (String)((Map<String, Object>) map.get("user")).get("name"), String.valueOf(((Map<String, Object>)map.get("user")).get("password"))
			try {// 128.0.0.19
				poolInstance.createPool();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return poolInstance;
	}
}
