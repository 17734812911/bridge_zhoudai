package com.xtw.collect.operationdata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.xtw.utils.ConnectionPool;
import com.xtw.utils.ConnectionPoolUtils;

public class InsertData {
	
	//单例模式创建连接池对象
	public static ConnectionPool connPool=ConnectionPoolUtils.GetPoolInstance();
	
	@SuppressWarnings("unused")
	public Connection getConnection() {
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = 0;
		try {
			conn = connPool.getConnection();	// 从连接池中获取一个可用的连接
			String sql = "";
			ps = conn.prepareStatement(sql);
			ps.setFloat(1, 12);
			
			rs = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("数据库操作异常");
		} 
		return conn;
	}

}
