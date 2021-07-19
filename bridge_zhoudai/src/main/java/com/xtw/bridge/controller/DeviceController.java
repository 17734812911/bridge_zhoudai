package com.xtw.bridge.controller;

import com.xtw.bridge.model.Device;
import com.xtw.bridge.model.Line;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
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
        if(deviceList != null){
            return ResponseFormat.success("查询成功",deviceList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
        }
    }

    // 查询在线设备
    @GetMapping("/onlineDevice")
    public ResponseFormat queryOnlineDevice(){
        List<Line> deviceList = deviceService.queryOnlineDevice();
        if(deviceList != null){
            return ResponseFormat.success("查询成功",deviceList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
        }
    }

    // 查询所有类型设备近7天最大值
    @GetMapping("/maxvalues")
    public ResponseFormat queryDeviceMaxValue(){
        List<Device> maxValueList = deviceService.queryDeviceMaxValue();
        if(maxValueList != null){
            return ResponseFormat.success("查询成功", maxValueList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
        }
    }

    //查询所有线路
    @GetMapping("/lines")
    public ResponseFormat queryAllLine(){
        List<Line> lineList = deviceService.queryAllLine();
        if(lineList != null){
            return ResponseFormat.success("查询成功", lineList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
        }
    }
}
