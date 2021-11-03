package com.xtw.bridge.controller;

import com.xtw.bridge.model.page.PageRequest;
import com.xtw.bridge.model.page.PageResult;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.EnvironmentService;
import com.xtw.bridge.service.EnvironmentServiceImpl;
import com.xtw.bridge.utils.MyUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

/**
 * User: Mr.Chen
 * Date: 2021/7/30
 * Description: No Description
 */
@RestController
@RequestMapping("/device")
public class EnvironmentController {

    @Resource
    EnvironmentServiceImpl environmentServiceImpl;

    // 根据分区ID查询环境量数据
    @GetMapping("/environments")
    @Operation(
            summary = "根据分区ID查询环境量数据",
            parameters = {
                    @Parameter(name = "partitionId", description = "分区ID")
            }
    )
    public ResponseFormat queryAllDataByPartitionId(int partitionId){
        LinkedList<Object> environmentDeviceList = environmentServiceImpl.queryAllDataByPartitionId(partitionId);
        if(environmentDeviceList != null){
            return ResponseFormat.success("查询成功",environmentDeviceList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }


    // 根据环境量和表皮测温设备id查询该设备最新数据
    @GetMapping("/environment")
    @Operation(
            summary = "根据环境量设备id查询该设备最新数据",
            parameters = {
                    @Parameter(name = "terminalId", description = "设备ID")
            }
    )
    public ResponseFormat getEnvironmentData(String terminalId){
        LinkedList<Object> environmentData = environmentServiceImpl.queryDatasByTerminalId(terminalId);
        if(environmentData != null){
            return ResponseFormat.success("查询成功",environmentData);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }

    // 根据环境量和表皮测温设备id查询该设备所有数据
    @GetMapping("/environmentpage")
    @Operation(
            summary = "根据环境量和表皮测温设备id分页查询该设备所有数据",
            parameters = {
                    @Parameter(name = "terminalId", description = "设备ID"),
                    @Parameter(name = "pageNum", description = "当前页码"),
                    @Parameter(name = "pageSize", description = "每页数量"),
                    @Parameter(name = "begintime", description = "开始时间"),
                    @Parameter(name = "endtime", description = "结束时间")
            }
    )
    public ResponseFormat queryEnvironmentDatasPage(String terminalId, Integer pageNum, Integer pageSize, String begintime, String endtime) throws ParseException  {//Integer pageNum, Integer pageSize, String begintime, String endtime
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer pagenum = 1;    // 默认页码
        Integer pagesize = 5;   // 默认每页数量
        String beginTimeStr = begintime;
        String endTimeStr = endtime;
        if("".equals(begintime)){
            Date beginTime = sdf.parse(MyUtils.getDateTime(1, -1, 0));
            beginTimeStr = sdf.format(beginTime);
            System.out.println(beginTimeStr);
        }
        if("".equals(endtime)){
            Date endTime = sdf.parse(MyUtils.getDateTime(1, 0, 0));
            endTimeStr = sdf.format(endTime);
            System.out.println(endTimeStr);
        }


        if(pageNum != null && pageSize != null){
            pagenum = pageNum;
            pagesize = pageSize;
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(pagenum);
        pageRequest.setPageSize(pagesize);

        PageResult environmentList = environmentServiceImpl.queryEnvironmentDatasPage(pageRequest, terminalId, beginTimeStr, endTimeStr);
        if(environmentList != null){
            return ResponseFormat.success("查询成功", environmentList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }


    // 查询环境量某个通道的历史趋势
    @PostMapping("/channeltrend")
    @Operation(
            summary = "查询环境量某个通道的历史趋势",
            parameters = {
                    @Parameter(name = "terminalId", description = "设备ID"),
                    @Parameter(name = "channelId", description = "通道ID"),
                    @Parameter(name = "begintime", description = "开始时间"),
                    @Parameter(name = "endtime", description = "结束时间")

            }
    )
    public ResponseFormat getChannelTrend(@RequestBody Map<String,String> map){
        String terminalId = map.get("terminalId");
        String channelId = map.get("channelId");
        String beginTime = MyUtils.getDateTime(1, -1, 0);
        String endTime = MyUtils.getDateTime(1, 0, 0);

        if(!"".equals(map.get("begintime")) & null != map.get("begintime")){
            beginTime = map.get("begintime");
        }
        if(!"".equals(map.get("endtime")) & null != map.get("endtime")){
            endTime = map.get("endtime");
        }

        ArrayList<Object> arrayList = environmentServiceImpl.queryChannelDatas(terminalId, channelId, beginTime,endTime);
        return ResponseFormat.success("查询成功", arrayList);
    }

}
