package com.xtw.bridge.controller;

import com.xtw.bridge.model.AlertDO;
import com.xtw.bridge.model.AlertDevice;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.AlertDeviceService;
import com.xtw.bridge.utils.MyUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Mr.Chen
 * Date: 2021/6/28
 * Description: No Description
 */
@RestController
@RequestMapping("/alert")
public class DeviceAlertController {

    @Resource
    AlertDeviceService alertDeviceService;

    // 查询报警设备
    @GetMapping("/devices")
    @Operation(
            summary = "查询报警设备信息及报警信息"
    )
    public ResponseFormat queryAllAlertDevice(){
        List<AlertDevice> alertDevices = alertDeviceService.queryAllAlertDevice();
        if(alertDevices != null){
            return ResponseFormat.success("查询成功", alertDevices);
        }else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询异常"));
        }
    }


    // 查询七天中每天的告警次数
    @Operation(
            summary = "查询七天中每天的告警次数"
    )
    @GetMapping("/alarmnumber")
    public ResponseFormat queryEveryDayAlarmNumber(){
        List<AlertDO> linkedHashMap = alertDeviceService.queryEveryDayAlarmNumber();
        return ResponseFormat.success("查询成功", linkedHashMap);
    }

    // 条件查询告警设备
    @PostMapping("/devices/criterias")
    @Operation(
            summary = "按条件查询告警设备",
            parameters = {
                    @Parameter(name = "id", description = "id"),
                    @Parameter(name = "linename", description = "线路名称"),
                    @Parameter(name = "devicename", description = "设备名称"),
                    @Parameter(name = "begintime", description = "开始时间"),
                    @Parameter(name = "endtime", description = "结束时间"),
                    @Parameter(name = "type", description = "告警类型"),
                    @Parameter(name = "isenter", description = "是否确认")
            }
    )
    public ResponseFormat queryAlertDeviceByCriteria(@RequestBody Map<String,String> map) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer id = 0;
        String beginTime = null;
        String endTime = null;
        if(map.get("id") != null){
            id = Integer.parseInt(map.get("id"));
        }
        String lineName = map.get("linename");
        String deviceName = map.get("devicename");
        String alertType = map.get("type");
        String isEnter = map.get("isenter");
        if(map.containsKey("begintime") && map.get("begintime") != "" & map.containsKey("endtime") && map.get("endtime") != ""){
            beginTime = map.get("begintime");
            endTime = map.get("endtime");
        }
        List<AlertDevice> alertDeviceList = alertDeviceService.queryAlertDeviceByCriteria(id,lineName, deviceName, beginTime, endTime, alertType, isEnter);
        if(alertDeviceList != null){
            return ResponseFormat.success("查询成功", alertDeviceList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询异常"));
        }

    }

    // 报警确认
    @PostMapping("/confirm")
    @Operation(
            summary = "报警确认",
            parameters = {
                    @Parameter(name = "id", description = "id")
            }
    )
    public ResponseFormat alarmEnter(@RequestBody Map<String,String> map){
        String id = map.get("id");
        int result = alertDeviceService.alarmEnter(id);
        if (result > 0){
            return ResponseFormat.success("确认成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "确认失败"));
        }
    }

    // 统计指定时长（天）的报警次数
    @GetMapping("/alertcount")
    @Operation(
            summary = "统计指定时长（天）的报警次数"
    )
    public ResponseFormat todayAlertCount(Integer time){
        List<HashMap<String,String>> hashMapList = alertDeviceService.alertCount(time);
        return ResponseFormat.success("查询成功", hashMapList);
    }

    // 获取所有报警设备所属分区(用于页面闪烁)
    @GetMapping("/partitions")
    @Operation(
            summary = "获取所有报警设备所属分区"
    )
    public ResponseFormat alertPartition(){
        ArrayList<Integer> arrayList = alertDeviceService.alertPartition();
        return ResponseFormat.success("查询成功", arrayList);
    }

    // 条件获取告警列表
    @PostMapping("/alertlist")
    @Operation(
            summary = "按条件查询告警设备",
            parameters = {
                    @Parameter(name = "begintime", description = "开始时间"),
                    @Parameter(name = "endtime", description = "结束时间"),
                    @Parameter(name = "devicename", description = "设备名称"),
                    @Parameter(name = "type", description = "告警类型")
            }
    )
    public ResponseFormat getTodayAlarm(@RequestBody Map<String,String> map){
        String beginTime = MyUtils.getDateTime(1, -1, 0);
        String endTime = MyUtils.getDateTime(1, 0, 0);
        String alertType = null;
        String deviceName = null;
        String isConfirm = null;

        if(map.containsKey("begintime")){
            beginTime = map.get("begintime");
        }
        if(map.containsKey("endtime")){
            endTime = map.get("endtime");
        }
        if(map.containsKey("type")){
            alertType = map.get("type");
        }
        if(map.containsKey("devicename")){
            deviceName = map.get("devicename");
        }
        if(map.containsKey("isconfirm")){
            isConfirm = map.get("isconfirm");
        }

        List<AlertDevice> alertDeviceList = alertDeviceService.getTodayAlarm(beginTime, endTime, alertType, deviceName, isConfirm);
        return  ResponseFormat.success("查询成功", alertDeviceList);

    }
}
