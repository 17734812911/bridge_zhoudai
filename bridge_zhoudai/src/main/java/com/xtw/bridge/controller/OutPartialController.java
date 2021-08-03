package com.xtw.bridge.controller;

import com.xtw.bridge.model.OutPartial;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.OutPartialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
}
