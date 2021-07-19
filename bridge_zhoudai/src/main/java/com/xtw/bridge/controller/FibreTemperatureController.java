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

    @PostMapping("/fibreTemperatures")
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
}
