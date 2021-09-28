package com.xtw.bridge.controller;

import com.xtw.bridge.model.Device;
import com.xtw.bridge.model.Line;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

    @GetMapping("/alldevices")
    @Operation(
            summary = "查询所有设备(不含摄像头)"
    )
    public ResponseFormat queryAllDevices(){
        List<LinkedHashMap<String,String>> deviceList = deviceService.queryAllDevice();
        if(deviceList != null){
            return ResponseFormat.success("查询成功",deviceList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
        }
    }

    @GetMapping("/devices")
    @Operation(
            summary = "查询所有设备"
    )
    public ResponseFormat queryAllDevice(){
        List<Device> deviceList = deviceService.queryAllDevices();
        if(deviceList != null){
            return ResponseFormat.success("查询成功",deviceList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
        }
    }

    // 查询在线设备
    @GetMapping("/onlinedevice")
    @Operation(
            summary = "查询在线设备"
    )
    public ResponseFormat queryOnlineDevice(){
        LinkedHashMap<String, Integer> deviceMap = deviceService.queryOnlineDevice();
        if(deviceMap != null){
            return ResponseFormat.success("查询成功",deviceMap);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
        }
    }

    // 查询所有线路
    @GetMapping("/lines")
    @Operation(
            summary = "查询所有线路"
    )
    public ResponseFormat queryAllLine(){
        List<Line> lineList = deviceService.queryAllLine();
        if(lineList != null){
            return ResponseFormat.success("查询成功", lineList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
        }
    }


    // 按分类查询所有设备
    @GetMapping("/category")
    @Operation(
            summary = "按分类查询所有设备"
    )
    public ResponseFormat queryDeviceByCategory(){
        List<Object> deviceList = deviceService.queryDeviceByCategory();
        if(deviceList != null){
            return ResponseFormat.success("查询成功", deviceList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
        }
    }


    // 获取所有类型设备七天最大值
    @GetMapping("/maxvalues")
    @Operation(
            summary = "获取所有类型设备七天最大值"
    )
    public ResponseFormat queryMaxValue(){
        LinkedList<Object> list= deviceService.queryMaxValue();
        if(list.size() > 0){
            return ResponseFormat.success("查询成功", list);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "查询失败"));
        }
    }


    // 查询分区+设备名称
    @GetMapping("/queryname")
    @Operation(
            summary = "查询分区+设备名称",
            parameters = {
                    @Parameter(name = "partitionId", description = "分区ID"),
                    @Parameter(name = "terminalId", description = "设备ID")
            }
    )
    public ResponseFormat queryName(String partitionId, String terminalId){
        String result = deviceService.queryName(partitionId, terminalId);
        return ResponseFormat.success("查询成功", result);
    }

    // 网站无故障运行天数
    @GetMapping("/safe")
    @Operation(
            summary = "网站无故障运行天数"
    )
    public ResponseFormat querySafeNumber(){
        String result = deviceService.safeDuration();
        return ResponseFormat.success("查询成功", result);
    }

}
