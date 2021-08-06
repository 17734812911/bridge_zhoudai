package com.xtw.bridge.controller;

import com.xtw.bridge.model.OutPartial;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.ConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/5
 * Description: No Description
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    ConfigService configService;

    // 查询外置局放所有配置
    @GetMapping("/outpartialconfigs")
    public ResponseFormat queryOutPartialConfig(){
        List<HashMap> configList = configService.queryOutPartialConfig();

        if(! configList.isEmpty()){
            return ResponseFormat.success("查询成功", configList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }

    }

    @GetMapping("/fibretemperatureconfigs")
    public ResponseFormat queryFibreTemperatureConfig(){
        List<HashMap> configList = configService.queryOutPartialConfig();

        if(! configList.isEmpty()){
            return ResponseFormat.success("查询成功", configList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }

    }
}
