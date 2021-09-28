package com.xtw.outdischarge.subject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.xtw.MainClass;
import com.xtw.outdischarge.model.OutPartial;
import com.xtw.outdischarge.model.OutPartialAlarmConfig;
import com.xtw.publicmodel.Alert;
import com.xtw.publicmodel.Device;
import com.xtw.utils.CRC16Util;
import com.xtw.utils.MyUtils;
import com.xtw.utils.SqlUtil;

/**
 * 	外置局放核心逻辑
 * @author Mr.Chen
 * 2021年7月13日
 */
public class OutPartialCoreLogic extends TimerTask implements Runnable{
	
	private Device device;
    private Socket socket;
    public ServerSocket serverSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private byte[] bytes = new byte[9];
    private int length = 0;
    private int retransmission=0;	// 重发次数
    private StringBuffer allMessage = new StringBuffer();	// 用于存放5次请求返回的所有数据
    public HashMap<Integer,String> map = new HashMap<>();	// 存放外置局放分包返回的数据
    private int timeOutResentNum = 0;	// 读取超时,重发次数
    private OutPartialAlarmConfig outPartialAlarmConfig;	// 外置局放报警配置
    private Alert alert = new Alert();
    
	public OutPartialCoreLogic(Device device, Socket socket,ServerSocket serverSocket) throws DecoderException, IOException {
		super();
		this.device = device;
		this.serverSocket = serverSocket;
		this.socket = socket;
		// 将terminalId十六进制字符串转换为byte数组
		this.bytes = stringToByteArray(device.getDeviceConfig().getTerminalId());
		this.inputStream = this.socket.getInputStream();
		this.outputStream = this.socket.getOutputStream();
		this.socket.setSoTimeout(10000);	// 读取超时时间
	}

