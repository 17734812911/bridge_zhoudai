package com.xtw.bridge.controller;

import com.xtw.bridge.model.FibreTemperature;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.FibreTemperatureService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * User: Mr.Chen
 * Date: 2021/7/19
 * Description: No Description
 */
@RestController
@RequestMapping("/device")
public class FibreTemperatureController {
    @Resource
    FibreTemperatureService fibreTemperatureService;

    @PostMapping("/fibretemperatures")
    public ResponseFormat insertFibreTemperatureDatas(@RequestBody Map<String,String> map){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FibreTemperature fibreTemperature = new FibreTemperature();

        if(!map.isEmpty()){
            try {
                String deviceIp = map.get("DeviceIp");
                String channel = map.get("Channel");
                Date createTime = simpleDateFormat.parse((String)map.get("CreateAt"));
                String step = map.get("Step");
                String datas = map.get("Datas");
                fibreTemperature.setDeviceIp(deviceIp);
                fibreTemperature.setChannel(channel);
                fibreTemperature.setCreateTime(createTime);
                fibreTemperature.setStep(step);
                fibreTemperature.setDatas(datas);
                int result = fibreTemperatureService.insertData(fibreTemperature);
                if(result >0){
                    return ResponseFormat.success("数据插入成功");
                } else{
                    return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "数据插入异常"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "数据不能为空"));
    }

    // @PostMapping("/test")
    // public ResponseFormat testMethod(){
    //     try{
    //         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //         String deviceIp = "192.168.100.3";
    //         String channel = "1";
    //         Date createTime = simpleDateFormat.parse("2021-07-21 15:45:44");
    //         String step = "0.5";
    //         String datas = "";
    //         for(int i=1;i<=18000;i++){
    //             if(i==2451 || i==7612 || i==12695){
    //                 datas += i+10000 + ".0,";
    //             }else{
    //                 if(i != 18000){
    //                     datas += i + ".0,";
    //                 }else{
    //                     datas += i + ".0";
    //                 }
    //             }
    //
    //         }
    //         FibreTemperature fibreTemperature = new FibreTemperature();
    //         fibreTemperature.setDeviceIp(deviceIp);
    //         fibreTemperature.setChannel(channel);
    //         fibreTemperature.setCreateTime(createTime);
    //         fibreTemperature.setStep(step);
    //         fibreTemperature.setDatas(datas);
    //         int result = fibreTemperatureService.insertData(fibreTemperature);
    //         if(result >0){
    //             return ResponseFormat.success("数据插入成功");
    //         } else{
    //             return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "数据插入异常"));
    //         }
    //     } catch (Exception e){
    //         e.printStackTrace();
    //     }
    //     return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "数据不能为空"));
    // }

}
