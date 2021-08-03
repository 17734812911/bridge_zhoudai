package com.xtw.bridge.controller;

import com.xtw.bridge.model.AlertDevice;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.AlertDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            summary = "查询报警设备"
    )
    public ResponseFormat queryAllAlertDevice(){
        List<AlertDevice> alertDevices = alertDeviceService.queryAllAlertDevice();
        if(alertDevices != null){
            return ResponseFormat.success("查询成功", alertDevices);
        }else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询异常"));
        }
    }

    // 条件查询告警设备
    @PostMapping("/devices/criterias")
    @Operation(
            summary = "按条件查询告警设备",
            parameters = {
                    @Parameter(name = "map", description = "请求条件的map集合,集合中Key为linename,devicename,joint,begintime,endtime")
            }
    )
    public ResponseFormat queryAlertDeviceByCriteria(@RequestBody Map<String,String> map) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginTime = null;
        Date endTime = null;
        String lineName = map.get("linename");
        String deviceName = map.get("devicename");
        String joint = map.get("joint");
        if(map.containsKey("begintime") && map.get("begintime") != "" & map.containsKey("endtime") && map.get("endtime") != ""){
            beginTime = sdf.parse(map.get("begintime"));
            endTime = sdf.parse(map.get("endtime"));
        }
        List<AlertDevice> alertDeviceList = alertDeviceService.queryAlertDeviceByCriteria(lineName, deviceName, joint, beginTime, endTime);
        if(alertDeviceList != null){
            return ResponseFormat.success("查询成功", alertDeviceList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询异常"));
        }

    }
}
