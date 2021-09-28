package com.xtw.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ByteToFloat {
	
	public static float toFloat(byte[] bytes) throws IOException {
        ByteArrayInputStream mByteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream mDataInputStream = new DataInputStream(mByteArrayInputStream);
        try {
            return mDataInputStream.readFloat();
        } finally {
            mDataInputStream.close();
            mByteArrayInputStream.close();
        }
    }
	
	
	
	
	public static float toFloat(short s1, short s2)
    {
        //将输入数值short转化为无符号unsigned short
        int us1 = s1, us2 = s2;
        if (s1 < 0) us1 += 65536;
        if (s2 < 0) us2 += 65536;
        //sign: 符号位, exponent: 阶码, mantissa:尾数
        int sign, exponent;
        float mantissa;
        //计算符号位
        sign = us1 / 32768;
        //去掉符号位
        int emCode = us1 % 32768;
        //计算阶码
        exponent = emCode / 128;
        //计算尾数
        mantissa = (float)(emCode % 128 * 65536 + us2) / 8388608;
        //代入公式 fValue = (-1) ^ S x 2 ^ (E - 127) x (1 + M)
        return (float)Math.pow(-1, sign) * (float)Math.pow(2, exponent - 127) * (1 + mantissa);
    }
	
	public static float bytesToFloat(byte[] data) {// 解析4个字节中的数据，按照IEEE754的标准
		int s = 0;// 浮点数的符号
		float f = 0;// 浮点数
		int e = 0;// 指数
		if ((data[3] & 0xff) >= 128) {// 求s
			s = -1;
		} else {
			s = 1;
		}
		int temp = 0;// 指数位的最后一位
		if ((data[2] & 0xff) >= 128) {
			temp = 1;
		} else
			temp = 0;
		e = ((data[3] & 0xff) % 128) * 2 + temp;// 求e
		// f=((data[2]&0xff)-temp*128+128)/128+(data[1]&0xff)/(128*256)+(data[0]&0xff)/(128*256*256);
		float[] data2 = new float[3];
		data2[0] = data[0] & 0xff;
		data2[1] = data[1] & 0xff;
		data2[2] = data[2] & 0xff;
		f = (data2[2] - temp * 128 + 128) / 128 + data2[1] / (128 * 256)
				+ data2[0] / (128 * 256 * 256);
		float result = 0;
		if (e == 0 && f != 0) {// 次正规数
			result = (float) (s * (f - 1) * Math.pow(2, -126));
			return result;
		}
		if (e == 0 && f == 0) {// 有符号的0
			result = (float) 0.0;
			return result;
		}
		if (s == 0 && e == 255 && f == 0) {// 正无穷大
			result = (float) 1111.11;
			return result;
		}
		if (s == 1 && e == 255 && f == 0) {// 负无穷大
			result = (float) -1111.11;
			return result;
		} else {
			result = (float) (s * f * Math.pow(2, e - 127));
			return result;
		}
 
	}
	
	
	public static float bytes2Float(byte[] bytes){
		//获取 字节数组转化成的2进制字符串
		String BinaryStr = bytes2BinaryStr(bytes);
		//符号位S
		Long s = Long.parseLong(BinaryStr.substring(0, 1));
		//指数位E
		Long e = Long.parseLong(BinaryStr.substring(1, 9),2);
		//位数M
		String M = BinaryStr.substring(9);
		float m = 0,a,b;
		for(int i=0;i<M.length();i++){
			a = Integer.valueOf(M.charAt(i)+"");
			b = (float) Math.pow(2, i+1);
			m =m + (a/b);
		}
		Float f = (float) ((Math.pow(-1, s)) * (1+m) * (Math.pow(2,(e-127))));
		return f;
	}
	/**
	 * 将字节数组转换成2进制字符串
	 * @param bytes
	 * @return
	 */
	public static String bytes2BinaryStr(byte[] bytes){
		StringBuffer binaryStr = new StringBuffer();
		for(int i=0;i<bytes.length;i++){
			String str = Integer.toBinaryString((bytes[i] & 0xFF) + 0x100).substring(1);
			binaryStr.append(str);
		}
		return binaryStr.toString();
	}
	
}
