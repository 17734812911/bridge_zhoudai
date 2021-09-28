package com.xtw.collect.subject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.xtw.collect.model.ChannelAttribute;
import com.xtw.collect.model.DeviceData;
import com.xtw.publicmodel.Alert;
import com.xtw.publicmodel.Device;
import com.xtw.utils.SqlUtil;

/**
 * 	环境量和表皮测温设备（温度、气体采集）  核心逻辑
 * @author Mr.Chen
 * 2021年7月13日
 */
public class CoreLogic extends TimerTask implements Runnable{
	public int start;
	public int length;
	public Device device;
	public List<ChannelAttribute> channelList;
	private Alert alert = new Alert();
	
	public int number = 0;	// 失败重试计数
	public static double coefficient = 10.00;	// 表皮测温要除以的系数
	public int[] registerValues;
	public ModbusMaster master;
	
	public CoreLogic(int start, int length, Device device, List<ChannelAttribute> channelList) {
		super();
		this.start = start;
		this.length = length;
		this.device = device;
		this.channelList = channelList;
	}

	
	@Override
	public String toString() {
		return "StartThread [start=" + start + ", length=" + length + ", device=" + device + ", channelList="
				+ channelList + ", master=" + master + "]";
	}


	@Override
	public void run() {

		// 设置主机TCP参数
		TcpParameters tcpParameters = new TcpParameters();
		// 设置TCP的ip地址
		InetAddress address = null;
		try {
			address = InetAddress.getByName(device.getDeviceConfig().getDeviceIp());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// TCP参数设置ip地址
		tcpParameters.setHost(address);
		// TCP设置长连接
		tcpParameters.setKeepAlive(true);
		// TCP设置端口
		tcpParameters.setPort(Integer.parseInt(device.getDeviceConfig().getPort()));
		// 创建一个主机
		master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
		master.setResponseTimeout(15000);
		Modbus.setAutoIncrementTransactionId(true);

		while(true) {
			try {
				channelList = SqlUtil.getChannelAttributeList(device.getDeviceConfig().getTerminalId());		// 查询从机下面在使用的通道数量
				requestData(start,length,device, channelList);
				Thread.sleep(device.getDeviceConfig().getPollingInterval());


			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ModbusIOException e) {
				System.out.println("断开连接异常。。。");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void requestData(int start, int length, Device device, List<ChannelAttribute> channelList) throws ModbusIOException {
		try {
 			if (!master.isConnected()) {
				try {
					master.connect();	// 开启连接
					System.out.println("link "+ device.getDeviceConfig().getDeviceIp() +" success。。。");
				}catch(Exception e) {
					System.out.println(device.getDeviceConfig().getDeviceIp() + " link time out");
					return;
				}
			}
 			
 			// 读取对应从机的数据,readInputRegisters读取的写寄存器,功能码04
			registerValues = master.readInputRegisters(1, start, length);

			// 找出同一设备下的所有通道
			List<ChannelAttribute> channelList_identical = new ArrayList<>();
			for(int i=0;i<channelList.size();i++) {
				if(channelList.get(i).getTerminalId().equals(device.getDeviceConfig().getTerminalId())){
					channelList_identical.add(channelList.get(i));
				}

			}

			// 解析数据
			boolean result = analysisAndInsertData(registerValues, device, channelList_identical);
			while(! result) {	// 如果第一次请求失败,重发请求
				if(number == device.getDeviceConfig().getRetryCount()) {
					number = 0;
					break;
				}
				// 读取设备数据
				registerValues = master.readInputRegisters(1, start, length);	// Integer.parseInt(device.getDeviceConfig().getTerminalId())
				System.out.println("失败，第" + (number+1) + "次重试......");
				// 解析数据
				result = analysisAndInsertData(registerValues, device, channelList_identical);
				registerValues = null;
				Thread.sleep(device.getDeviceConfig().getRetryInterval());	// 失败后重发次数中的时间间隔
				number++;
			}
		} catch (Exception e) {
			try {

				System.out.println("连接错误,稍后重试");
				master.disconnect();
				Thread.sleep(5000);
				requestData(start,length,device,channelList);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		} finally {
//			master.disconnect();
			registerValues = null;
		}
	}
	
	/**
     *   	描述：解析数据
     * @param registerValues	// 返回的10个通道的数据数组
     * @param channelList	// 通道属性
     * @return
     */
    private boolean analysisAndInsertData(int[] registerValues, Device device, List<ChannelAttribute> channelList) {
    	boolean flag = false;	// 是否解析存储成功

    	try {
    		DeviceData deviceData = new DeviceData();	// 通道数据对象
			deviceData.setTerminalIp(device.getDeviceConfig().getDeviceIp());		// 设置从机IP
			deviceData.setTerminalId(String.valueOf(device.getDeviceConfig().getTerminalId()));		// 设置从机ID

			for(int i=0;i<channelList.size();i++) {	// 根据该从机下的通道数量遍历返回的数据
				switch(channelList.get(i).getChannelId()){		// 根据通道的ID判断当前通道是否在使用
					case "1":
						if("BPWD".equals(channelList.get(i).getChannelType())) {		// 通道是用于表皮测温的温度
							if(registerValues[0] / coefficient >= Double.parseDouble(channelList.get(i).getCriticalAlarm())){	// 严重告警
								insertDataToAlertTable(device, " 的A1相值严重告警", String.format("%.3f", registerValues[0] / coefficient), String.valueOf(3));
							} else if(registerValues[0] / coefficient >= Double.parseDouble(channelList.get(i).getAlarmUp())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的A1相值告警", String.format("%.3f", registerValues[0] / coefficient), String.valueOf(2));
							} else if(registerValues[0] / coefficient >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的A1相值预警", String.format("%.3f", registerValues[0] / coefficient), String.valueOf(1));
							}
							deviceData.setPassageOne(String.valueOf(registerValues[0] / coefficient));
						}else if("HJL".equals(channelList.get(i).getChannelType())){	// 通道是用于环境量
							double numb = calculation(registerValues[0], channelList.get(i).getRangeUp(), channelList.get(i).getRangeLow(), channelList.get(i).getOffsetValue());
							if(numb >= Double.parseDouble(channelList.get(i).getCriticalAlarm())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的湿度值严重告警", String.format("%.3f", numb), String.valueOf(3));
							} else if(numb >= Double.parseDouble(channelList.get(i).getAlarmUp())){
								insertDataToAlertTable(device, " 的湿度值告警", String.format("%.3f", numb), String.valueOf(2));
							} else if(numb >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的湿度值预警", String.format("%.3f", numb), String.valueOf(1));
							}
							deviceData.setPassageOne(String.format("%.3f", numb));
						}
						break;
					case "2":
						if("BPWD".equals(channelList.get(i).getChannelType())) {		// 通道是用于表皮测温
							if(registerValues[1] / coefficient >= Double.parseDouble(channelList.get(i).getCriticalAlarm())){	// 预警
								insertDataToAlertTable(device, " 的B1相值严重告警", String.format("%.3f", registerValues[1] / coefficient), String.valueOf(3));
							} else if(registerValues[1] / coefficient >= Double.parseDouble(channelList.get(i).getAlarmUp())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的B1相值告警", String.format("%.3f", registerValues[1] / coefficient), String.valueOf(2));
							} else if(registerValues[1] / coefficient >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的B1相值预警", String.format("%.3f", registerValues[1] / coefficient), String.valueOf(1));
							}
							deviceData.setPassageTwo(String.valueOf(registerValues[1] / coefficient));
						}else if("HJL".equals(channelList.get(i).getChannelType())){		// 通道是用于环境量
							double numb = calculation(registerValues[1], channelList.get(i).getRangeUp(), channelList.get(i).getRangeLow(), channelList.get(i).getOffsetValue());
							if(numb >= Double.parseDouble(channelList.get(i).getCriticalAlarm())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的温度值严重告警", String.format("%.3f", numb), String.valueOf(3));
							} else if(numb >= Double.parseDouble(channelList.get(i).getAlarmUp())){
								insertDataToAlertTable(device, " 的温度值告警", String.format("%.3f", numb), String.valueOf(2));
							} else if(numb >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的温度值预警", String.format("%.3f", numb), String.valueOf(1));
							}
							deviceData.setPassageTwo(String.format("%.3f", numb));
						}
						break;
					case "3":
						if("BPWD".equals(channelList.get(i).getChannelType())) {		// 通道是用于表皮测温
							if(registerValues[2] / coefficient >= Double.parseDouble(channelList.get(i).getCriticalAlarm())){	// 预警
								insertDataToAlertTable(device, " 的C1相值严重告警", String.format("%.3f", registerValues[2] / coefficient), String.valueOf(3));
							} else if(registerValues[2] / coefficient >= Double.parseDouble(channelList.get(i).getAlarmUp())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的C1相值告警", String.format("%.3f", registerValues[2] / coefficient), String.valueOf(2));
							} else if(registerValues[2] / coefficient >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的C1相值预警", String.format("%.3f", registerValues[2] / coefficient), String.valueOf(1));
							}
							deviceData.setPassageThree(String.valueOf(registerValues[2] / coefficient));
						}else if("HJL".equals(channelList.get(i).getChannelType())){
							double numb = calculation(registerValues[2], channelList.get(i).getRangeUp(), channelList.get(i).getRangeLow(), channelList.get(i).getOffsetValue());
							if(numb >= Double.parseDouble(channelList.get(i).getCriticalAlarm())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的2号通道值严重告警", String.format("%.3f", numb), String.valueOf(3));
							} else if(numb >= Double.parseDouble(channelList.get(i).getAlarmUp())){
								insertDataToAlertTable(device, " 的2号通道值告警", String.format("%.3f", numb), String.valueOf(2));
							} else if(numb >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的2号通道值预警", String.format("%.3f", numb), String.valueOf(1));
							}
							deviceData.setPassageThree(String.format("%.3f", numb));
						}
						break;
					case "4":
						if("BPWD".equals(channelList.get(i).getChannelType())) {		// 通道是用于表皮测温
							if(registerValues[3] / coefficient >= Double.parseDouble(channelList.get(i).getCriticalAlarm())){	// 预警
								insertDataToAlertTable(device, " 的3号通道值严重告警", String.format("%.3f", registerValues[3] / coefficient), String.valueOf(3));
							} else if(registerValues[3] / coefficient >= Double.parseDouble(channelList.get(i).getAlarmUp())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的3号通道值告警", String.format("%.3f", registerValues[3] / coefficient), String.valueOf(2));
							} else if(registerValues[3] / coefficient >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的3号通道值预警", String.format("%.3f", registerValues[3] / coefficient), String.valueOf(1));
							}
							deviceData.setPassageFour(String.valueOf(registerValues[3] / coefficient));
						}else if("HJL".equals(channelList.get(i).getChannelType())){
							double numb = calculation(registerValues[3], channelList.get(i).getRangeUp(), channelList.get(i).getRangeLow(), channelList.get(i).getOffsetValue());
							if(numb >= Double.parseDouble(channelList.get(i).getCriticalAlarm())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的3号通道值严重告警", String.format("%.3f", numb), String.valueOf(3));
							} else if(numb >= Double.parseDouble(channelList.get(i).getAlarmUp())){
								insertDataToAlertTable(device, " 的3号通道值告警", String.format("%.3f", numb), String.valueOf(2));
							} else if(numb >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的3号通道预警", String.format("%.3f", numb), String.valueOf(1));
							}
							deviceData.setPassageFour(String.format("%.3f", numb));
						}
						break;
					case "5":
						if("BPWD".equals(channelList.get(i).getChannelType())) {		// 通道是用于表皮测温
							if(registerValues[4] / coefficient >= Double.parseDouble(channelList.get(i).getCriticalAlarm())){	// 预警
								insertDataToAlertTable(device, " 的4号通道值严重告警", String.format("%.3f", registerValues[4] / coefficient), String.valueOf(3));
							} else if(registerValues[4] / coefficient >= Double.parseDouble(channelList.get(i).getAlarmUp())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的4号通道值告警", String.format("%.3f", registerValues[4] / coefficient), String.valueOf(2));
							} else if(registerValues[4] / coefficient >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的4号通道值预警", String.format("%.3f", registerValues[4] / coefficient), String.valueOf(1));
							}
							deviceData.setPassageFive(String.valueOf(registerValues[4] / coefficient));
						}else if("HJL".equals(channelList.get(i).getChannelType())){
							double numb = calculation(registerValues[4], channelList.get(i).getRangeUp(), channelList.get(i).getRangeLow(), channelList.get(i).getOffsetValue());
							if(numb >= Double.parseDouble(channelList.get(i).getCriticalAlarm())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的4号通道值严重告警", String.format("%.3f", numb), String.valueOf(3));
							} else if(numb >= Double.parseDouble(channelList.get(i).getAlarmUp())){
								insertDataToAlertTable(device, " 的4号通道值告警", String.format("%.3f", numb), String.valueOf(2));
							} else if(numb >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的4号通道预警", String.format("%.3f", numb), String.valueOf(1));
							}
							deviceData.setPassageFive(String.format("%.3f", numb));
						}
						break;
					case "6":
						if("BPWD".equals(channelList.get(i).getChannelType())) {		// 通道是用于表皮测温
							if(registerValues[5] / coefficient >= Double.parseDouble(channelList.get(i).getCriticalAlarm())){	// 预警
								insertDataToAlertTable(device, " 的A2相值严重告警", String.format("%.3f", registerValues[5] / coefficient), String.valueOf(3));
							} else if(registerValues[5] / coefficient >= Double.parseDouble(channelList.get(i).getAlarmUp())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的A2相值告警", String.format("%.3f", registerValues[5] / coefficient), String.valueOf(2));
							} else if(registerValues[5] / coefficient >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的A2相值预警", String.format("%.3f", registerValues[5] / coefficient), String.valueOf(1));
							}
							deviceData.setPassageSix(String.valueOf(registerValues[5] / coefficient));
						}else if("HJL".equals(channelList.get(i).getChannelType())){
							double numb = calculation(registerValues[5], channelList.get(i).getRangeUp(), channelList.get(i).getRangeLow(), channelList.get(i).getOffsetValue());
							if(numb >= Double.parseDouble(channelList.get(i).getCriticalAlarm())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的含氧量严重告警", String.format("%.3f", numb), String.valueOf(3));
							} else if(numb >= Double.parseDouble(channelList.get(i).getAlarmUp())){
								insertDataToAlertTable(device, " 的含氧量告警", String.format("%.3f", numb), String.valueOf(2));
							} else if(numb >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的含氧量预警", String.format("%.3f", numb), String.valueOf(1));
							} else if(numb <= 19.5){
								insertDataToAlertTable(device, " 的含氧量过低告警", String.format("%.3f", numb), String.valueOf(2));
							}
							deviceData.setPassageSix(String.format("%.3f", numb));
						}
						break;
					case "7":
						if("BPWD".equals(channelList.get(i).getChannelType())) {		// 通道是用于表皮测温
							if(registerValues[6] / coefficient >= Double.parseDouble(channelList.get(i).getCriticalAlarm())){	// 预警
								insertDataToAlertTable(device, " 的B2相值严重告警", String.format("%.3f", registerValues[6] / coefficient), String.valueOf(3));
							} else if(registerValues[6] / coefficient >= Double.parseDouble(channelList.get(i).getAlarmUp())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的B2相值告警", String.format("%.3f", registerValues[6] / coefficient), String.valueOf(2));
							} else if(registerValues[6] / coefficient >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的B2相值预警", String.format("%.3f", registerValues[6] / coefficient), String.valueOf(1));
							}
							deviceData.setPassageSeven(String.valueOf(registerValues[6] / coefficient));
						}else if("HJL".equals(channelList.get(i).getChannelType())){
							double numb = calculation(registerValues[6], channelList.get(i).getRangeUp(), channelList.get(i).getRangeLow(), channelList.get(i).getOffsetValue());
							if(numb >= Double.parseDouble(channelList.get(i).getCriticalAlarm())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的二氧化碳浓度严重告警", String.format("%.3f", numb), String.valueOf(3));
							} else if(numb >= Double.parseDouble(channelList.get(i).getAlarmUp())){
								insertDataToAlertTable(device, " 的二氧化碳浓度告警", String.format("%.3f", numb), String.valueOf(2));
							} else if(numb >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的二氧化碳浓度预警", String.format("%.3f", numb), String.valueOf(1));
							}
							deviceData.setPassageSeven(String.format("%.3f", numb));
						}
						break;
					case "8":
						if("BPWD".equals(channelList.get(i).getChannelType())) {		// 通道是用于表皮测温
							if(registerValues[7] / coefficient >= Double.parseDouble(channelList.get(i).getCriticalAlarm())){	// 严重告警
								insertDataToAlertTable(device, " 的C2相值严重告警", String.format("%.3f", registerValues[7] / coefficient), String.valueOf(3));
							} else if(registerValues[7] / coefficient >= Double.parseDouble(channelList.get(i).getAlarmUp())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的C2相值告警", String.format("%.3f", registerValues[7] / coefficient), String.valueOf(2));
							} else if(registerValues[7] / coefficient >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的C2相值预警", String.format("%.3f", registerValues[7] / coefficient), String.valueOf(1));
							}
							deviceData.setPassageEight(String.valueOf(registerValues[7] / coefficient));
						}else if("HJL".equals(channelList.get(i).getChannelType())){
							double numb = calculation(registerValues[7], channelList.get(i).getRangeUp(), channelList.get(i).getRangeLow(), channelList.get(i).getOffsetValue());
							if(numb >= Double.parseDouble(channelList.get(i).getCriticalAlarm())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的硫化氢严重告警", String.format("%.3f", numb), String.valueOf(3));
							} else if(numb >= Double.parseDouble(channelList.get(i).getAlarmUp())){
								insertDataToAlertTable(device, " 的硫化氢告警", String.format("%.3f", numb), String.valueOf(2));
							} else if(numb >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的硫化氢预警", String.format("%.3f", numb), String.valueOf(1));
							}
							deviceData.setPassageEight(String.format("%.3f", numb));
						}
						break;
					case "9":
						if("BPWD".equals(channelList.get(i).getChannelType())) {		// 通道是用于表皮测温
							if(registerValues[8] / coefficient >= Double.parseDouble(channelList.get(i).getCriticalAlarm())){	// 预警
								insertDataToAlertTable(device, " 的8号通道严重告警", String.format("%.3f", registerValues[8] / coefficient), String.valueOf(3));
							} else if(registerValues[8] / coefficient >= Double.parseDouble(channelList.get(i).getAlarmUp())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的8号通道告警", String.format("%.3f", registerValues[8] / coefficient), String.valueOf(2));
							} else if(registerValues[8] / coefficient >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的8号通道预警", String.format("%.3f", registerValues[8] / coefficient), String.valueOf(1));
							}
							deviceData.setPassageNine(String.valueOf(registerValues[8] / coefficient));
						}else if("HJL".equals(channelList.get(i).getChannelType())){
							double numb = calculation(registerValues[8], channelList.get(i).getRangeUp(), channelList.get(i).getRangeLow(), channelList.get(i).getOffsetValue());
							if(numb >= Double.parseDouble(channelList.get(i).getCriticalAlarm())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的甲烷含量严重告警", String.format("%.3f", numb), String.valueOf(3));
							} else if(numb >= Double.parseDouble(channelList.get(i).getAlarmUp())){
								insertDataToAlertTable(device, " 的甲烷含量告警", String.format("%.3f", numb), String.valueOf(2));
							} else if(numb >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的甲烷含量预警", String.format("%.3f", numb), String.valueOf(1));
							}
							deviceData.setPassageNine(String.format("%.3f", numb));
						}
						break;
					case "10":
						if("BPWD".equals(channelList.get(i).getChannelType())) {		// 通道是用于表皮测温
							if(registerValues[9] / coefficient >= Double.parseDouble(channelList.get(i).getCriticalAlarm())){	// 预警
								insertDataToAlertTable(device, " 的9号通道严重告警", String.format("%.3f", registerValues[9] / coefficient), String.valueOf(3));
							} else if(registerValues[9] / coefficient >= Double.parseDouble(channelList.get(i).getAlarmUp())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的9号通道告警", String.format("%.3f", registerValues[9] / coefficient), String.valueOf(2));
							} else if(registerValues[9] / coefficient >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的9号通道预警", String.format("%.3f", registerValues[9] / coefficient), String.valueOf(1));
							}
							deviceData.setPassageTen(String.valueOf(registerValues[9] / coefficient));
						}else if("HJL".equals(channelList.get(i).getChannelType())){
							double numb = calculation(registerValues[9], channelList.get(i).getRangeUp(), channelList.get(i).getRangeLow(), channelList.get(i).getOffsetValue());
							if(numb >= Double.parseDouble(channelList.get(i).getCriticalAlarm())) {	// 如果超过报警值
								insertDataToAlertTable(device, " 的9号通道严重报警", String.format("%.3f", numb), String.valueOf(3));
							} else if(numb >= Double.parseDouble(channelList.get(i).getAlarmUp())){
								insertDataToAlertTable(device, " 的9号通道告警", String.format("%.3f", numb), String.valueOf(2));
							} else if(numb >= Double.parseDouble(channelList.get(i).getWarningValue())){
								insertDataToAlertTable(device, " 的9号通道预警", String.format("%.3f", numb), String.valueOf(1));
							}
							deviceData.setPassageTen(String.format("%.3f", numb));
						}
						break;
				}
			}

//			System.out.println(device.getDeviceConfig().getDeviceIp() + " == "+ deviceData.toString());

//			if("128.0.0.3".equals(device.getDeviceConfig().getDeviceIp())){
//				DecimalFormat df = new DecimalFormat("#.000");
//				// 读取128.0.0.4的数据
//				DeviceData deviceData1 = SqlUtil.queryDataEnvironment();
//				deviceData.setPassageTwo(df.format(Double.parseDouble(deviceData1.getPassageTwo()) + 0.8));
//				deviceData.setPassageEight(df.format(Double.parseDouble(deviceData1.getPassageEight()) + 0.8));
//			}
//			if("128.0.0.2".equals(device.getDeviceConfig().getDeviceIp())){
//				DecimalFormat df = new DecimalFormat("#.000");
//				// 读取128.0.0.4的数据
//				DeviceData deviceData1 = SqlUtil.queryDataEnvironment();
//				deviceData.setPassageOne(df.format(Double.parseDouble(deviceData1.getPassageOne()) + 0.8));
//				deviceData.setPassageTwo(df.format(Double.parseDouble(deviceData1.getPassageTwo()) + 0.8));
//				deviceData.setPassageSix(df.format(Double.parseDouble(deviceData1.getPassageSix()) + 0.8));
//				deviceData.setPassageSeven(df.format(Double.parseDouble(deviceData1.getPassageSeven()) + 0.8));
//				deviceData.setPassageEight(df.format(Double.parseDouble(deviceData1.getPassageEight()) + 0.8));
//				deviceData.setPassageNine(df.format(Double.parseDouble(deviceData1.getPassageNine()) + 0.8));
//
//			}


			if(deviceData.getPassageOne() != null & deviceData.getPassageTwo() != null ){
				// 存储到数据库
				int result = SqlUtil.insertChannelData(deviceData);
				if(result > 0) {
					flag = true;
					System.out.println("====保存数据成功====");
					// 更新设备表中该设备最新数据时间
					SqlUtil.lastDataTime(device.getDeviceConfig().getTerminalId());
				}else {
					flag = true;
					System.out.println("====保存数据失败====");
				}
				return true;
			}
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
		return flag;
    }

    /**
     * 	描述：计算环境量的通道值公式
     * @param rangeUp			量程上限
     * @param rangeLow			量程下限
     * @param offsetValue		偏移量
     * @return
     */
	private static double calculation(int registerValue, int rangeUp, int rangeLow, double offsetValue) {
		double registerValue1= registerValue / 1000.00;		// 环境量的十进制数要除以1000.00再计算
		if(registerValue1<=4) {
			return rangeLow;
		}else if(registerValue1>=20){
			return rangeUp;
		}

		double a = (registerValue1 - 4) / (20 - 4) * (rangeUp - rangeLow) + rangeLow + offsetValue;

		return a;
    }
	
	// 向报警表插入报警信息
	private void insertDataToAlertTable(Device device, String content, String number,String type) {
		alert.setLineId(device.getLineId());
		alert.setTerminalId(device.getDeviceConfig().getTerminalId());
		alert.setContent((device.getDeviceConfig().getPartitionId()) +  "号接头 " + device.getName() + "设备" + content + ",值为：" + number);
		alert.setAlertType(type);	// 告警类型
		alert.setAlertData(number);
		try {
			SqlUtil.insertAlertData(alert);
		} catch(Exception e) {
			System.out.println("插入报警表数据失败");
		}
	}
}
