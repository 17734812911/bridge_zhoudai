package com.xtw.bridge.service;

import com.xtw.bridge.mapper.DeviceDao;
import com.xtw.bridge.model.Device;
import com.xtw.bridge.model.DeviceDO;
import com.xtw.bridge.model.Line;
import com.xtw.bridge.model.MaxValue;
import com.xtw.bridge.utils.MyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: No Description
 */
@Service
public class DeviceService {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    DeviceDao deviceDao;

    // 查询所偶遇设备（不含摄像头）
    public List<LinkedHashMap<String,String>> queryAllDevice(){
        List<LinkedHashMap<String,String>> devices = deviceDao.queryAllDevice();
        for(int i=0;i<devices.size();i++){
            String lastDateTime = devices.get(i).get("last_data_time");

            if(belongCalendar(lastDateTime)){
                devices.get(i).put("isonLine", "true");
            }else{
                devices.get(i).put("isonLine", "false");
            }
        }
        return devices;
    }

    // 查询所所有设备
    public List<Device> queryAllDevices(){
        return deviceDao.queryAllDevices();
    }



    // 判断给定时间是否在最近24小时之内
    private boolean belongCalendar(String datetime){
        String beginTime = MyUtils.getDateTime(1, -1, 0);
        String endTime = MyUtils.getDateTime(1, 0, 0);

        try {
            Calendar date = Calendar.getInstance();
            date.setTime(sdf.parse(datetime));

            Calendar after = Calendar.getInstance();
            after.setTime(sdf.parse(beginTime));

            Calendar before = Calendar.getInstance();
            before.setTime(sdf.parse(endTime));

            return date.after(after) && date.before(before);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    // 查询设备在线情况
    public LinkedHashMap<String, Integer> queryOnlineDevice(){
        LinkedHashMap<String, Integer> linkHashMap = new LinkedHashMap<>();
        // 查询所有种类设备对应的数量
        List<DeviceDO> allTypeNumberList = deviceDao.queryAllTypeNumber();
        // 查询不在线设备种类对应的数量
        List<DeviceDO> deviceList = deviceDao.queryOutlineDevice();

        // 解析设备在线/不在线情况
        for(int i=0;i<allTypeNumberList.size();i++){
            for(int j=0;j<deviceList.size();j++){
                // 获取对应设备
                if(allTypeNumberList.get(i).getName().equals(deviceList.get(j).getName())){
                    // 在线数量
                    int onLineNumber = allTypeNumberList.get(i).getNumber() - deviceList.get(j).getNumber();
                    int outLine = deviceList.get(j).getNumber();
                    // 光纤测温做特殊处理
                    if("光纤测温".equals(allTypeNumberList.get(i).getName())){
                        if(deviceList.get(j).getNumber() == 26){    // 等于通道数26
                            onLineNumber = 1;   // 光纤测温在线数量1台
                            outLine = 0;
                        }else{
                            onLineNumber = 0;
                            outLine = 1;
                        }
                    }

                    switch(allTypeNumberList.get(i).getName()){
                        case "高频局放":
                            linkHashMap.put("WZJFOnLine", onLineNumber);
                            linkHashMap.put("WZJFOffLine", outLine);
                            break;
                        case "环境量":
                            linkHashMap.put("HJLOnLine", onLineNumber);
                            linkHashMap.put("HJLOffLine", outLine);
                            break;
                        case "光纤测温":
                            linkHashMap.put("GXCWOnLine", 1);//onLineNumber
                            linkHashMap.put("GXCWOffLine", outLine);
                            break;
                        // case "摄像头":
                        //     linkHashMap.put("SXTOnLine", onLineNumber);
                        //     linkHashMap.put("SXTOffLine", outLine);
                        //     break;
                        case "表皮测温":
                            linkHashMap.put("BPCWOnLine", onLineNumber);
                            linkHashMap.put("BPCWOffLine", outLine);
                    }
                }
            }
        }
        return linkHashMap;
    }

    // 查询所有线路
    public List<Line> queryAllLine(){
        return deviceDao.queryAllLine();
    }

    // 按分类查询设备
    public LinkedList<Object> queryDeviceByCategory(){
        LinkedList<Object> linkedList = new LinkedList<>();
        ArrayList<Object> HJList = new ArrayList<>();   // 存放环境量数据
        ArrayList<Object> BPCWList = new ArrayList<>();  // 摄像头
        ArrayList<Object> JFList = new ArrayList<>();   // 外置局放
        ArrayList<Object> GXList = new ArrayList<>();   // 光纤测温
        ArrayList<Object> SXTList = new ArrayList<>();  // 摄像头

        List<LinkedHashMap<String,String>> deviceList = deviceDao.queryDeviceByCategory();

        for(int i=0;i<deviceList.size();i++){
            HashMap<String,Object> map = new HashMap<>();
            switch (deviceList.get(i).get("productname")){
                case "摄像头":
                    map.put("productName", deviceList.get(i).get("productname"));
                    map.put("deviceName", deviceList.get(i).get("name"));
                    map.put("terminalId", deviceList.get(i).get("terminal_id"));

                    map.put("partitionId", deviceList.get(i).get("line_id"));  // 分区id比line_id小1
                    SXTList.add(map);
                    break;
                case "环境量":
                    map.put("productName", deviceList.get(i).get("productname"));
                    map.put("deviceName", deviceList.get(i).get("name"));
                    map.put("terminalId", deviceList.get(i).get("terminal_id"));
                    map.put("partitionId", deviceList.get(i).get("line_id"));
                    HJList.add(map);
                    break;
                case "高频局放":
                    map.put("productName", deviceList.get(i).get("productname"));
                    map.put("deviceName", deviceList.get(i).get("name"));
                    map.put("terminalId", deviceList.get(i).get("terminal_id"));
                    map.put("partitionId", deviceList.get(i).get("line_id"));
                    JFList.add(map);
                    break;
                case "光纤测温":
                    map.put("productName", deviceList.get(i).get("productname"));
                    map.put("deviceName", deviceList.get(i).get("name"));
                    map.put("terminalId", deviceList.get(i).get("terminal_id"));
                    map.put("partitionId", deviceList.get(i).get("line_id"));
                    GXList.add(map);
                    break;
                case "表皮测温":
                    map.put("productName", deviceList.get(i).get("productname"));
                    map.put("deviceName", deviceList.get(i).get("name"));
                    map.put("terminalId", deviceList.get(i).get("terminal_id"));
                    map.put("partitionId", deviceList.get(i).get("line_id"));
                    BPCWList.add(map);
            }
        }
        for(int i=0;i<5;i++){
            if(i==0){
                HashMap<String, Object> map = new HashMap<>();
                map.put("productName", "摄像头");
                map.put("data", SXTList);
                linkedList.add(map);
            } else if(i==1){
                HashMap<String, Object> map = new HashMap<>();
                map.put("productName", "环境量");
                map.put("data", HJList);
                linkedList.add(map);
            } else if(i==2){
                HashMap<String, Object> map = new HashMap<>();
                map.put("productName", "高频局放");
                map.put("data", JFList);
                linkedList.add(map);
            } else if(i==3){
                HashMap<String, Object> map = new HashMap<>();
                map.put("productName", "光纤测温");
                map.put("data", GXList);
                linkedList.add(map);
            } else{
                HashMap<String, Object> map = new HashMap<>();
                map.put("productName", "表皮测温");
                map.put("data", BPCWList);
                linkedList.add(map);
            }

        }
        return linkedList;
    }

    // 获取所有类型设备七天最大值
    public LinkedList<Object> queryMaxValue(){
        LinkedList<Object> linkedList = new LinkedList<>();
        MaxValue maxValue = deviceDao.queryMaxValue();
        for(int i=0;i<9;i++){
            HashMap<String,String> map = new HashMap<>();
            if(i==0){
                map.put("name", "湿度(%)");
                map.put("value", maxValue.getSd());
                linkedList.add(map);
            }else if(i==1){
                map.put("name", "温度(°C)");
                map.put("value", maxValue.getWd());
                linkedList.add(map);
            }else if(i==2){
                map.put("name", "氧气(%vol)");
                map.put("value", maxValue.getYq());
                linkedList.add(map);
            }else if(i==3){
                map.put("name", "一氧化碳(ppm)");
                map.put("value", maxValue.getEyht());
                linkedList.add(map);
            }else if(i==4){
                map.put("name", "硫化氢(ppm)");
                map.put("value", maxValue.getLhq());
                linkedList.add(map);
            }else if(i==5){
                map.put("name", "甲烷(%lel)");
                map.put("value", maxValue.getJw());
                linkedList.add(map);
            }else if(i==6){
                map.put("name", "表皮温度(°C)");
                map.put("value", maxValue.getBpwd());
                linkedList.add(map);
            }else if(i==7){
                map.put("name", "高频局放(pC)");
                map.put("value", maxValue.getWzjf());
                linkedList.add(map);
            }else{
                map.put("name", "光纤测温(°C)");
                map.put("value", maxValue.getGxcw());
                linkedList.add(map);
            }
        }
        return linkedList;
    }

    // 根据设备id和分区id查询线路名+设备名
    public String queryName(String partitionId, String terminalId){
        LinkedHashMap<String,String> list = deviceDao.queryName(partitionId,terminalId);
        String lineName = list.get("line_name");
        String name = list.get("name");
        return lineName + name;
    }

    // 查询当前无故障运行天数
    public String safeDuration(){
        return deviceDao.safeDuration();
    }

    // 计算更新无故障天数
    public int calculation(int number){     // number是当前无故障天数
        int number2 = number;
        // 查询今天的告警数量
        int num = deviceDao.alertNumber();
        if(num == 0){
            number2 = number2 + 1;  // 如果今天没告警，就加一天
        }
        // 更新安全运行天数表
        int result = deviceDao.updateSafe(number2);
        return result;
    }
}
