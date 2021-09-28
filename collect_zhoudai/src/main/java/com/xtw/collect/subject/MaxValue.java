package com.xtw.collect.subject;

import java.sql.SQLException;
import java.util.TimerTask;

import com.xtw.collect.model.DeviceData;
import com.xtw.outdischarge.model.OutPartial;
import com.xtw.publicmodel.DeviceMaxValue;
import com.xtw.publicmodel.FibreTemperature;
import com.xtw.utils.SqlUtil;

/**
 * 	查询并设置各种设备的最大值
 * @author Mr.Chen
 * 2021年8月27日
 */
public class MaxValue extends TimerTask {
	
	@Override
	public void run(){
		try{//异常处理
            setMaxValue();
         }
         	catch(Exception e){
         		System.out.println("执行最大值计算任务出错");
         		e.printStackTrace();
         }
	}
	
	public void setMaxValue() {
		
		try {
			// 查询环境量最近七天最大值
			DeviceMaxValue deviceMaxValue = SqlUtil.maxValue();
			// 查询表皮温度七天最大值
			DeviceData deviceData = SqlUtil.maxValueBpwd();
			String bpwdMaxValue = sortValue(deviceData);	// 获取表皮温度所有通道最大值
			deviceMaxValue.setBpwd(bpwdMaxValue);			// 将表皮温度的最大值设置到最大值实体类中
			
			// 获取外置局放最近七天最大值
			OutPartial outPartial = SqlUtil.maxValueWzjf();
			String maxOutPartial = sortOutpartial(outPartial);	// 获取外置局放三相最大值
			deviceMaxValue.setWzjf(maxOutPartial);			// 将外置局放的最大值设置到最大值实体类中
			
			// 获取光纤测温最近七天最大值
			FibreTemperature fibreTemperature = SqlUtil.maxValueGxcw();
			deviceMaxValue.setGxcw(String.valueOf(fibreTemperature.getMaxValue()));		// 将光纤测温的最大值设置到最大值实体类中
			
			// 向最大值表(maxvalue)中更新数据
			int result = SqlUtil.updateMaxValue(deviceMaxValue);
			if(result>0){
				System.out.println("更新设备最大值成功");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 获取表皮温度所有通道最大值
	private static String sortValue(DeviceData deviceData) {
		String max = deviceData.getPassageOne();
		if(Double.parseDouble(deviceData.getPassageTwo()) > Double.parseDouble(max)) {
			max = deviceData.getPassageTwo();
		} else if(Double.parseDouble(deviceData.getPassageThree()) > Double.parseDouble(max)) {
			max = deviceData.getPassageThree();
		} else if(Double.parseDouble(deviceData.getPassageSix()) > Double.parseDouble(max)) {
			max = deviceData.getPassageSix();
		} else if(Double.parseDouble(deviceData.getPassageSeven()) > Double.parseDouble(max)) {
			max = deviceData.getPassageSeven();
		} else if(Double.parseDouble(deviceData.getPassageEight()) > Double.parseDouble(max)){
			max = deviceData.getPassageEight();
		}
		return max;
	}
	// 获取外置局放三相最大值
	private static String sortOutpartial(OutPartial outPartial) {
		Integer max = outPartial.getaMaxElectric();
		if(outPartial.getbMaxElectric() > max) {
			max = outPartial.getbMaxElectric();
		} else if(outPartial.getcMaxElectric() > max) {
			max = outPartial.getcMaxElectric();
		}
		return String.valueOf(max);
		
	}
}
