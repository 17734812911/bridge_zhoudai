package com.xtw.bridge.controller;

import com.xtw.bridge.model.FibreTemperature;
import com.xtw.bridge.model.page.PageRequest;
import com.xtw.bridge.model.page.PageResult;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.FibreTemperatureService;
import com.xtw.bridge.service.FibreTemperatureServiceImpl;
import com.xtw.bridge.utils.MyUtils;
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
    @Resource
    FibreTemperatureServiceImpl fibreTemperatureServiceImpl;


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
                    // 向device表更新光纤测温最新数据时间
                    fibreTemperatureService.updateDataTime();
                    return ResponseFormat.success("数据保存成功");
                } else{
                    return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "数据保存失败"));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "数据不能为空"));
    }


    // 按分区查询光纤测温三相数据
    @GetMapping("/gxcwdatas")
    @Operation(
            summary = "根据分区ID查询光纤测温数据",
            parameters = {
                    @Parameter(name = "partitionId", description = "分区ID")
            }
    )
    public ResponseFormat queryAllDatasById(Integer partitionId){
        List<FibreTemperature> fibreTemperatureList = fibreTemperatureServiceImpl.queryDatasById(partitionId);
        if(fibreTemperatureList != null){
            return ResponseFormat.success("查询成功", fibreTemperatureList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }


    // 按分区、时间查询光纤测温三相数据(分页)
    @GetMapping("/gxcwdataspage")
    @Operation(
            summary = "按分区、时间查询光纤测温三相数据(分页)",
            parameters = {
                    @Parameter(name = "partitionId", description = "分区ID"),
                    @Parameter(name = "pageNum", description = "当前页码"),
                    @Parameter(name = "pageSize", description = "每页数量"),
                    @Parameter(name = "begintime", description = "开始时间"),
                    @Parameter(name = "endtime", description = "结束时间")
            }
    )
    public ResponseFormat queryAllDatasPage(Integer partitionId, Integer pageNum, Integer pageSize, String begintime, String endtime) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer pagenum = 1;    // 默认页码
        Integer pagesize = 3 * 100;   // 默认每页数量
        Date beginTime = sdf.parse(MyUtils.getDateTime(-1));
        Date endTime = sdf.parse(MyUtils.getDateTime(0));

        if(!"".equals(begintime)){
            beginTime = sdf.parse(begintime);
        }
        if(!"".equals(endtime)){
            endTime = sdf.parse(endtime);
        }
        if(pageNum != null && pageSize != null){
            pagenum = pageNum;
            pagesize = 3 * pageSize;
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(pagenum);
        pageRequest.setPageSize(pagesize);

        PageResult fibreTemperatureList = fibreTemperatureServiceImpl.queryDatasPage(pageRequest, partitionId, beginTime, endTime);
        if(fibreTemperatureList != null){
            return ResponseFormat.success("查询成功", fibreTemperatureList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }


    // 查询光纤测温所有数据(所有分区三相最大值)
    @GetMapping("/gxcwallmaxvalues")
    @Operation(
            summary = "查询光纤测温所有数据(所有分区三相最大值)"
    )
    public ResponseFormat queryAllPartitionMaxValue(){
        HashMap<String, Object> map = fibreTemperatureServiceImpl.parseData();
        if(! map.isEmpty()){
            return ResponseFormat.success("查询成功", map);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }

    // 查询所有分区光纤测温三相中的最大值及所在点位
    @GetMapping("/gxcwmaxvalues")
    @Operation(
            summary = "查询所有分区光纤测温三相中的最大值及所在点位"
    )
    public ResponseFormat queryAllMaxAndPoint(){
        ArrayList<LinkedHashMap<String,Object>> arrayList = fibreTemperatureServiceImpl.queryAllMaxAndPoint();
        if(!arrayList.isEmpty()){
            return ResponseFormat.success("查询成功", arrayList);
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
    public ResponseFormat queryHistoricalDatasByCondition(String begintime, String endtime, Integer partitionid, Integer point) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginTime = null;
        Date endTime = null;
        if(!"".equals(begintime)){
            beginTime = sdf.parse(begintime);
        }
        if(!"".equals(endtime)){
            endTime = sdf.parse(endtime);
        }

        List<LinkedHashMap<String, Object>> resultList = fibreTemperatureServiceImpl.queryHistoricalDatasByCondition(beginTime, endTime, partitionid, point);
        if(resultList != null){
            return ResponseFormat.success("查询成功", resultList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询异常"));
        }
    }

}
