package com.xtw.bridge.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtw.bridge.model.FibreTemperature;
import com.xtw.bridge.model.page.PageRequest;
import com.xtw.bridge.model.page.PageResult;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.FibreTemperatureService;
import com.xtw.bridge.service.FibreTemperatureServiceImpl;
import com.xtw.bridge.utils.MyUtils;
import com.xtw.bridge.utils.PageUtils;
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
//                Date createTime = simpleDateFormat.parse((String)map.get("CreateAt"));
                String step = map.get("Step");
                String datas = map.get("Datas");
                if("1".equals(channel) || "3".equals(channel) || "5".equals(channel)){
                   datas = reverseString(datas.split(","));
                }
                fibreTemperature.setDeviceIp(deviceIp);
                fibreTemperature.setChannel(channel);
                fibreTemperature.setCreateTime(map.get("CreateAt"));
                fibreTemperature.setStep(step);
                fibreTemperature.setDatas(datas);


                int result = fibreTemperatureServiceImpl.insertData(fibreTemperature);
                if(result >0){
                    // 向device表更新光纤测温最新数据时间
                    fibreTemperatureServiceImpl.updateDataTime();
                    return ResponseFormat.success("数据保存成功");
                } else{
                    return ResponseFormat.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "数据保存失败"));
                }
            } catch (Exception e) {
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
        boolean flag = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer pagenum = 1;    // 默认页码
        Integer pagesize = 3;   // 默认每页数量
        String beginTime = MyUtils.getDateTime(-1);
        String endTime = MyUtils.getDateTime(0);

        if("".equals(begintime)){
            flag = false;
        }
        if(!"".equals(begintime) & null != begintime){
            beginTime = begintime;
        }
        if(!"".equals(endtime) & null != endtime){
            endTime = endtime;
        }

        if(null != pageNum){
            pagenum = pageNum;
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(pagenum);
        pageRequest.setPageSize(pagesize);

        List<FibreTemperature> fibreTemperatureList = null;
        PageResult pageResult = new PageResult();

        if(flag){
            fibreTemperatureList = fibreTemperatureServiceImpl.queryDatasPage(partitionId, beginTime, endTime);

            List<FibreTemperature> list1 = new LinkedList<>();

            for(int i=((pagenum-1)*pagesize);i<(pagenum*pagesize);i++){
                list1.add(fibreTemperatureList.get(i));
            }
            pageResult.setPageNum(pagenum);
            pageResult.setPageSize(pagesize);
            pageResult.setTotalSize(fibreTemperatureList.size());
            pageResult.setTotalPages((fibreTemperatureList.size()/pagesize));
            pageResult.setContent(list1);
        }else{
            fibreTemperatureList = fibreTemperatureServiceImpl.queryDatasPageDesc(partitionId, beginTime, endTime);
            List<FibreTemperature> list1 = new LinkedList<>();

            for(int i=((pagenum-1)*pagesize);i<(pagenum*pagesize);i++){
                list1.add(fibreTemperatureList.get(i));
            }
            pageResult.setPageNum(pagenum);
            pageResult.setPageSize(pagesize);
            pageResult.setTotalSize(3);
            pageResult.setTotalPages(1);
            pageResult.setContent(list1);
        }

        if(fibreTemperatureList != null){
            return ResponseFormat.success("查询成功", pageResult);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }


    // 查询光纤测温所有数据(所有分区三相最大值)
    @GetMapping("/gxcwallmaxvalues")
    @Operation(
            summary = "查询光纤测温所有数据(所有分区三相最大值)",
            parameters = {
                    @Parameter(name = "begintime", description = "开始时间"),
                    @Parameter(name = "endtime", description = "结束时间")
            }

    )
    public ResponseFormat queryAllPartitionMaxValue(String begintime, String endtime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginTime = MyUtils.getDateTime(-1);
        String endTime = MyUtils.getDateTime(0);

        if(!"".equals(begintime) & null != begintime){
            beginTime = begintime;
        }
        if(!"".equals(endtime) & null != endtime){
            endTime = endtime;
        }

        HashMap<String, Object> map = fibreTemperatureServiceImpl.parseData(beginTime, endTime);

        return ResponseFormat.success("查询成功", map);
    }



    // 查询所有分区光纤测温三相中的最大值及所在点位
    @GetMapping("/gxcwmaxvalues")
    @Operation(
            summary = "查询所有分区光纤测温三相中的最大值及所在点位"
    )
    public ResponseFormat queryAllMaxAndPoint(){
        ArrayList<LinkedHashMap<String,Object>> arrayList = fibreTemperatureServiceImpl.queryAllMaxAndPoint();
        return ResponseFormat.success("查询成功", arrayList);
//        if(!arrayList.isEmpty()){
//            return ResponseFormat.success("查询成功", arrayList);
//        } else{
//            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
//        }
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
        List<LinkedHashMap<String, Object>> resultList = fibreTemperatureServiceImpl.queryHistoricalDatasByCondition(begintime, endtime, partitionid, point);

        if(resultList != null){
            return ResponseFormat.success("查询成功", resultList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询异常"));
        }
    }

    // 反转数组
    private String reverseString(String[] strArr){
        for(int i=0;i<strArr.length / 2-1;i++){
            String temp1 = strArr[i];
            String temp2 = strArr[strArr.length-i-1];
            strArr[i] = temp2;
            strArr[strArr.length-i-1] = temp1;
        }
        return Arrays.asList(strArr).toString();
    }

}
