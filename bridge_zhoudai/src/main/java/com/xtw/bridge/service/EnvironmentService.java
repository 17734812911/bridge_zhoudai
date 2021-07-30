package com.xtw.bridge.service;

import com.xtw.bridge.mapper.EnvironmentDao;
import com.xtw.bridge.model.EnvironmentDevice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/7/30
 * Description: No Description
 */
@Service
public class EnvironmentService {

    @Resource
    EnvironmentDao environmentDao;

    // 根据分区ID查询环境量数据
    public List<EnvironmentDevice> queryAllDataByPartitionId(int partitionId){
        return environmentDao.queryAllDataByPartitionId(partitionId);
    }
}
