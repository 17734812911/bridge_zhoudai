package com.xtw.utils;

public class CRC16Util {

    /**
     * 	获取源数据和验证码的组合byte数组
     *
     * @param strings 可变长度的十六进制字符串
     * @return
     */
    public static byte[] appendCrc16(String... strings) {
        byte[] data = new byte[]{};
        for (int i = 0; i < strings.length; i++) {
            int x = Integer.parseInt(strings[i], 16);
            byte n = (byte) x;
            byte[] buffer = new byte[data.length + 1];
            byte[] aa = {n};
            System.arraycopy(data, 0, buffer, 0, data.length);
            System.arraycopy(aa, 0, buffer, data.length, aa.length);
            data = buffer;
        }
        return appendCrc16(data);
    }

    
    /**
     *	 获取源数据和验证码的组合byte数组
     *
     * @param a 字节数组
     * @return
     */
    public static byte[] appendCrc16(byte[] a) {
        byte[] b = getCrc16(a);  //计算得到的校验位(第一种方式)
        
        
//        //第二种方式
//    	String[] b0 = CRC16_MODBUS(a);
//    	byte[] b = new byte[2];
//    	//互换高低位,只要将数组b下标调整即可
//    	b[1] = (byte) Integer.parseInt(b0[0],16);
//    	b[0] = (byte) Integer.parseInt(b0[1],16);
    	
        byte[] c = new byte[a.length + b.length];
        //将之前传过来的数据和校验位放在一个数组中
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
    /**
     * 	CRC16(Modbus)  计算得出10进制整数,然后使用intToHex(int i)方法转换为十六进制字符串数组
     * @param buffer
     * @return 
     * @return
     */
    public static String[] CRC16_MODBUS(byte[] buffer) {
        int wCRCin = 0xffff;
        int POLYNOMIAL = 0xa001;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= POLYNOMIAL;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        return MyUtils.intToHex(wCRCin ^= 0x0000);
    }
    
    
    
    /**
     * 	获取验证码byte数组，基于Modbus CRC16的校验算法
     */
    public static byte[] getCrc16(byte[] arr_buff) {
        int len = arr_buff.length;
        
        // 预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
        int crc = 0xFFFF;
        int i, j;
        for (i = 0; i < len; i++) {
        	// 把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
            crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (arr_buff[i] & 0xFF));
            for (j = 0; j < 8; j++) {
            	// 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
                if ((crc & 0x0001) > 0) {
                	 // 如果移出位为 1, CRC寄存器与多项式A001进行异或
                    crc = crc >> 1;
                    crc = crc ^ 0xA001;
                } else
                	// 如果移出位为 0,再次右移一位
                    crc = crc >> 1;
            }
        }
        //将校验码高低位互换
        crc = ( (crc & 0xFF00) >> 8) | ( (crc & 0x00FF) << 8);
        
        return intToBytes(crc);
    }

    /**
     * 	将int转换成byte数组，低位在前，高位在后
     * 	改变高低位顺序只需调换数组下标序号
     */
    private static byte[] intToBytes(int value) {
        byte[] src = new byte[2];
        src[0] = (byte) ((value >> 8) & 0xFF);					// (byte) ((value >> 8) & 0xFF)
        src[1] = (byte) (value & 0xFF);			// (byte) (value & 0xFF)
        return src;
    }
}