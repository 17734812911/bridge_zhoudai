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
import java.util.*;

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


    // 按分区查询光纤测温数据
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

    @GetMapping("/gxcwallmaxvalues")
    @Operation(
            summary = "查询光纤测温所有数据(所有分区三相最大值)"
    )
    public ResponseFormat queryAllPartitionMaxValue(){
        HashMap<String, Object> map = fibreTemperatureService.parseData();
        if(! map.isEmpty()){
            return ResponseFormat.success("查询成功", map);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }

    @GetMapping("/queryhistoricaldatas")
    @Operation(
            summary = "按条件查询点位的历史数据",
            parameters = {
                    @Parameter(name = "begintime", description = "开始时间"),
                    @Parameter(name = "endtime", description = "结束时间"),
                    @Parameter(name = "partitionid", description = "分区ID"),
                    @Parameter(name = "point", description = "下标点位")
            }
    )
    public ResponseFormat queryHistoricalDatasByCondition(@RequestBody Map<String,String> map) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginTime = null;
        Date endTime = null;
        int partitionId = Integer.parseInt(map.get("partitionid"));
        int point = Integer.parseInt(map.get("point"));
        if(map.containsKey("begintime") && map.get("begintime") != "" & map.containsKey("endtime") && map.get("endtime") != ""){
            beginTime = sdf.parse(map.get("begintime"));
            endTime = sdf.parse(map.get("endtime"));
        }
        List<LinkedHashMap> resultList = fibreTemperatureService.queryHistoricalDatasByCondition(beginTime, endTime, partitionId, point);
        if(resultList != null){
            return ResponseFormat.success("查询成功", resultList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询异常"));
        }
    }

}
