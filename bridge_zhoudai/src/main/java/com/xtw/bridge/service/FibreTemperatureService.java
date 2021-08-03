package com.xtw.bridge.service;

import com.xtw.bridge.mapper.FibreTemperatureDao;
import com.xtw.bridge.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

/**
 * User: Mr.Chen
 * Date: 2021/7/19
 * Description: No Description
 */
@Slf4j
@Service
public class FibreTemperatureService implements FibreTemperatureDao {
    private static boolean readOrder;   // 光纤测温通道读取顺序
    private static DecimalFormat df = new DecimalFormat("#0.00");   // 用于将double类型的值保留两位小数
    private static int num = 0;
    private ArrayList<FibreTemperature> fibreTemperatureArrayList = new ArrayList<>();


    @Resource
    FibreTemperatureDao fibreTemperatureDao;
    // 光纤测温配置
    public static List<FibreTemperatureConfig> fibreTemperatureConfigs = new ArrayList<FibreTemperatureConfig>();

    @Override
    public List<FibreTemperatureConfig> queryFibreTemperatureConfig(String deviceIp, String channel) {
        return fibreTemperatureDao.queryFibreTemperatureConfig(deviceIp, channel);
    }

    @Override
    public int insertData(FibreTemperature fibreTemperature) {
        System.out.println(fibreTemperature.toString());
        int result = 0;
        try{
            int maxValueIndex = 0;
            FibreTemperature fibreTemperaturePojo = new FibreTemperature();
            String[] strArray = fibreTemperature.getDatas().split(",");
            List<String> strList = Arrays.asList(strArray);     // 将数组转换为集合，用于截取数据
            // 先根据配备IP和通道ID,查询光纤测温配置
            fibreTemperatureConfigs = queryFibreTemperatureConfig(fibreTemperature.getDeviceIp(), fibreTemperature.getChannel());

            // 根据配置处理数据
            for(FibreTemperatureConfig config : fibreTemperatureConfigs){
                if(config.getDeviceIp().equals(fibreTemperature.getDeviceIp()) && config.getChannel().equals(fibreTemperature.getChannel())){
                    int start = config.getStartPosition();  // 起始位置
                    // System.out.println("开始：" + start);
                    int end = config.getEndPosition();      // 结束位置
                    // System.out.println("结束：" + end);
                    List<String> list = strList.subList(start, end);       // 根据起止点位截取的集合
                    String subDatas = StringUtils.join(list,",");   // 根据起止点位截取的字符串
                    // System.out.println("list长度：" + list.size());
                    String[] subListArr = new String[list.size()];
                    list.toArray(subListArr);   // 将截取的list转换为数组

                    // 排序
                    double[] doubleArr = toDoubleArray(subListArr);
                    arraySort(doubleArr);
                    double maxValue = 0.00;     // 分区中的最大值
                    // 获取最大值
                    for(int i=doubleArr.length-1;i>=0;){    // 只执行一次,取最后一个值
                        maxValue = doubleArr[i];
                        // System.out.println("最大值：" + maxValue);
                        if(maxValue >= config.getAlarmValue()){     // 如果最大值达到预设报警值,向报警表插入信息
                            FibreTemperatureAlert fibreTemperatureAlert = new FibreTemperatureAlert();
                            fibreTemperatureAlert.setContent("光纤测温告警");
                            fibreTemperatureAlert.setAlertData((maxValue + config.getOffsetValue())+"");
                            fibreTemperatureAlert.setAlertDate(fibreTemperature.getCreateTime());
                            fibreTemperatureAlert.setChannel(config.getChannel());  // 将光纤测温的通道号设置为告警表中的线路ID
                            fibreTemperatureAlert.setPartitionId(config.getPartitionId()+"");   // 将光纤测温的分区号设置为告警表中的设备

                            insertAlertData(fibreTemperatureAlert); // 插入报警数据
                        }
                        break;
                    }

                    maxValueIndex = list.indexOf(df.format(maxValue) + "");     // 获取最大值的下标(即最大值点位)

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

                    if(result>0){
                        System.out.println("数据保存成功");
                    } else{
                        System.out.println("数据保存失败");
                    }
                }
            }
            // log.info("result:" + result);
        } catch(Exception e){
            // log.info("数据处理异常");
            System.out.println("数据处理异常");
        }
        return result;
    }

    @Override
    public int insertAlertData(FibreTemperatureAlert fibreTemperatureAlert) {
        return fibreTemperatureDao.insertAlertData(fibreTemperatureAlert);
    }

