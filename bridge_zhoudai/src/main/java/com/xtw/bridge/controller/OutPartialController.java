package com.xtw.bridge.controller;

import com.xtw.bridge.model.OutPartial;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.OutPartialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Mr.Chen
 * Date: 2021/7/28
 * Description: No Description
 */
@RestController
@RequestMapping("/device")
public class OutPartialController {
    @Resource
    OutPartialService outPartialService;

    // 根据分区ID查询该分区所有外置局放设备的数据
    @GetMapping("/outpartialvalues")
    @Operation(
            summary = "根据分区ID查询该分区所有外置局放设备的数据",
            parameters = {
                    @Parameter(name = "partitionId", description = "分区ID")
            }
    )
    public ResponseFormat queryOutPartialMaxValue(int partitionId){
        List<OutPartial> outPartialList = outPartialService.queryOutPartialMaxValue(partitionId);
        if(outPartialList != null){
            return ResponseFormat.success("查询成功",outPartialList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }

    // 根据分区ID和设备ID查询该分区的外置局放设备的历史趋势(最大放电量、对应时间)
    @GetMapping("/outpartialtrend")
    @Operation(
            summary = "查询外置局放设备的历史趋势",
            parameters = {
                    @Parameter(name = "partitionId", description = "分区ID"),
                    @Parameter(name = "terminalId", description = "外置局放设备ID")
            }
    )
    public ResponseFormat queryOutPartitionTrend(int partitionId, String terminalId) {
        List<HashMap<String, String>> trendList = outPartialService.queryOutPartitionTrend(partitionId, terminalId);
        if(trendList != null){
            return ResponseFormat.success("查询成功",trendList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }


    // 根据分区ID和设备ID查询该分区的外置局放设备的历史分析(最大放电量、最大放电频次、对应时间)
    @GetMapping("/historicalanalysis")
    @Operation(
            summary = "外置局放设备的历史分析",
            parameters = {
                    @Parameter(name = "partitionId", description = "分区ID"),
                    @Parameter(name = "terminalId", description = "外置局放设备ID")
            }
    )
    public ResponseFormat queryHistoricalAnalysis(int partitionId, String terminalId) {
        List<HashMap<String, String>> trendList = outPartialService.historicalAnalysis(partitionId, terminalId);
        if(trendList != null){
            return ResponseFormat.success("查询成功",trendList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }


    // 根据外置局放设备id查询该设备最新数据
    @GetMapping("/outpartial")
    @Operation(
            summary = "根据外置局放设备id查询该设备最新数据",
            parameters = {
                    @Parameter(name = "terminalId", description = "外置局放设备ID")
            }
    )
    public ResponseFormat getOutPartitionDataByTerminalId(String terminalId){
        List<OutPartial> outPartialData = outPartialService.queryOutPartialData(terminalId);
        if(outPartialData != null){
            return ResponseFormat.success("查询成功",outPartialData);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }
}
