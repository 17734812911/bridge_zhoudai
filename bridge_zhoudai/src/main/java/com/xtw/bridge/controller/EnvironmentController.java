package com.xtw.bridge.controller;

import com.xtw.bridge.model.EnvironmentDevice;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.EnvironmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/30
 * Description: No Description
 */
@RestController
@RequestMapping("/device")
public class EnvironmentController {
    @Resource
    EnvironmentService environmentService;

    // 根据分区ID查询环境量数据
    @GetMapping("/environmentdevice")
    public ResponseFormat queryAllDataByPartitionId(int partitionId){
        List<EnvironmentDevice> environmentDeviceList = environmentService.queryAllDataByPartitionId(partitionId);
        if(environmentDeviceList != null){
            return ResponseFormat.success("查询成功",environmentDeviceList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }

}
