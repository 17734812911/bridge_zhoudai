package com.xtw.bridge.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xtw.bridge.mapper.EnvironmentDao;
import com.xtw.bridge.model.EnvironmentDO;
import com.xtw.bridge.model.EnvironmentDevice;
import com.xtw.bridge.model.FibreTemperature;
import com.xtw.bridge.model.page.PageRequest;
import com.xtw.bridge.model.page.PageResult;
import com.xtw.bridge.utils.MyUtils;
import com.xtw.bridge.utils.PageUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * User: Mr.Chen
 * Date: 2021/7/30
 * Description: No Description
 */
@Service
public class EnvironmentServiceImpl implements EnvironmentService {

    @Resource
    EnvironmentDao environmentDao;

    // 根据分区ID查询环境量数据
    public LinkedList<Object> queryAllDataByPartitionId(int partitionId){
        LinkedList<Object> linkedList = new LinkedList<>();

        // 根据分区ID查询所有设备在使用的通道
        List<EnvironmentDO> channelList =  environmentDao.queryUseChannels(partitionId);
        // 根据分区ID查询所有通道数据
        List<EnvironmentDevice> environmentDeviceList = environmentDao.queryAllDataByPartitionId(partitionId);

        LinkedList<Object> list = new LinkedList<Object>();       // 每种设备信息放在一个HashMap中
        // 根据在使用的通道取出对应的数据
        for(int j=0;j<environmentDeviceList.size();j++){      // 遍历所有环境量设备

            for(int i=0;i<channelList.size();i++) {             // 遍历所有在使用的通道

                if (environmentDeviceList.get(j).getTerminalId().equals(channelList.get(i).getTerminalId())) {    // 如果时同一个设备则进行解析，如果不是,下一轮再解析
                    HashMap<String,Object> hashMap = new HashMap<>();   // 每种通道下用集合形式存放具体数据
                    switch (channelList.get(i).getChannelId()){
                        case "1":
                            hashMap.put("channelID", "1");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDeviceList.get(j).getPassageOne());
                            break;
                        case "2":
                            hashMap.put("channelID", "2");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDeviceList.get(j).getPassageTwo());
                            break;
                        case "3":
                            hashMap.put("channelID", "3");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDeviceList.get(j).getPassageThree());
                            break;
                        case "4":
                            hashMap.put("channelID", "4");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDeviceList.get(j).getPassageFour());
                            break;
                        case "5":
                            hashMap.put("channelID", "5");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDeviceList.get(j).getPassageFive());
                            break;
                        case "6":
                            hashMap.put("channelID", "6");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDeviceList.get(j).getPassageSix());
                            break;
                        case "7":
                            hashMap.put("channelID", "7");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDeviceList.get(j).getPassageSeven());
                            break;
                        case "8":
                            hashMap.put("channelID", "8");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDeviceList.get(j).getPassageEigth());
                            break;
                        case "9":
                            hashMap.put("channelID", "9");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDeviceList.get(j).getPassageNine());
                            break;
                        case "10":
                            hashMap.put("channelID", "10");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDeviceList.get(j).getPassageTen());
                            break;
                    }
                    hashMap.put("id", environmentDeviceList.get(j).getId());
                    hashMap.put("terminalID", environmentDeviceList.get(j).getTerminalId());
                    hashMap.put("terminalIP", environmentDeviceList.get(j).getTerminalIp());
                    hashMap.put("insertTime", environmentDeviceList.get(j).getInsertTime());
                    hashMap.put("partitionID", partitionId);

                    list.add(hashMap);
                }

            }
        }
        return list;
    }


    // 根据环境量和表皮测温设备id查询该设备最新数据
    public LinkedList<Object> queryDatasByTerminalId(String terminalId){
        // 根据环境量和表皮测温设备id查询该设备最新数据
        List<EnvironmentDevice> environmentDevice = environmentDao.queryDatasByTerminalId(terminalId);
        // 根据设备ID查询所有设备在使用的通道
        List<EnvironmentDO> channelList =  environmentDao.queryUseChannelsByTerminalId(terminalId);
        LinkedList<Object> list = new LinkedList<Object>();
        for(int j=0;j<environmentDevice.size();j++) {      // 遍历所有环境量设备
            for(int i=0;i<channelList.size();i++) {     // 遍历所有在使用的通道
                if (environmentDevice.get(j).getTerminalId().equals(channelList.get(i).getTerminalId())) {    // 如果时同一个设备则进行解析
                    HashMap<String,Object> hashMap = new HashMap<>();   // 每种通道下用集合形式存放具体数据
                    switch (channelList.get(i).getChannelId()){
                        case "1":
                            hashMap.put("channelID", "1");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDevice.get(j).getPassageOne());
                            break;
                        case "2":
                            hashMap.put("channelID", "2");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDevice.get(j).getPassageTwo());
                            break;
                        case "3":
                            hashMap.put("channelID", "3");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDevice.get(j).getPassageThree());
                            break;
                        case "4":
                            hashMap.put("channelID", "4");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDevice.get(j).getPassageFour());
                            break;
                        case "5":
                            hashMap.put("channelID", "5");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDevice.get(j).getPassageFive());
                            break;
                        case "6":
                            hashMap.put("channelID", "6");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDevice.get(j).getPassageSix());
                            break;
                        case "7":
                            hashMap.put("channelID", "7");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDevice.get(j).getPassageSeven());
                            break;
                        case "8":
                            hashMap.put("channelID", "8");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDevice.get(j).getPassageEigth());
                            break;
                        case "9":
                            hashMap.put("channelID", "9");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDevice.get(j).getPassageNine());
                            break;
                        case "10":
                            hashMap.put("channelID", "10");
                            hashMap.put("name", channelList.get(i).getName());
                            hashMap.put("value", environmentDevice.get(j).getPassageTen());
                            break;
                    }
                    hashMap.put("id", environmentDevice.get(j).getId());
                    hashMap.put("terminalID", environmentDevice.get(j).getTerminalId());
                    hashMap.put("terminalIP", environmentDevice.get(j).getTerminalIp());
                    hashMap.put("insertTime", environmentDevice.get(j).getInsertTime());

                    list.add(hashMap);
                }
            }
        }

        return list;
    }

    // 查询环境量数据并分页
    @Override
    public PageResult queryEnvironmentDatasPage(PageRequest pageRequest, String terminalId, Date beginTime, Date endTime) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest, terminalId, beginTime, endTime));
    }
    /**
     * 调用分页插件完成分页
     */
    private PageInfo<EnvironmentDevice> getPageInfo(PageRequest pageRequest, String terminalId, Date beginTime, Date endTime) {
        LinkedList list = new LinkedList();
        LinkedList<Object> dateTimeList = new LinkedList<>();

        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();

        // 分页
        PageHelper.startPage(pageNum, pageSize);
        List<EnvironmentDevice> environmentDeviceList = environmentDao.queryEnvironmentDatasPage(terminalId, beginTime, endTime);
        // 根据设备ID查询所有设备在使用的通道
        List<EnvironmentDO> channelList =  environmentDao.queryUseChannelsByTerminalId(terminalId);
        //
        // for (EnvironmentDevice  environmentDevice: environmentDeviceList) {
        //     for(int i=0;i<channelList.size();i++) {     // 遍历所有在使用的通道
        //         if (environmentDevice.getTerminalId().equals(channelList.get(i).getTerminalId())) {    // 如果时同一个设备则进行解析
        //             HashMap<String,Object> hashMap = new HashMap<>();   // 每种通道下用集合形式存放具体数据
        //             switch (channelList.get(i).getChannelId()){
        //                 case "1":
        //                     hashMap.put("channelID", "1");
        //                     hashMap.put("name", channelList.get(i).getName());
        //                     hashMap.put("value", environmentDevice.getPassageOne());
        //                     break;
        //                 case "2":
        //                     hashMap.put("channelID", "2");
        //                     hashMap.put("name", channelList.get(i).getName());
        //                     hashMap.put("value", environmentDevice.getPassageTwo());
        //                     break;
        //                 case "3":
        //                     hashMap.put("channelID", "3");
        //                     hashMap.put("name", channelList.get(i).getName());
        //                     hashMap.put("value", environmentDevice.getPassageThree());
        //                     break;
        //                 case "4":
        //                     hashMap.put("channelID", "4");
        //                     hashMap.put("name", channelList.get(i).getName());
        //                     hashMap.put("value", environmentDevice.getPassageFour());
        //                     break;
        //                 case "5":
        //                     hashMap.put("channelID", "5");
        //                     hashMap.put("name", channelList.get(i).getName());
        //                     hashMap.put("value", environmentDevice.getPassageFive());
        //                     break;
        //                 case "6":
        //                     hashMap.put("channelID", "6");
        //                     hashMap.put("name", channelList.get(i).getName());
        //                     hashMap.put("value", environmentDevice.getPassageSix());
        //                     break;
        //                 case "7":
        //                     hashMap.put("channelID", "7");
        //                     hashMap.put("name", channelList.get(i).getName());
        //                     hashMap.put("value", environmentDevice.getPassageSeven());
        //                     break;
        //                 case "8":
        //                     hashMap.put("channelID", "8");
        //                     hashMap.put("name", channelList.get(i).getName());
        //                     hashMap.put("value", environmentDevice.getPassageEigth());
        //                     break;
        //                 case "9":
        //                     hashMap.put("channelID", "9");
        //                     hashMap.put("name", channelList.get(i).getName());
        //                     hashMap.put("value", environmentDevice.getPassageNine());
        //                     break;
        //                 case "10":
        //                     hashMap.put("channelID", "10");
        //                     hashMap.put("name", channelList.get(i).getName());
        //                     hashMap.put("value", environmentDevice.getPassageTen());
        //                     break;
        //             }
        //             hashMap.put("id", environmentDevice.getId());
        //             hashMap.put("terminalID", environmentDevice.getTerminalId());
        //             hashMap.put("terminalIP", environmentDevice.getTerminalIp());
        //             hashMap.put("insertTime", environmentDevice.getInsertTime());
        //
        //             list.add(hashMap);
        //         }
        //
        //     }
        // }

        for(int i=0;i<channelList.size();i++){
            LinkedList<Object> linkedList = new LinkedList<>();
            for (EnvironmentDevice  environmentDevice: environmentDeviceList) {
                switch(channelList.get(i).getChannelId()){
                    case "1":
                        linkedList.add(environmentDevice.getPassageOne());
                        break;
                    case "2":
                        linkedList.add(environmentDevice.getPassageTwo());
                        break;
                    case "3":
                        linkedList.add(environmentDevice.getPassageThree());
                        break;
                    case "4":
                        linkedList.add(environmentDevice.getPassageFour());
                        break;
                    case "5":
                        linkedList.add(environmentDevice.getPassageFive());
                        break;
                    case "6":
                        linkedList.add(environmentDevice.getPassageSix());
                        break;
                    case "7":
                        linkedList.add(environmentDevice.getPassageSeven());
                        break;
                    case "8":
                        linkedList.add(environmentDevice.getPassageEigth());
                        break;
                    case "9":
                        linkedList.add(environmentDevice.getPassageNine());
                        break;
                    case "10":
                        linkedList.add(environmentDevice.getPassageTen());
                        break;
                }
            }
            list.add(linkedList);
        }

        for (EnvironmentDevice  environmentDevice: environmentDeviceList) {
            dateTimeList.add(environmentDevice.getInsertTime());
        }
        list.add(dateTimeList);

        return new PageInfo<EnvironmentDevice>(list);
    }
}