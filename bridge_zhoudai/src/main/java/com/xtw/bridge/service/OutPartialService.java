package com.xtw.bridge.service;

import com.xtw.bridge.mapper.OutPartialDao;
import com.xtw.bridge.model.OutPartial;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/28
 * Description: No Description
 */
@Service
public class OutPartialService {
    @Resource
    OutPartialDao outPartialDao;

    public List<OutPartial> queryOutPartialMaxValue(){
        return outPartialDao.queryOutPartialMaxValue();
    }
}
