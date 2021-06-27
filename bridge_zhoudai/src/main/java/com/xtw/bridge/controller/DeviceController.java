package com.xtw.bridge.controller;

import com.xtw.bridge.model.Device;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.DeviceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/6/25
 * Description: No Description
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    @Resource
    DeviceService deviceService;

    @GetMapping("/devices")
    public ResponseFormat queryAllDevice(){
        List<Device> deviceList = deviceService.queryAllDevice();
        return ResponseFormat.success("查询成功",deviceList);
    }
}
