package com.xtw.utils;

import java.io.IOException;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.sero.util.queue.ByteQueue;

public class ReadSlaveUtil {

	public static ByteQueue modbusTCP(String ip, int port, int start,int readLenth) throws IOException {  // 从站ip,端口
        ModbusFactory modbusFactory = new ModbusFactory();  
        // 设备ModbusTCP的Ip与端口，如果不设定端口则默认为502  
        IpParameters params = new IpParameters();  
        params.setHost(ip);
        //设置端口，默认502
        if(502!=port){
            params.setPort(port);
        }  
        ModbusMaster tcpMaster = null;  
        tcpMaster = modbusFactory.createTcpMaster(params, true);  
        try {  
            tcpMaster.init();  
            System.out.println("========初始化成功=======");  
        } catch (ModbusInitException e) {  
            return null;  
        }
        ModbusRequest modbusRequest=null;
        try {
            //功能码03   读取保持寄存器的值
            modbusRequest = new ReadInputRegistersRequest(1, start, readLenth);  //    ReadHoldingRegistersRequest
        } catch (ModbusTransportException e) {
            e.printStackTrace();
        }
        ModbusResponse modbusResponse=null;
        try {
            modbusResponse = tcpMaster.send(modbusRequest);
        } catch (ModbusTransportException e) {
            e.printStackTrace();  
        }
        ByteQueue byteQueue= new ByteQueue(23);
        modbusResponse.write(byteQueue);
        
//        System.out.println(ByteToFloat.toFloat((short) 8003, (short) 8001));					
        
        byte[] array = new byte[2];
        int j = 0;
        for(int i=0;i<byteQueue.size()-3;i++){
        	array[j] = (byte)byteQueue.peek(i + 3);
        	if(j != 0 & j%1 == 0) {
//        		array = reverse(array);
//        		for(byte b : array) {
//        			System.out.println("array数组的值为：" + b);
//        		}
        		      		
        		int result = bytesToInt(array);
        		System.out.println(result/10);
        		
//        	    System.out.println(new DecimalFormat("0.00").format(result));
        		
        		
        		j = 0;
        	}else {
        		j += 1;
        	}
        }
        
        System.out.println("功能码:"+modbusRequest.getFunctionCode());
        System.out.println("从站地址:"+modbusRequest.getSlaveId());
        System.out.println("收到的响应信息大小:"+byteQueue.size());
        System.out.println("收到的响应信息值:"+byteQueue);
        return byteQueue;
    }
	
	public static byte[] reverse(byte[] arr){
          //遍历数组
          for(int i = 1;i < arr.length / 2;i++){
              //交换元素
              byte temp = (byte) arr[arr.length -i];
              arr[arr.length -i] = arr[i];
              arr[i] = temp;
         }
         //返回反转后的结果
         return arr;
     }
	
    public static int bytesToInt(byte[] bs) {
        int a = 0;
        for (int i = bs.length - 1; i >= 0; i--) {
            a += bs[i] * Math.pow(255, bs.length - i - 1);
        }
        return a;
    }
	
}
