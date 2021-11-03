package com.xtw.bridge.service;

import com.xtw.bridge.mapper.FibreTemperatureDao;
import com.xtw.bridge.model.*;
import com.xtw.bridge.utils.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Mr.Chen
 * Date: 2021/7/19
 * Description: No Description
 */
@Slf4j
@Service
public class FibreTemperatureServiceImpl implements FibreTemperatureService {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    // 保存光纤测温数据
    @Override
    public int insertData(FibreTemperature fibreTemperature) {
        int result = 0;
        try{
            int maxValueIndex = 0;
            FibreTemperature fibreTemperaturePojo = new FibreTemperature();
            String[] strArray = fibreTemperature.getDatas().split(",");     // 单条通道的所有数据
            List<String> strList = Arrays.asList(strArray);     // 将数组转换为集合，用于截取数据
            // 先根据配备IP和通道ID,查询光纤测温配置
            fibreTemperatureConfigs = queryFibreTemperatureConfig(fibreTemperature.getDeviceIp(), fibreTemperature.getChannel());

            // 根据配置处理数据
            for(FibreTemperatureConfig config : fibreTemperatureConfigs){
                if(config.getDeviceIp().equals(fibreTemperature.getDeviceIp()) && config.getChannel().equals(fibreTemperature.getChannel())){
                    int start = config.getStartPosition();  // 起始位置
                    int end = config.getEndPosition();      // 结束位置
                    Integer partitionId = config.getPartitionId();
                    List<String> list = strList.subList(start, end+1);       // 根据起止点位截取的集合

                    String subDatas = StringUtils.join(list,",");   // 将截取的集合转换为字符串

                    String[] subListArr = new String[list.size()];
                    list.toArray(subListArr);   // 将截取的list转换为数组

                    double[] doubleArr = MyUtils.toDoubleArray(subListArr);     // 将字符串数组转换成double数组

                    // 排序
                    MyUtils.arraySort(doubleArr);
                    Double maxValue = null;     // 分区中的最大值
                    // 获取最大值
                    for(int i=doubleArr.length-1;i>=0;){    // 只执行一次,取最后一个值
                        maxValue = doubleArr[i];

//                        maxValueIndex = list.indexOf(df.format(maxValue) + "");     // 获取最大值的下标(即最大值点位)
                        maxValueIndex = list.indexOf(String.valueOf(maxValue));     // 获取最大值的下标(即最大值点位)

                        if(maxValue >= 150){  // 断纤告警
                            FibreTemperatureAlert fibreTemperatureAlert = new FibreTemperatureAlert();
                            if(partitionId == 0){
                                fibreTemperatureAlert.setContent("马目侧光纤测温（马目侧至岱山侧方向） " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处断纤,值为：" + maxValue);

                            } else if(partitionId == 25){
                                fibreTemperatureAlert.setContent("岱山侧光纤测温（马目侧至岱山侧方向） " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处断纤,值为：" + maxValue);
                            } else{
                                fibreTemperatureAlert.setContent(partitionId + "号接头光纤测温(马目侧至岱山侧方向) " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处断纤,值为：" + maxValue);
                            }
                            fibreTemperatureAlert.setAlertData(String.valueOf(maxValue + config.getOffsetValue()));
                            fibreTemperatureAlert.setAlertDate(fibreTemperature.getCreateTime());
                            fibreTemperatureAlert.setChannel(config.getChannel());  // 将光纤测温的通道号设置为告警表中的线路ID
                            fibreTemperatureAlert.setPartitionId(config.getPartitionId()+"");   // 将光纤测温的分区号设置为告警表中的设备
                            fibreTemperatureAlert.setAlertType(String.valueOf(3));
                            fibreTemperatureAlert.setTerminalId("22201001");

                            insertAlertData(fibreTemperatureAlert); // 插入报警数据

                        } else if(maxValue >= config.getCriticalAlarm()){  // 严重告警
                            FibreTemperatureAlert fibreTemperatureAlert = new FibreTemperatureAlert();
                            if(partitionId == 0){
                                fibreTemperatureAlert.setContent("马目侧光纤测温（马目侧至岱山侧方向） " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处严重报警,值为：" + maxValue);
                            } else if(partitionId == 25){
                                fibreTemperatureAlert.setContent("岱山侧光纤测温（马目侧至岱山侧方向） " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处严重报警,值为：" + maxValue);
                            } else{
                                fibreTemperatureAlert.setContent(partitionId + "号接头光纤测温(马目侧至岱山侧方向) " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处严重报警,值为：" + maxValue);
                            }
                            fibreTemperatureAlert.setAlertData(String.valueOf(maxValue + config.getOffsetValue()));
                            fibreTemperatureAlert.setAlertDate(fibreTemperature.getCreateTime());
                            fibreTemperatureAlert.setChannel(config.getChannel());  // 将光纤测温的通道号设置为告警表中的线路ID
                            fibreTemperatureAlert.setPartitionId(config.getPartitionId()+"");   // 将光纤测温的分区号设置为告警表中的设备
                            fibreTemperatureAlert.setAlertType(String.valueOf(3));
                            fibreTemperatureAlert.setTerminalId("22201001");

                            insertAlertData(fibreTemperatureAlert); // 插入报警数据

                        } else if(maxValue >= config.getAlarmValue()){     // 如果最大值达到预设报警值,向报警表插入信息
                            FibreTemperatureAlert fibreTemperatureAlert = new FibreTemperatureAlert();
                            if(partitionId == 0){
                                fibreTemperatureAlert.setContent("马目侧光纤测温（马目侧至岱山侧方向） " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处报警,值为：" + maxValue);
                            } else if(partitionId == 25){
                                fibreTemperatureAlert.setContent("岱山侧光纤测温（马目侧至岱山侧方向） " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处报警,值为：" + maxValue);
                            } else{
                                fibreTemperatureAlert.setContent(partitionId + "号接头光纤测温(马目侧至岱山侧方向) " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处报警,值为：" + maxValue);
                            }

                            fibreTemperatureAlert.setAlertData(String.valueOf(maxValue + config.getOffsetValue()));
                            fibreTemperatureAlert.setAlertDate(fibreTemperature.getCreateTime());
                            fibreTemperatureAlert.setChannel(config.getChannel());  // 将光纤测温的通道号设置为告警表中的线路ID
                            fibreTemperatureAlert.setPartitionId(config.getPartitionId()+"");   // 将光纤测温的分区号设置为告警表中的设备
                            fibreTemperatureAlert.setAlertType(String.valueOf(2));
                            fibreTemperatureAlert.setTerminalId("22201001");

                            insertAlertData(fibreTemperatureAlert); // 插入报警数据

                        } else if(maxValue >= config.getWarningValue()){    // 预警
                            FibreTemperatureAlert fibreTemperatureAlert = new FibreTemperatureAlert();
                            if(partitionId == 0){
                                fibreTemperatureAlert.setContent("马目侧光纤测温（马目侧至岱山侧方向） " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处预警,值为：" + maxValue);
                            } else if(partitionId == 25){
                                fibreTemperatureAlert.setContent("岱山侧光纤测温（马目侧至岱山侧方向） " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处预警,值为：" + maxValue);
                            } else{
                                fibreTemperatureAlert.setContent(partitionId + "号接头光纤测温(马目侧至岱山侧方向) " + (maxValueIndex * Double.parseDouble(fibreTemperature.getStep())) + "米处预警,值为：" + maxValue);
                            }
                            fibreTemperatureAlert.setAlertData(String.valueOf(maxValue + config.getOffsetValue()));
                            fibreTemperatureAlert.setAlertDate(fibreTemperature.getCreateTime());
                            fibreTemperatureAlert.setChannel(config.getChannel());  // 将光纤测温的通道号设置为告警表中的线路ID
                            fibreTemperatureAlert.setPartitionId(config.getPartitionId()+"");   // 将光纤测温的分区号设置为告警表中的设备
                            fibreTemperatureAlert.setAlertType(String.valueOf(1));
                            fibreTemperatureAlert.setTerminalId("22201001");

                            insertAlertData(fibreTemperatureAlert); // 插入报警数据
                        }
                        break;
                    }

                    // 填充光纤测温实体类
                    fibreTemperaturePojo.setDeviceIp(fibreTemperature.getDeviceIp());
                    fibreTemperaturePojo.setChannel(fibreTemperature.getChannel());
                    fibreTemperaturePojo.setPartitionId(config.getPartitionId());
                    fibreTemperaturePojo.setCreateTime(fibreTemperature.getCreateTime());
                    fibreTemperaturePojo.setStep(fibreTemperature.getStep());
                    fibreTemperaturePojo.setDatas(subDatas);
                    if (maxValue != null) {
                        fibreTemperaturePojo.setMaxValue(maxValue + config.getOffsetValue());   // 加上偏移量(调整最大值)
                    }
                    fibreTemperaturePojo.setMaxValuePoints(maxValueIndex);
                    fibreTemperaturePojo.setOffsetValue(config.getOffsetValue());
                    // 插入数据
                    result += fibreTemperatureDao.insertData(fibreTemperaturePojo);

                    if(result>0){
//                        System.out.println("数据保存成功");
                    } else{
                        System.out.println("数据保存失败");
                    }
                }
            }

        } catch(Exception e){
            e.printStackTrace();
//            System.out.println("数据处理异常");
        }
        return result;
    }

    // 向device表更新光纤测温最新数据时间
    public int updateDataTime() {
        int result = 0;
        try{
            result = fibreTemperatureDao.updateDataTime(sdf.format(new Date()));
        } catch (Exception e){
            System.out.println("字符串转时间格式失败");
        }
        return result;
    }

    // 向报警表插入数据
    @Override
    public int insertAlertData(FibreTemperatureAlert fibreTemperatureAlert) {
        return fibreTemperatureDao.insertAlertData(fibreTemperatureAlert);
    }

    // 根据分区ID查询光纤测温三相数据
    @Override
    public List<FibreTemperature> queryDatasById(Integer partitionId) {
        List list = new ArrayList();
        List<FibreTemperature> fibreTemperatureList = fibreTemperatureDao.queryDatasById(partitionId);
        // 获取分区起止点位
        HashMap<String,Integer> hashMap = fibreTemperatureDao.queryStartEndPoint(partitionId);
        Integer startPoint = hashMap.get("start_position");
        Integer endPoint = hashMap.get("end_position");

        for (FibreTemperature  fibreTemperature: fibreTemperatureList) {
            ArrayList<Object> arrayList = new ArrayList<>();
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            String[] fibreTemperatureDatas =  fibreTemperature.getDatas().split(",");
            double[] doubleArray = MyUtils.toDoubleArray(fibreTemperatureDatas);
            linkedHashMap.put("id", fibreTemperature.getId());
            linkedHashMap.put("deviceIp", fibreTemperature.getDeviceIp());
            linkedHashMap.put("channel", fibreTemperature.getChannel());
            linkedHashMap.put("partitionId", fibreTemperature.getPartitionId());
            linkedHashMap.put("createTime", fibreTemperature.getCreateTime());
            linkedHashMap.put("step", fibreTemperature.getStep());
            linkedHashMap.put("datas", doubleArray);
            linkedHashMap.put("maxValue", fibreTemperature.getMaxValue());
            linkedHashMap.put("maxValuePoints", fibreTemperature.getMaxValuePoints());
            for(int i=startPoint;i<endPoint;i++){
                arrayList.add(Double.parseDouble(fibreTemperature.getStep()) * i);
//                double a = Double.parseDouble(fibreTemperature.getStep()) * i / 1000.00;    // 换算成公里
//                if(a <= 1){
//                    arrayList.add(a);
//                }else{
//                    arrayList.add(String.format("%.1f", a));
//                }
            }
            linkedHashMap.put("index", arrayList);
            list.add(linkedHashMap);
        }
        return list;
    }


    // 按分区ID、时间查询光纤测温数据并分页
    @Override
    public List<FibreTemperature> queryDatasPage( Integer partitionId, String beginTime, String endTime, Integer limitStart, Integer LimitLength) {
        List list = new ArrayList();

        List<FibreTemperature> fibreTemperatureList = fibreTemperatureDao.queryDatasPage( partitionId, beginTime, endTime, limitStart, LimitLength);

        // 获取分区起止点位
        HashMap<String,Integer> hashMap = fibreTemperatureDao.queryStartEndPoint(partitionId);
        Integer startPoint = hashMap.get("start_position");
        Integer endPoint = hashMap.get("end_position");

        for (FibreTemperature  fibreTemperature: fibreTemperatureList) {
            ArrayList<Object> arrayList = new ArrayList<>();
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            String[] fibreTemperatureDatas =  fibreTemperature.getDatas().split(",");
            if(fibreTemperatureDatas.length==0){
                return null;
            }
            double[] doubleArray = MyUtils.toDoubleArray(fibreTemperatureDatas);
            linkedHashMap.put("id", fibreTemperature.getId());
            linkedHashMap.put("deviceIp", fibreTemperature.getDeviceIp());
            linkedHashMap.put("channel", fibreTemperature.getChannel());
            linkedHashMap.put("partitionId", fibreTemperature.getPartitionId());
            linkedHashMap.put("createTime", fibreTemperature.getCreateTime());    // fibreTemperature.getCreateTime()   sdf.format(new Date())
            linkedHashMap.put("step", fibreTemperature.getStep());
            linkedHashMap.put("datas", doubleArray);
            linkedHashMap.put("maxValue", fibreTemperature.getMaxValue());
            linkedHashMap.put("maxValuePoints", fibreTemperature.getMaxValuePoints());
            for(int i=startPoint;i<endPoint;i++){
                arrayList.add(Double.parseDouble(fibreTemperature.getStep()) * i);
//                double a = Double.parseDouble(fibreTemperature.getStep()) * i / 1000.00;    // 换算成公里
//                if(a <= 1){
//                    arrayList.add(a);
//                }else{
//                    arrayList.add(String.format("%.1f", a));
//                }
            }
            linkedHashMap.put("index", arrayList);

            list.add(linkedHashMap);
        }
        return list;
    }

    // 查询符合条件的数据的总数
    @Override
    public Integer queryDataCount(Integer partitionId, String beginTime, String endTime) {
        return fibreTemperatureDao.queryDataCount(partitionId, beginTime, endTime);
    }

    public List<FibreTemperature> queryDatasPageDesc( Integer partitionId, String beginTime, String endTime, Integer limitStart, Integer LimitLength) {
        List list = new ArrayList();

        List<FibreTemperature> fibreTemperatureList = fibreTemperatureDao.queryDatasPageDesc( partitionId, beginTime, endTime, limitStart, LimitLength);

        // 获取分区起止点位
        HashMap<String,Integer> hashMap = fibreTemperatureDao.queryStartEndPoint(partitionId);
        Integer startPoint = hashMap.get("start_position");
        Integer endPoint = hashMap.get("end_position");

        for (FibreTemperature  fibreTemperature: fibreTemperatureList) {
            ArrayList<Object> arrayList = new ArrayList<>();
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            String[] fibreTemperatureDatas =  fibreTemperature.getDatas().split(",");
            if(fibreTemperatureDatas.length==0){
                return null;
            }
            double[] doubleArray = MyUtils.toDoubleArray(fibreTemperatureDatas);
            linkedHashMap.put("id", fibreTemperature.getId());
            linkedHashMap.put("deviceIp", fibreTemperature.getDeviceIp());
            linkedHashMap.put("channel", fibreTemperature.getChannel());
            linkedHashMap.put("partitionId", fibreTemperature.getPartitionId());
            linkedHashMap.put("createTime", sdf.format(new Date()));  //   fibreTemperature.getCreateTime()
            linkedHashMap.put("step", fibreTemperature.getStep());
            linkedHashMap.put("datas", doubleArray);
            linkedHashMap.put("maxValue", fibreTemperature.getMaxValue());
            linkedHashMap.put("maxValuePoints", fibreTemperature.getMaxValuePoints());
            for(int i=startPoint;i<endPoint;i++){
                arrayList.add(Double.parseDouble(fibreTemperature.getStep()) * i);
//                double a = Double.parseDouble(fibreTemperature.getStep()) * i / 1000.00;    // 换算成公里
//                if(a <= 1){
//                    arrayList.add(a);
//                }else{
//                    arrayList.add(String.format("%.1f", a));
//                }
            }
            linkedHashMap.put("index", arrayList);

            list.add(linkedHashMap);
        }
        return list;
    }

    @Override
    public List<FibreTemperature> queryAllPartitionMaxValue(String begintime, String endtime) {
        // 获取所有分区三相的最新值
        return fibreTemperatureDao.queryAllPartitionMaxValue(begintime, endtime);
    }

    // 解析数据
    @Override
    public LinkedHashMap<String, Object> parseData(String begintime, String endtime){
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = "";
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

        // 按时间段获取所有分区三相的值
        List<FibreTemperature> fibreTemperatureList = queryAllPartitionMaxValue(begintime, endtime);
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

        if(readOrder){      // 默认读取是[1-->2][3-->4][5-->6]
            // 合并数组,按参数传递顺序合并
            aPhaseList = MyUtils.mergeArray(onePhaseList,twoPhaseList);
            bPhaseList = MyUtils.mergeArray(threePhaseList,fourPhaseList);
            cPhaseList = MyUtils.mergeArray(fivePhaseList,sixPhaseList);
        } else{     // 否则[2-->1][4-->3][6-->5]
            // 合并数组,按参数传递顺序合并
            aPhaseList = MyUtils.mergeArray(twoPhaseList, onePhaseList);
            bPhaseList = MyUtils.mergeArray(fourPhaseList, threePhaseList);
            cPhaseList = MyUtils.mergeArray(sixPhaseList, fivePhaseList);
        }
        date = sdf.format(new Date());
        map.put("aPhase", aPhaseList);
        map.put("bPhase", bPhaseList);
        map.put("cPhase", cPhaseList);
        map.put("datetime", date);

        return map;
    }

    // 查询光纤测温通道读取顺序
    @Override
    public boolean queryReadOrder() {
        return fibreTemperatureDao.queryReadOrder();
    }


    // 查询所有分区光纤测温三相中的最大值及所在点位
    public ArrayList<LinkedHashMap<String,Object>> queryAllMaxAndPoint(){
        ArrayList<LinkedHashMap<String,Object>> arrayList = new ArrayList<>();

        List<FibreTemperature> fibreTemperatureList = fibreTemperatureDao.queryAllMaxAndPoint();
        for(int i=0;i<fibreTemperatureList.size();i++){
            FibreTemperature fibreTemperature = fibreTemperatureList.get(i);
            LinkedHashMap<String,Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("partitionId", fibreTemperature.getPartitionId());
            linkedHashMap.put("maxValue", fibreTemperature.getMaxValue());
            linkedHashMap.put("maxValuePoint", fibreTemperature.getMaxValuePoints());

            arrayList.add(linkedHashMap);
        }
        return arrayList;
    }

    // 根据分区id和数据点位查询该点历史数据
    @Override
    public List<LinkedHashMap<String, Object>> queryHistoricalDatasByCondition(String beginTime, String endTime, Integer partitionId, int point){
        String pointValue = "";     // point点位的数据
        List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
        List<FibreTemperature> fibreTemperatureList = queryHistoricalDatas(beginTime, endTime, partitionId);

        for(FibreTemperature fibreTemperature : fibreTemperatureList){
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("channelId", fibreTemperature.getChannel());
            linkedHashMap.put("createTime", fibreTemperature.getCreateTime());
            pointValue = MyUtils.getPointValue(fibreTemperature.getDatas(), point);

            linkedHashMap.put("value", pointValue);   // 获取fibreTemperature中下标为point的数据
            resultList.add(linkedHashMap);
        }
        return resultList;
    }
    @Override
    public List<FibreTemperature> queryHistoricalDatas(String beginTime, String endTime, Integer partitionId) {
        return fibreTemperatureDao.queryHistoricalDatas(beginTime, endTime, partitionId);
    }


    public static Date StringToDate(String datetime){
        SimpleDateFormat sdFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdFormat.parse(datetime);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }


}
