package com.xtw.bridge.controller;

import com.xtw.bridge.model.AlertDevice;
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
    public ResponseFormat queryAllAlertDevice(){
        List<AlertDevice> alertDevices = alertDeviceService.queryAllAlertDevice();
        return ResponseFormat.success("查询成功", alertDevices);
    }

    // 条件查询告警设备
    @PostMapping("/devices/criterias")
    @Operation(
            summary = "按条件查询告警设备",
            parameters = {
                    @Parameter(name = "map", description = "请求条件的map集合,集合中Key为linename,devicename,joint,begintime,endtime")
            }
    )
    public List<AlertDevice> queryAlertDeviceByCriteria(@RequestBody Map<String,String> map) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String lineName = map.get("linename");
        String deviceName = map.get("devicename");
        String joint = map.get("joint");
        Date beginTime = map.containsKey("begintime") ? sdf.parse(map.get("begintime")) : null;
        Date endTime = map.containsKey("endtime") ? sdf.parse(map.get("endtime")) : null;

        return alertDeviceService.queryAlertDeviceByCriteria(lineName, deviceName, joint, beginTime, endTime);
    }
}
