package com.xtw;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import com.xtw.collect.model.ChannelAttribute;
import com.xtw.collect.subject.CoreLogic;
import com.xtw.collect.subject.MaxValue;
import com.xtw.outdischarge.subject.OutPartialCoreLogic;
import com.xtw.publicmodel.Device;
import com.xtw.utils.ConnectionPool;
import com.xtw.utils.ConnectionPoolUtils;
import com.xtw.utils.MyUtils;
import com.xtw.utils.SqlUtil;

public class MainClass {
	public static ConnectionPool connPool=ConnectionPoolUtils.GetPoolInstance();	// 创建数据库连接池
	public static LinkedList<List<ChannelAttribute>> list = new LinkedList<>();
	public static List<Device> collectDeviceConfigList = new ArrayList<>();	// 采集器设备配置集合
	public static List<Device> outPartialConfigList = new ArrayList<>();	// 外置局放设备配置集合
	public static HashMap<String,Device> deviceMap = new HashMap<>();
	public static HashMap<String,List<ChannelAttribute>> configChannel = new HashMap<>();
	
	// 从数据库加载相关配置
	static {
		try {
			collectDeviceConfigList = SqlUtil.getCollectDeviceConfigList();		// 加载采集器(表皮测温/气体)
			outPartialConfigList = SqlUtil.getOutPartialConfigList();		// 加载外置局放
			for(Device device :outPartialConfigList) {
				deviceMap.put(device.getDeviceConfig().getDeviceIp(), device);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(int index=0;index < collectDeviceConfigList.size();index++) {
			// 根据从机号查询该从机下所有通道的类型
			String terminalID = collectDeviceConfigList.get(index).getDeviceConfig().getTerminalId();
			ArrayList<ChannelAttribute> channelList = new ArrayList<>();
			try {
				channelList = SqlUtil.getChannelAttributeList(terminalID);		// 查询从机下面在使用的通道数量
				list.add(channelList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		//表皮测温/环境量
		for(int index=0;index<collectDeviceConfigList.size();index++) {
			configChannel.put(collectDeviceConfigList.get(index).getDeviceConfig().getDeviceIp(), list.get(index));
			CoreLogic startThread = new CoreLogic(0, 10, collectDeviceConfigList.get(index), list.get(index));
			Thread thread = new Thread(startThread);
			thread.start();
		}
		
		
		// 外置局放
		ServerSocket serverSocket = null;
		try {
			//建立服务器的Socket,并设定一个监听的端口
			if(serverSocket == null) {
				System.out.println("start listening " + Integer.parseInt(outPartialConfigList.get(0).getDeviceConfig().getPort()) + " port...");
				serverSocket = new ServerSocket(Integer.parseInt(outPartialConfigList.get(0).getDeviceConfig().getPort()));
			}
		} catch (Exception e) {
			System.out.println("The port " + outPartialConfigList.get(0).getDeviceConfig().getPort() +" already is occupied!");
			e.printStackTrace();
		}
		for(int index=0;index<outPartialConfigList.size();index++) {
			try {
				String heartbeat = null;
				Socket socket = getSocket(serverSocket);
				InputStream inputStream = socket.getInputStream();
				Thread.sleep(1000);
				// 获取心跳包
				while(inputStream.available() != 0) {
					byte[] beat = new byte[10240];
		        	inputStream.read(beat);
		        	String resultStr = MyUtils.printHexString(beat);
		        	if("00".equals(resultStr.substring(16, 18))) {
		        		heartbeat = resultStr.substring(0, 14);
		        		System.out.println("收到心跳包：" + heartbeat);
		        	}
		        }

				Device device = deviceMap.get(heartbeat.replace("  ", ""));
				OutPartialCoreLogic outPartialCoreLogic = new OutPartialCoreLogic(device, socket,serverSocket);
				Thread thread = new Thread(outPartialCoreLogic);
				thread.start();
			} catch(Exception e) {
				System.out.println("Abnormal connection with client");	// 与客户端连接异常
				e.printStackTrace();
			}
		}

		// 开一个线程,间隔指定时间查询并设置一次最大值
		for(int i=0;i<1;i++) {
			Timer timer = new Timer();
			timer.schedule(new MaxValue(), 60*1000, 5*60*1000);	// 从现在起过1分钟以后，每隔5分钟执行一次
		}
		
	}
	
	// 建立socket连接
	public static Socket getSocket(ServerSocket serverSocket) {
		Socket socket = null;
		try {
			socket = serverSocket.accept();
			System.out.println(socket.getRemoteSocketAddress() + " Link Success!");
			socket.setSoTimeout(10000);	// 读取超时时间
			return socket;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return socket;
		
	}

}
