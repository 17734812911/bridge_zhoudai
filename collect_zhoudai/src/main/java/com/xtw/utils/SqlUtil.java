package com.xtw.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xtw.collect.model.ChannelAttribute;
import com.xtw.collect.model.DeviceData;
import com.xtw.outdischarge.model.OutPartial;
import com.xtw.outdischarge.model.OutPartialAlarmConfig;
import com.xtw.publicmodel.Alert;
import com.xtw.publicmodel.Device;
import com.xtw.publicmodel.DeviceConfig;
import com.xtw.publicmodel.DeviceMaxValue;
import com.xtw.publicmodel.FibreTemperature;

public class SqlUtil {
	
	public static ConnectionPool connPool=ConnectionPoolUtils.GetPoolInstance();	// 创建数据库连接池
	public static List<Device> collectDeviceConfigList = new ArrayList<>();
	public static List<Device> outpartialDeviceConfigList = new ArrayList<>();
	public static ArrayList<ChannelAttribute> channelAttributeList = new ArrayList<>();
	public static ArrayList<OutPartialAlarmConfig> outPartialAlarmConfigList = new ArrayList<>();

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	// 从数据库加载所有采集器从机的相关配置
	public static List<Device> getCollectDeviceConfigList() throws SQLException{
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String deviceSql = "select d.id,dt.name,d.line_id,d.joint,dc.id,dc.terminal_id,dc.device_type_id,dc.device_ip,dc.port,dc.partition_id,dc.interval_slave,dc.retry_count,dc.retry_interval,dc.polling_interval\n" +
					"from device d,device_config dc,device_type dt,(\n" +
					"select id\n" +
					"from device d\n" +
					"where d.device_type_id=1 or d.device_type_id=5\n" +
					") a\n" +
					"where d.id = a.id and d.terminal_id = dc.terminal_id and d.device_type_id=dt.device_type_id\n" +
					"group by d.id";
			ps = conn.prepareStatement(deviceSql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Device device = new Device();
				DeviceConfig deviceConfig = new DeviceConfig();
				device.setId(rs.getInt(1));
				device.setName(rs.getString(2));
				device.setLineId(rs.getInt(3));
				device.setJoint(rs.getString(4));
				
				deviceConfig.setId(rs.getInt(5));
				deviceConfig.setTerminalId(rs.getString(6));
				deviceConfig.setDeviceTypeId(rs.getInt(7));
				deviceConfig.setDeviceIp(rs.getString(8));
				deviceConfig.setPort(rs.getString(9));
				deviceConfig.setPartitionId(rs.getInt(10));
				deviceConfig.setIntervalSlave(rs.getInt(11));
				deviceConfig.setRetryCount(rs.getInt(12));
				deviceConfig.setRetryInterval(rs.getInt(13));
				deviceConfig.setPollingInterval(rs.getInt(14));
				
				device.setDeviceConfig(deviceConfig);
				
				collectDeviceConfigList.add(device);
			}
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}	
		return collectDeviceConfigList;
	}
	
	// 根据从机号查询该从机下所有通道的类型
	public static ArrayList<ChannelAttribute> getChannelAttributeList(String terminalID) throws SQLException{
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String channelSql = "select id,name,terminal_id,channel_id,channel_type,range_up,range_low,warning_value,alarm_up,critical_alarm,offset_value,is_use from channel_attribute where terminal_id = ? and is_use = 1";
			ps = conn.prepareStatement(channelSql);
			ps.setString(1, terminalID);	// 设置where条件
			rs = ps.executeQuery();
//			channelAttributeList.clear();
			while(rs.next()) {
				ChannelAttribute channelAttribute = new ChannelAttribute();
				channelAttribute.setId(rs.getInt(1));
				channelAttribute.setName(rs.getString(2));
				channelAttribute.setTerminalId(rs.getString(3));
				channelAttribute.setChannelId(rs.getString(4));
				channelAttribute.setChannelType(rs.getString(5));
				channelAttribute.setRangeUp(rs.getInt(6));
				channelAttribute.setRangeLow(rs.getInt(7));
				channelAttribute.setWarningValue(rs.getString(8));
				channelAttribute.setAlarmUp(rs.getString(9));
				channelAttribute.setCriticalAlarm(rs.getString(10));
				channelAttribute.setOffsetValue(rs.getDouble(11));
				channelAttribute.setUse(rs.getBoolean(12));
				channelAttributeList.add(channelAttribute);
			}
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return channelAttributeList;
	}
	
	// 从数据库加载所有外置局放的相关配置
	public static List<Device> getOutPartialConfigList() throws SQLException{
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String Sql = "select d.id,d.name,d.line_id,d.joint,dc.id,dc.terminal_id,dc.device_type_id,dc.device_ip,dc.port,dc.partition_id,dc.interval_slave,dc.retry_count,dc.retry_interval,dc.polling_interval from device d,device_config dc where dc.device_type_id=2 and d.device_type_id=dc.device_type_id group by dc.device_ip";
			ps = conn.prepareStatement(Sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				Device device = new Device();
				DeviceConfig deviceConfig = new DeviceConfig();
				device.setId(rs.getInt(1));
				device.setName(rs.getString(2));
				device.setLineId(rs.getInt(3));
				device.setJoint(rs.getString(4));
				
				deviceConfig.setId(rs.getInt(5));
				deviceConfig.setTerminalId(rs.getString(6));
				deviceConfig.setDeviceTypeId(rs.getInt(7));
				deviceConfig.setDeviceIp(rs.getString(8));
				deviceConfig.setPort(rs.getString(9));
				deviceConfig.setPartitionId(rs.getInt(10));
				deviceConfig.setIntervalSlave(rs.getInt(11));
				deviceConfig.setRetryCount(rs.getInt(12));
				deviceConfig.setRetryInterval(rs.getInt(13));
				deviceConfig.setPollingInterval(rs.getInt(14));
				
				device.setDeviceConfig(deviceConfig);
				
				outpartialDeviceConfigList.add(device);
			}
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}	
		return outpartialDeviceConfigList;
	}
	
	// 保存通道数据到数据库
	public static int insertChannelData(DeviceData deviceData) throws SQLException {

		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String insertSql = "insert into device_data(terminal_id,terminal_ip,passage_one,passage_two,passage_three,passage_four,passage_five,passage_six,passage_seven,passage_eigth,passage_nine,passage_ten,insert_time) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(insertSql);
			ps.setString(1, deviceData.getTerminalId());
			ps.setString(2, deviceData.getTerminalIp());
			ps.setString(3, deviceData.getPassageOne());
			ps.setString(4, deviceData.getPassageTwo());
			ps.setString(5, deviceData.getPassageThree());
			ps.setString(6, deviceData.getPassageFour());
			ps.setString(7, deviceData.getPassageFive());
			ps.setString(8, deviceData.getPassageSix());
			ps.setString(9, deviceData.getPassageSeven());
			ps.setString(10, deviceData.getPassageEight());
			ps.setString(11, deviceData.getPassageNine());
			ps.setString(12, deviceData.getPassageTen());
			ps.setString(13, sdf.format(new Date()));
			
			int result = ps.executeUpdate();
			
			channelAttributeList.clear();	// 清空通道集合,否则一直在往里面加数据
			
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
            
            return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return 0;
	}

	// 向报警表插入数据
	public static void insertAlertData(Alert alert) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String sql = "insert into alert(line_id,terminal_id,content,alert_data,alert_date,isconfirm,alert_type) values(?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, alert.getLineId());
			ps.setString(2, alert.getTerminalId());
			ps.setString(3, alert.getContent());
			ps.setString(4, String.valueOf(alert.getAlertData()));
			ps.setString(5, sdf.format(new Date()));
			ps.setInt(6, 0);
			ps.setString(7, alert.getAlertType());
			int result = ps.executeUpdate();
			if(result > 0) {
				System.out.println("保存报警信息成功");
				
			}else {
				System.out.println("保存报警信息失败");
			}
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
	}
	
	// 向外置局放表插入数据
	public static int insertOutPartialData(OutPartial outPartial) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String sql = "insert into outpartial(terminal_id,a_electric,a_frequency,a_max_electric,a_max_frequency,b_electric,b_frequency,b_max_electric,b_max_frequency,c_electric,c_frequency,c_max_electric,c_max_frequency,createtime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1,outPartial.getTerminalId());
			ps.setString(2, outPartial.getaElectric());
			ps.setString(3, outPartial.getaFrequency());
			ps.setInt(4, outPartial.getaMaxElectric());
			ps.setInt(5, outPartial.getaMaxFrequency());
			ps.setString(6, outPartial.getbElectric());
			ps.setString(7, outPartial.getbFrequency());
			ps.setInt(8, outPartial.getbMaxElectric());
			ps.setInt(9, outPartial.getbMaxFrequency());
			ps.setString(10, outPartial.getcElectric());
			ps.setString(11, outPartial.getcFrequency());
			ps.setInt(12, outPartial.getcMaxElectric());
			ps.setInt(13, outPartial.getcMaxFrequency());
			ps.setString(14, sdf.format(new Date()));
			
			result = ps.executeUpdate();
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return result;
	}
	
	
	// 外置局放根据TerminalID查询报警配置
	public static OutPartialAlarmConfig getOutPartialAlarmConfig(String terminalId) throws SQLException{
		ResultSet rs = null;
		PreparedStatement ps = null;
		OutPartialAlarmConfig outPartialAlarmConfig = new OutPartialAlarmConfig();
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String channelSql = "select id,partition_id,terminal_id,alarm_value,warning_value,critical_alarm,alarm_frequency_value,offset_value from outpartial_config where terminal_id=?";
			ps = conn.prepareStatement(channelSql);
			ps.setString(1, terminalId);	// 设置where条件
			rs = ps.executeQuery();
			
			while(rs.next()) {
				outPartialAlarmConfig.setId(rs.getInt(1));
				outPartialAlarmConfig.setPartitionId(rs.getInt(2));
				outPartialAlarmConfig.setTerminalId(rs.getString(3));
				outPartialAlarmConfig.setAlarmValue(rs.getDouble(4));
				outPartialAlarmConfig.setWarningValue(rs.getDouble(5));
				outPartialAlarmConfig.setCriticalAlarm(rs.getDouble(6));
				outPartialAlarmConfig.setAlarmFrequencyValue(rs.getInt(7));
				outPartialAlarmConfig.setOffsetValue(rs.getDouble(8));
			}
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return outPartialAlarmConfig;
	}
	
	
	// 更新设备表中每种设备最后保存数据的时间
	public static int lastDataTime(String terminalId) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String sql = "update device set last_data_time = ? where terminal_id = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, sdf.format(new Date()));
			ps.setString(2, terminalId);
		
			result = ps.executeUpdate();
			
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return result;
		
	}
	
	// 查询环境量最近七天最大值
	public static DeviceMaxValue maxValue() throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		DeviceMaxValue deviceMaxValue = new DeviceMaxValue();
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String channelSql = "select max(passage_one) sd,max(passage_two) wd,max(passage_six) yq,max(passage_seven) eyht,max(passage_eigth) lhq,max(passage_nine) jw\r\n" + 
					"from device_data\r\n" + 
					"where terminal_id in ('22202003','22202004','22202005') and date_sub(curdate(), interval 7 day) <= date(insert_time)";
			ps = conn.prepareStatement(channelSql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				deviceMaxValue.setSd(rs.getString(1));
				deviceMaxValue.setWd(rs.getString(2));
				deviceMaxValue.setYq(rs.getString(3));
				deviceMaxValue.setEyht(rs.getString(4));
				deviceMaxValue.setLhq(rs.getString(5));
				deviceMaxValue.setJw(rs.getString(6));
			}
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return deviceMaxValue;
		
	}
	
	// 查询表皮温度最近七天最大值
	public static DeviceData maxValueBpwd() throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		DeviceData deviceData = new DeviceData();
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String channelSql = "select max(passage_one),max(passage_two),max(passage_three),max(passage_six),max(passage_seven),max(passage_eigth)\n" +
					"from device_data\n" +
					"where terminal_id in (\n" +
					"select terminal_id from device d,device_type dt where d.device_type_id = dt.device_type_id and dt.name = '表皮测温'\n" +
					") and date_sub(curdate(), interval 7 day) <= date(insert_time)";
			ps = conn.prepareStatement(channelSql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				deviceData.setPassageOne(rs.getString(1));
				deviceData.setPassageTwo(rs.getString(2));
				deviceData.setPassageThree(rs.getString(3));
				deviceData.setPassageSix(rs.getString(4));
				deviceData.setPassageSeven(rs.getString(5));
				deviceData.setPassageEight(rs.getString(6));
			}
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return deviceData;
	}
	
	// 查询外置局放最近七天最大值
	public static OutPartial maxValueWzjf() throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		OutPartial outPartial = new OutPartial();
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String channelSql = "select max(a_max_electric),max(b_max_electric),max(c_max_electric)\r\n" + 
					"from outpartial\r\n" + 
					"where date_sub(curdate(), interval 7 day) <= date(createtime)";
			ps = conn.prepareStatement(channelSql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				outPartial.setaMaxElectric(rs.getInt(1));
				outPartial.setbMaxElectric(rs.getInt(2));
				outPartial.setcMaxElectric(rs.getInt(3));
			}
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return outPartial;
	}
		
	// 查询光纤测温最近七天最大值
	public static FibreTemperature maxValueGxcw() throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		FibreTemperature fibreTemperature = new FibreTemperature();
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String channelSql = "select max(max_value)\r\n" + 
					"from fibre_temperature\r\n" + 
					"where date_sub(curdate(), interval 7 day) <= date(create_time)";
			ps = conn.prepareStatement(channelSql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				fibreTemperature.setMaxValue(rs.getDouble(1));
			}
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return fibreTemperature;
	}

	// 查询128.0.0.4的最新一条数据
	public static DeviceData queryDataEnvironment() throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		DeviceData deviceData = new DeviceData();
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String channelSql = "select dd.passage_one,dd.passage_two,dd.passage_six,dd.passage_seven,dd.passage_eigth,dd.passage_nine\n" +
					"from device_data dd,(select max(id) id from device_data where terminal_ip = '128.0.0.3') a\n" +
					"where dd.id = a.id";
			ps = conn.prepareStatement(channelSql);
			rs = ps.executeQuery();

			while(rs.next()) {
				deviceData.setPassageOne(rs.getString(1));
				deviceData.setPassageTwo(rs.getString(2));
				deviceData.setPassageSix(rs.getString(3));
				deviceData.setPassageSeven(rs.getString(4));
				deviceData.setPassageEight(rs.getString(5));
				deviceData.setPassageNine(rs.getString(6));
			}
			// 连接使用完后释放连接到连接池
			connPool.returnConnection(conn);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return deviceData;
	}



	// 向最大值表(maxvalue)中更新数据
	public static int updateMaxValue(DeviceMaxValue deviceMaxValue) throws SQLException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			Connection conn = connPool.getConnection();		// 从连接池中获取一个可用的连接
			String sql = "update maxvalues set sd=?,wd=?,yq=?,eyht=?,lhq=?,jw=?,bpwd=?,wzjf=?,gxcw=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1,deviceMaxValue.getSd());
			ps.setString(2, deviceMaxValue.getWd());
			ps.setString(3, deviceMaxValue.getYq());
			ps.setString(4, deviceMaxValue.getEyht());
			ps.setString(5, deviceMaxValue.getLhq());
			ps.setString(6, deviceMaxValue.getJw());
			ps.setString(7, deviceMaxValue.getBpwd());
			ps.setString(8, deviceMaxValue.getWzjf());
			ps.setString(9, deviceMaxValue.getGxcw());
			
			result = ps.executeUpdate();
			// 连接使用完后释放连接到连接池
            connPool.returnConnection(conn);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			//关闭相关连接
			if(null != rs) {
				rs.close();
			}
			if(null != ps) {
				ps.close();
			}
		}
		return result;
	}
	
}