	@Override
	public void run() {
		try {
			outPartialAlarmConfig = SqlUtil.getOutPartialAlarmConfig(device.getDeviceConfig().getTerminalId());
		} catch (SQLException se) {
			System.out.println("查询外置局放配置失败");
		}
		
		while(true) {
			try {
				try {
					sendCommand();	// 发送命令
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				Thread.sleep(device.getDeviceConfig().getPollingInterval());	// 指定时间后进行下一次获取数据
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 向客户端发送指令
	public void sendCommand() throws IOException, InterruptedException {

		bytes[4] = 0x03;
		allMessage.delete(0, allMessage.length());	// 每次开始,清空集合
		boolean flag = true;	// 循环控制条件
		String value = null;	// 从共享变量集合中取回的值
		int resultNum = 0;		// 返回的数据包总数
		int numbers=0;	// 表示从集合中重复获取了几次数据
		allMessage.append(bytes[0]+"  " + bytes[1]+"  " + bytes[2]+"  " + bytes[3] + "  " + bytes[4] + "  " + "08" + "  " + "70" + "  ");
		for(int num=0;num<5;num++) {
			switch(num) {
				case 0:
					length = 903;
					bytes[5] = 0x00;
					bytes[6] = 0x00;
					bytes[7] = 0x01;
					bytes[8] = (byte) 0xBF;
					break;
				case 1:
					length = 901;
					bytes[5] = 0x01;
					bytes[6] = (byte) 0xBF;
					bytes[7] = 0x01;
					bytes[8] = (byte) 0xBE;
					break;
				case 2:
					length = 899;
					bytes[5] = 0x03;
					bytes[6] = 0x7D;
					bytes[7] = 0x01;
					bytes[8] = (byte) 0xBD;
					break;
				case 3:
					length = 897;
					bytes[5] = 0x05;
					bytes[6] = (byte) 0x3A;
					bytes[7] = 0x01;
					bytes[8] = (byte) 0xBC;
					break;
				case 4:
					length = 765;
					bytes[5] = 0x06;
					bytes[6] = (byte) 0xF6;
					bytes[7] = 0x01;
					bytes[8] = (byte) 0x7A;
					break;
			}
			

			try {
				
	            // 生成CRC校验码并合并bytes中的数据,构成完整指令
	            byte[] sentOutBytes = CRC16Util.appendCrc16(bytes);
	            // 获取数据
	            String resultStr = getDatas(sentOutBytes, length);
            	
	        	if(resultStr != null) {
//	        		System.out.println("resultStr==" + resultStr);
	        		// 将数据放入集合map中
		        	map.put(num, resultStr);
//		        	System.out.println(resultStr.substring(0, 14) + "的第" + num + "条数据放入集合成功");
	        	} else {
	        		System.out.println("重发3次没有数据返回");

	        		return;
	        	}
	        	
	        	
	        	// 从map中获取请求的元素
	        	while(flag) {
	        		numbers++;	// 取数据的次数加1
	        		try {
	        			value = map.get(num);
//	        			System.out.println(resultStr.substring(0, 14) + "的value==" + value);
	        			if(value != null) {
	        				map.remove(num);		// 如果值取出来了,就将共享集合中的键值对删除
	        				break;
	        			}
	        		} catch(Exception e) {
	        			System.out.println("map中没有该键值对");
	        		}
	        		
	        		if(numbers >= 3) {		// 只重复取3次,避免一直没数据,陷入死循环
	        			break;
        			} else {
        				Thread.sleep(2000);		// 从集合中取数据的间隔,过两秒再取
        			}
	        	}
	        	
	        	if(value != null) {
	        		resultNum++;	// 返回的数据包总数加1
//	        		System.out.println(resultStr.substring(0, 14) + "返回的数据包总数=" + resultNum);
	        		
	        		allMessage.append(value.substring(28,value.length()-8));
	        		if(num != 4) {
	        			allMessage.append("  ");
	        		}
	        		value = null;	// 将value清空
	        	}
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			Thread.sleep(1000);		// 取下一包数据的间隔时间
		}
//		System.out.println(device.getDeviceConfig().getTerminalId() + " 设备的整包数据====" + allMessage);
		
		if(resultNum == 5) {	// 返回的数据包数量正确
			// 处理返回的所有数据
			System.out.println(device.getDeviceConfig().getTerminalId() + "取到了完整数据包");
			
			OutPartial outPartial = processData(allMessage);
			// 清空allMessage
			allMessage.setLength(0);
			
			// 存储数据
			saveData(outPartial);
			
		} else {
			System.out.println("外置局放 " + device.getDeviceConfig().getTerminalId() + " 返回的数据包不完整,已丢弃");
		}
	}
	
	// 处理返回的所有数据
	private OutPartial processData(StringBuffer allMessage) {	// allMessage中的数据已经有两个空格的间隔
		// 外置局放实体类
		OutPartial outPartial = new OutPartial();
		String[] allDatas = allMessage.toString().split("  ");
		
		int[] dischargeA = interceptDatas(allDatas, 7, 726);
		String dischargeAStr = arrayToString(dischargeA);			// A相放电波形
		int maxDischargeA = arraySort(dischargeA);					// 获取A相最大放电量

		int[] frequencyA = interceptDatas(allDatas, 727, 1446);
		String frequencyAStr = arrayToString(frequencyA);			// A相放电频次
		int maxFrequencyA = arraySort(frequencyA);					// A相最大放电频次

		// 判断是否需要报警
		if(maxDischargeA >= outPartialAlarmConfig.getCriticalAlarm() && maxFrequencyA >= outPartialAlarmConfig.getAlarmFrequencyValue()){
			insertDataToAlertTable(maxDischargeA + outPartialAlarmConfig.getOffsetValue() + "", " 的A相放电严重报警", String.valueOf(3));
		} else if(maxDischargeA >= outPartialAlarmConfig.getAlarmValue() && maxFrequencyA >= outPartialAlarmConfig.getAlarmFrequencyValue()) {
			insertDataToAlertTable(maxDischargeA + outPartialAlarmConfig.getOffsetValue() + "", " 的A相放电告警", String.valueOf(2));
		} else if(maxDischargeA >= outPartialAlarmConfig.getWarningValue() && maxFrequencyA >= outPartialAlarmConfig.getAlarmFrequencyValue()){
			insertDataToAlertTable(maxDischargeA + outPartialAlarmConfig.getOffsetValue() + "", " 的A相放电预警", String.valueOf(1));
		}
		
		int[] dischargeB = interceptDatas(allDatas, 1447, 2166);
		String dischargeBStr = arrayToString(dischargeB);			// B相放电波形
		int maxDischargeB = arraySort(dischargeB);					// 获取B相最大放电量

		int[] frequencyB = interceptDatas(allDatas, 2167, 2886);
		String frequencyBStr = arrayToString(frequencyB);			// A相放电频次
		int maxFrequencyB = arraySort(frequencyB);					// B相最大放电频次

		// 判断是否需要报警
		if(maxDischargeB >= outPartialAlarmConfig.getCriticalAlarm() && maxFrequencyB >= outPartialAlarmConfig.getAlarmFrequencyValue()){
			insertDataToAlertTable(maxDischargeB + outPartialAlarmConfig.getOffsetValue() + "", " 的B相放电严重报警", String.valueOf(3));
		} else if(maxDischargeB >= outPartialAlarmConfig.getAlarmValue() && maxFrequencyB >= outPartialAlarmConfig.getAlarmFrequencyValue()) {
			insertDataToAlertTable(maxDischargeB + outPartialAlarmConfig.getOffsetValue() + "", " 的B相放电报警", String.valueOf(2));
		} else if(maxDischargeB >= outPartialAlarmConfig.getWarningValue() && maxFrequencyB >= outPartialAlarmConfig.getAlarmFrequencyValue()){
			insertDataToAlertTable(maxDischargeB + outPartialAlarmConfig.getOffsetValue() + "", " 的B相放电预警", String.valueOf(1));
		}
		
		
		int[] dischargeC = interceptDatas(allDatas, 2887, 3606);
		String dischargeCStr = arrayToString(dischargeC);			// C相放电波形
		int maxDischargeC = arraySort(dischargeC);					// 获取C相最大放电量
		int[] frequencyC = interceptDatas(allDatas, 3607, 4326);
		String frequencyCStr = arrayToString(frequencyC);			// A相放电频次
		int maxFrequencyC = arraySort(frequencyC);					// C相最大放电频次

		// 判断是否需要报警
		if(maxDischargeC >= outPartialAlarmConfig.getCriticalAlarm() && maxFrequencyC >= outPartialAlarmConfig.getAlarmFrequencyValue()){
			insertDataToAlertTable(maxDischargeC + outPartialAlarmConfig.getOffsetValue() + "", " 的C相放电严重报警", String.valueOf(3));
		} else if(maxDischargeC >= outPartialAlarmConfig.getAlarmValue() && maxFrequencyC >= outPartialAlarmConfig.getAlarmFrequencyValue()) {
			insertDataToAlertTable(maxDischargeC + outPartialAlarmConfig.getOffsetValue() + "", " 的C相放电报警", String.valueOf(2));
		} else if(maxDischargeC >= outPartialAlarmConfig.getWarningValue() && maxFrequencyC >= outPartialAlarmConfig.getAlarmFrequencyValue()){
			insertDataToAlertTable(maxDischargeC + outPartialAlarmConfig.getOffsetValue() + "", " 的C相放电预警", String.valueOf(1));
		}
		
		String terminalId = device.getDeviceConfig().getTerminalId();	// 设备ID
		
		outPartial.setTerminalId(terminalId);
		outPartial.setaElectric(dischargeAStr);
		outPartial.setaFrequency(frequencyAStr);
		outPartial.setaMaxElectric(maxDischargeA);
		outPartial.setaMaxFrequency(maxFrequencyA);
		
		outPartial.setbElectric(dischargeBStr);
		outPartial.setbFrequency(frequencyBStr);
		outPartial.setbMaxElectric(maxDischargeB);
		outPartial.setbMaxFrequency(maxFrequencyB);
		
		outPartial.setcElectric(dischargeCStr);
		outPartial.setcFrequency(frequencyCStr);
		outPartial.setcMaxElectric(maxDischargeC);
		outPartial.setcMaxFrequency(maxFrequencyC);
		return outPartial;
	}
	
	// 储存数据
	private void saveData(OutPartial outPartial) {
		try {
			int result = SqlUtil.insertOutPartialData(outPartial);
			if(result > 0) {
				System.out.println("外置局放数据保存成功！");
				// 更新该设备的最后存入数据时间
				SqlUtil.lastDataTime(outPartial.getTerminalId());
			} else {
				System.out.println("外置局放数据保存失败！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	private synchronized String getDatas(byte[] sentOutBytes, int length) throws IOException, InterruptedException {
		try {
			
			if(inputStream.available() != 0) {	// 丢掉可能有的脏数据
				inputStream.read(new byte[1024]);
			}
			
			// 发送数据
			for(byte b : sentOutBytes) {
            	System.out.print(b + " ");
            }
			
            outputStream.write(sentOutBytes);
            System.out.print(" Sent Out OK  ");
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            Thread.sleep(1000);
            
            //接收客户端的消息
            byte[] message = new byte[length];
            inputStream.read(message);
            String resultStr = MyUtils.printHexString(message);		// 将返回的字节数组转换为字符串
            
//        	System.out.println("resultStr===" + resultStr);
        	
        	
            // 进行CRC校验,如果不正确,重新发送这一包的命令
            //将字符串转换为字符串数组，获取从机返回的校验位
            String[] strCheck = resultStr.split("  ");  //字符串中间是两个空格
            //获取从机返回的校验位
        	String check_result = strCheck[strCheck.length-2] + strCheck[strCheck.length-1];
        	byte[] dataByte = new byte[strCheck.length-2];
            for(int m=0;m<dataByte.length;m++) {
            	dataByte[m] = MyUtils.HexString2Bytes(strCheck[m])[0];
            }
            //用新数组计算校验位，返回的是数据加上校验位的数组
        	byte[] result = CRC16Util.appendCrc16(dataByte);
        	// 将字节数组转字符串
        	String checkResultStr = MyUtils.printHexString(result);
        	//将字符串分割成字符串数组，取得校验位
        	String[] resultStrArr = checkResultStr.split("  ");  //字符串中间是两个空格
        	//获取计算的校验位
        	String check_calculation = resultStrArr[resultStrArr.length-2] + resultStrArr[resultStrArr.length-1];
        	
        	//将计算的校验位check_calculation与从机返回的校验位check_result对比
        	System.out.println("返回的校验位：" + check_result);
			System.out.println("计算的校验位：" + check_calculation);
        	if(check_calculation.equals(check_result)) {
        		retransmission = 0;
        		return resultStr;
        	}else {
        		retransmission++;
        		System.out.println(strCheck[2] + "  " + strCheck[3] + " CRC校验不通过,重新获取数据");
        		if(retransmission==3) {
        			retransmission=0;
        			return null;
        		}
        		Thread.sleep(1000);
        		getDatas(sentOutBytes, length);
        	}
            
		} catch(SocketException se) {
			socket = serverSocket.accept();
			this.inputStream = socket.getInputStream();
			this.outputStream = socket.getOutputStream();
			
			// 再次调用
			getDatas(sentOutBytes, length);
			
		} catch(SocketTimeoutException e) {
			if(timeOutResentNum < 3) {	// 超时重发次数没有3次
				timeOutResentNum++;
				System.out.println("读取数据超时,重新发送命令");
				// 重新获取数据
				getDatas(sentOutBytes, length);
			}else {
				timeOutResentNum = 0;	// 将超时重发次数置为0

				// 重新建立TCP连接
				try {
					this.socket.close();
					Thread.sleep(500);
					this.socket = MainClass.getSocket(serverSocket);

					this.inputStream = this.socket.getInputStream();
					this.outputStream = this.socket.getOutputStream();
				} catch(Exception ine) {
					ine.printStackTrace();
				}
			}
		} catch(IOException e) {
			try {
				this.inputStream = this.socket.getInputStream();
				this.outputStream = this.socket.getOutputStream();
			} catch(Exception socke) {
				e.printStackTrace();
			}
			// 再次获取数据
			getDatas(sentOutBytes, length);
		} 
		return null;	// 三次重发都没有数据,就返回null
	}
	
	// 用于将十六进制字符换转换为byte数组
	private byte[] stringToByteArray(String terminalId) throws DecoderException {
		byte[] terminalIdArray = Hex.decodeHex(terminalId);
		
		for(int j=0;j<terminalIdArray.length;j++) {
			bytes[j] = terminalIdArray[j];
		}		
		return bytes;
	}
	
	// 截取放电量波形数据
	private int[] interceptDatas(String[] allDatas, int start, int end) {
		int[] datas = new int[(end-start+1)/2];
		int[] datas1 = new int[(end-start+1)/2];
		for(int i=0;i<(end-start+1)/2;i++) {
			datas[i] = MyUtils.getNum_Int(allDatas[i*2+1+start]+allDatas[i*2+start]);		// 将十六进制字符串转换为十进制数字
		}

		for(int j=0;j<datas.length;j++){	// 处理局放值
			datas1[j] = max(datas[j]);
		}

		return datas1;
	}
	
	// 获取数组最大值
	private int arraySort(int[] datas) {
		Arrays.sort(datas);		// 将数组按从小到大排序
		return datas[datas.length-1];
	}
	
	// 将int数组转换为字符串
	private String arrayToString(int[] datas) {
		String datasStr = Arrays.toString(datas);
		return datasStr.substring(1, datasStr.length()-1).replace(" ","");		// 截取两端的中括号,再去掉中间的所有空格
	}
	
	// 向报警表插入外置局放报警信息
	private void insertDataToAlertTable(String alertData, String content,String type) {
		alert.setLineId(outPartialAlarmConfig.getPartitionId());	// 将分区号作为线路号
		alert.setTerminalId(outPartialAlarmConfig.getTerminalId());
		alert.setContent(outPartialAlarmConfig.getPartitionId() + " 号接头外置局放设备 " + outPartialAlarmConfig.getTerminalId() + content);
		alert.setAlertType(type);
		alert.setAlertData(alertData);
		try {
			SqlUtil.insertAlertData(alert);
		} catch(Exception e) {
			System.out.println("插入报警表数据失败");
		}
	}

	// 处理局放最大值
	private int max(int value){
		if(value > 100){
			value = (value / 100 ) + 8;
		} else if(value > 3){
			value = (value / 20 ) + 3;
		}
		return value;
	}
}