    // 根据分区ID查询光纤测温三相数据
    @Override
    public List<FibreTemperature> queryDatasById(int partitionId) {
        List list = new ArrayList();
        List<FibreTemperature> fibreTemperatureList = fibreTemperatureDao.queryDatasById(partitionId);
        for (FibreTemperature  fibreTemperature: fibreTemperatureList) {
            HashMap<String, Object> hashMap = new HashMap<>();
            String[] fibreTemperatureDatas =  fibreTemperature.getDatas().split(",");
            double[] doubleArray = toDoubleArray(fibreTemperatureDatas);
            hashMap.put("id", fibreTemperature.getId());
            hashMap.put("deviceIp", fibreTemperature.getDeviceIp());
            hashMap.put("channel", fibreTemperature.getChannel());
            hashMap.put("partitionId", fibreTemperature.getPartitionId());
            hashMap.put("createTime", fibreTemperature.getCreateTime());
            hashMap.put("step", fibreTemperature.getStep());
            hashMap.put("datas", doubleArray);
            hashMap.put("maxValue", fibreTemperature.getMaxValue());
            hashMap.put("maxValuePoints", fibreTemperature.getMaxValuePoints());
            list.add(hashMap);
        }
        return list;
    }

    @Override
    public List<FibreTemperature> queryAllPartitionMaxValue() {
        // 获取所有分区三相的最新值
        List<FibreTemperature> fibreTemperatureList =  fibreTemperatureDao.queryAllPartitionMaxValue();
        return fibreTemperatureList;
    }

    // 解析数据
    public HashMap<String, Object> parseData(){
        HashMap<String,Object> map = new HashMap<>();
        ArrayList<String> aPhaseList = new ArrayList<>();   // A相最大值数组
        ArrayList<String> bPhaseList = new ArrayList<>();   // B相最大值数组
        ArrayList<String> cPhaseList = new ArrayList<>();   // C相最大值数组
        List<String> onePhaseList = new ArrayList<>();
        List<String> twoPhaseList = new ArrayList<>();
        List<String> threePhaseList = new ArrayList<>();
        List<String> fourPhaseList = new ArrayList<>();
        List<String> fivePhaseList = new ArrayList<>();
        List<String> sixPhaseList = new ArrayList<>();
        if(num == 0){   // 如果没有读取顺序
            readOrder = queryReadOrder();
            num ++;
        }

        // 获取所有分区三相的最新值
        List<FibreTemperature> fibreTemperatureList = queryAllPartitionMaxValue();

        for(int i=0;i<fibreTemperatureList.size();i++){
            String channelId = fibreTemperatureList.get(i).getChannel();
            switch(channelId){
                case "1":
                    onePhaseList.add(String.valueOf(fibreTemperatureList.get(i).getMaxValue()));
                    break;
                case "2":
                    twoPhaseList.add(String.valueOf(fibreTemperatureList.get(i).getMaxValue()));
                    break;
                case "3":
                    threePhaseList.add(String.valueOf(fibreTemperatureList.get(i).getMaxValue()));
                    break;
                case "4":
                    fourPhaseList.add(String.valueOf(fibreTemperatureList.get(i).getMaxValue()));
                    break;
                case "5":
                    fivePhaseList.add(String.valueOf(fibreTemperatureList.get(i).getMaxValue()));
                    break;
                case "6":
                    sixPhaseList.add(String.valueOf(fibreTemperatureList.get(i).getMaxValue()));
                    break;
            }
        }

        if(readOrder){      // 默认读取是[1-->4][2-->5][3-->6]
            // 合并数组,按参数传递顺序合并
            aPhaseList = mergeArray(onePhaseList,fourPhaseList);
            bPhaseList = mergeArray(twoPhaseList,fivePhaseList);
            cPhaseList = mergeArray(threePhaseList,sixPhaseList);
        } else{     // 否则[4-->1][5-->2][6-->3]
            // 合并数组,按参数传递顺序合并
            aPhaseList = mergeArray(fourPhaseList, onePhaseList);
            bPhaseList = mergeArray(fivePhaseList, twoPhaseList);
            cPhaseList = mergeArray(sixPhaseList, threePhaseList);
        }
        map.put("aPhase", aPhaseList);
        map.put("bPhase", bPhaseList);
        map.put("cPhase", bPhaseList);

        return map;
    }

    // 查询光纤测温通道读取顺序
    @Override
    public boolean queryReadOrder() {
        return fibreTemperatureDao.queryReadOrder();
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
    // 数组排序
    private void arraySort(double[] doubleArr){
        Arrays.sort(doubleArr);
    }
    // 合并数组
    private ArrayList<String> mergeArray(List<String> firstArr, List<String> secondArr){
        ArrayList<String> mergeArr = new ArrayList<>();
        mergeArr.addAll(firstArr);
        mergeArr.addAll(secondArr);
        return mergeArr;
    }

}
