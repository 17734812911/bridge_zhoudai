package com.xtw.bridge.controller;

import com.xtw.bridge.model.OutPartial;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.OutPartialService;
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
@RequestMapping("/outpartial")
public class OutPartialController {
    @Resource
    OutPartialService outPartialService;

    @GetMapping("/maxvalues")
    public ResponseFormat queryOutPartialMaxValue(){
        List<OutPartial> outPartialList = outPartialService.queryOutPartialMaxValue();
        if(outPartialList != null){
            return ResponseFormat.success("查询成功",outPartialList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }
}
