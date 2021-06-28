package com.xtw.bridge.controller;

import com.xtw.bridge.model.AlertDevice;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.AlertDeviceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
}
