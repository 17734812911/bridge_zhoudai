package com.xtw.bridge.controller;

import com.xtw.bridge.model.FibreTemperature;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.FibreTemperatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: Mr.Chen
 * Date: 2021/7/19
 * Description: No Description
 */
@Slf4j
@RestController
@RequestMapping("/device")
public class FibreTemperatureController {

    @Resource
    FibreTemperatureService fibreTemperatureService;

    // 接收光纤测温参数并保存
    @PostMapping("/fibretemperatures")
    @Operation(
            summary = "接收光纤测温数据并保存",
            parameters = {
                    @Parameter(name = "map", description = "光纤测温设备采集到的数据")
            }
    )
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


    // 查询光纤测温数据
    @GetMapping("/gxcwdatas")
    @Operation(
            summary = "根据分区ID查询光纤测温数据",
            parameters = {
                    @Parameter(name = "partitionId", description = "分区ID")
            }
    )
    public ResponseFormat queryAllDatasById(int partitionId){
        List<FibreTemperature> fibreTemperatureList = fibreTemperatureService.queryDatasById(partitionId);
        if(fibreTemperatureList != null){
            return ResponseFormat.success("查询成功", fibreTemperatureList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }

}
