package com.xtw.bridge.service;

import com.xtw.bridge.mapper.FibreTemperatureDao;
import com.xtw.bridge.model.FibreTemperature;
import com.xtw.bridge.model.FibreTemperatureConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/19
 * Description: No Description
 */
@Service
public class FibreTemperatureService implements FibreTemperatureDao {

    @Resource
    FibreTemperatureDao fibreTemperatureDao;
    // 光纤测温配置
    List<FibreTemperatureConfig> fibreTemperatureConfigs = new ArrayList<FibreTemperatureConfig>();

    @Override
    public List<FibreTemperatureConfig> queryFibreTemperatureConfig(String deviceIp, String channel) {
        return fibreTemperatureDao.queryFibreTemperatureConfig(deviceIp, channel);
    }

    @Override
    public int insertData(FibreTemperature fibreTemperature) {
        int result = 0;
        int maxValueIndex = 0;
        FibreTemperature fibreTemperaturePojo = new FibreTemperature();
        String[] strArray = fibreTemperature.getDatas().split(",");
        List<String> strList = Arrays.asList(strArray);     // 将数组转换为集合，用于截取数据
        // StringUtils.join(list,",")   // 将集合转换为字符串
        // 先根据配备IP和通道ID,查询光纤测温配置
        fibreTemperatureConfigs = queryFibreTemperatureConfig(fibreTemperature.getDeviceIp(), fibreTemperature.getChannel());
        // 根据配置处理数据
        for(FibreTemperatureConfig config : fibreTemperatureConfigs){
            if(config.getDeviceIp().equals(fibreTemperature.getDeviceIp()) && config.getChannel().equals(fibreTemperature.getChannel())){
                int start = config.getStartPosition();  // 起始位置
                int end = config.getEndPosition();      // 结束位置
                List<String> list = strList.subList(start, end + 1);       // 根据起止点位截取的集合
                String subDatas = StringUtils.join(list,",");   // 根据起止点位截取的字符串

                String[] subListArr = new String[list.size()];
                list.toArray(subListArr);   // 将截取的list转换为数组

                // 排序
                double[] doubleArr = toDoubleArray(subListArr);
                arraySort(doubleArr);
                double maxValue = 0.00;     // 分区中的最大值
                // 获取最大值
                for(int i=doubleArr.length-1;i>=0;){    // 只执行一次,取最后一个值
                    maxValue = doubleArr[i];
                    break;
                }
                maxValueIndex = list.indexOf(maxValue+"");     // 获取最大值的下标(即最大值点位)

                // 填充光纤测温实体类
                fibreTemperaturePojo.setDeviceIp(fibreTemperature.getDeviceIp());
                fibreTemperaturePojo.setChannel(fibreTemperature.getChannel());
                fibreTemperaturePojo.setPartitionId(config.getPartitionId());
                fibreTemperaturePojo.setCreateTime(fibreTemperature.getCreateTime());
                fibreTemperaturePojo.setStep(fibreTemperature.getStep());
                fibreTemperaturePojo.setDatas(subDatas);
                fibreTemperaturePojo.setMaxValue(maxValue + config.getOffsetValue());   // 加上偏移量(调整最大值)
                fibreTemperaturePojo.setMaxValuePoints(maxValueIndex);
                fibreTemperaturePojo.setOffsetValue(config.getOffsetValue());
                // 插入数据
                result += fibreTemperatureDao.insertData(fibreTemperaturePojo);
            }
        }
        return result;

    }

    // 将字符串数组转换成double数组
    private double[] toDoubleArray(String[] strArr) {
        // 定义一个int数组
        double[] arr=new double[strArr.length];
        // 对字符串数组进行遍历
        for (int i = 0; i < arr.length; i++) {
            // 将数组格式的字符串转成双精度数，存储到arr数组中
            arr[i]=Double.parseDouble(strArr[i]);
        }
        return arr;
    }
    private void arraySort(double[] doubleArr){
        Arrays.sort(doubleArr);
    }
}
