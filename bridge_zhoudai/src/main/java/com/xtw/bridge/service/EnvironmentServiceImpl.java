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
                    if("BPWD".equals(channelList.get(i).getChannelType())){
                        switch (channelList.get(i).getChannelId()){
                            case "1":
                                hashMap.put("channelID", "1");
                                hashMap.put("name", channelList.get(i).getName());
                                hashMap.put("phase", "A1相");
                                hashMap.put("produceName", "表皮温度");
                                hashMap.put("value", environmentDevice.get(j).getPassageOne());
                                break;
                            case "2":
                                hashMap.put("channelID", "2");
                                hashMap.put("name", channelList.get(i).getName());
                                hashMap.put("phase", "B1相");
                                hashMap.put("value", environmentDevice.get(j).getPassageTwo());
                                break;
                            case "3":
                                hashMap.put("channelID", "3");
                                hashMap.put("name", channelList.get(i).getName());
                                hashMap.put("phase", "C1相");
                                hashMap.put("value", environmentDevice.get(j).getPassageThree());
                                break;
                            case "6":
                                hashMap.put("channelID", "6");
                                hashMap.put("name", channelList.get(i).getName());
                                hashMap.put("phase", "A2相");
                                hashMap.put("value", environmentDevice.get(j).getPassageSix());
                                break;
                            case "7":
                                hashMap.put("channelID", "7");
                                hashMap.put("name", channelList.get(i).getName());
                                hashMap.put("phase", "B2相");
                                hashMap.put("value", environmentDevice.get(j).getPassageSeven());
                                break;
                            case "8":
                                hashMap.put("channelID", "8");
                                hashMap.put("name", channelList.get(i).getName());
                                hashMap.put("phase", "C2相");
                                hashMap.put("value", environmentDevice.get(j).getPassageEigth());
                                break;
                        }
                    } else{
                        switch (channelList.get(i).getChannelId()){
                            case "1":
                                hashMap.put("channelID", "1");
                                hashMap.put("name", channelList.get(i).getName());
                                hashMap.put("produceName", "环境量");
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
                    }
                    hashMap.put("id", environmentDevice.get(j).getId());
                    hashMap.put("terminalID", environmentDevice.get(j).getTerminalId());
                    hashMap.put("terminalIP", environmentDevice.get(j).getTerminalIp());
                    hashMap.put("deviceName", channelList.get(i).getChannelType());
                    hashMap.put("insertTime", environmentDevice.get(j).getInsertTime());

                    list.add(hashMap);
                }
            }
        }

        return list;
    }

    // 查询环境量数据并分页
    @Override
    public PageResult queryEnvironmentDatasPage(PageRequest pageRequest, String terminalId, String beginTime, String endTime) {
        return PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest, terminalId, beginTime, endTime));
    }
    /**
     * 调用分页插件完成分页
     */
    private PageInfo<EnvironmentDevice> getPageInfo(PageRequest pageRequest, String terminalId, String beginTime, String endTime) {
        LinkedList list = new LinkedList();
        LinkedList<Object> dateTimeList = new LinkedList<>();

        int pageNum = pageRequest.getPageNum();
        int pageSize = pageRequest.getPageSize();

        // 分页
        PageHelper.startPage(pageNum, pageSize);
        List<EnvironmentDevice> environmentDeviceList = environmentDao.queryEnvironmentDatasPage(terminalId, beginTime, endTime);
        // 根据设备ID查询所有设备在使用的通道
        List<EnvironmentDO> channelList =  environmentDao.queryUseChannelsByTerminalId(terminalId);

        for(int i=0;i<channelList.size();i++){
            LinkedList<Object> linkedList = new LinkedList<>();
            for (EnvironmentDevice  environmentDevice: environmentDeviceList) {
                HashMap<String,Object> hashMap = new HashMap<>();   // 每种通道下用集合形式存放具体数据
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

    // 查询环境量某个通道的历史曲线
    public ArrayList<Object> queryChannelDatas(String terminalId, String channelId, String begintime, String endtime){
        ArrayList<Object> arrayList = new ArrayList<>();
        List<EnvironmentDevice> environmentDeviceList = environmentDao.queryChannelDatas(terminalId, begintime, endtime);
        for(int i=0;i<environmentDeviceList.size();i++){
            HashMap<String,Object> hashMap = new HashMap<>();
            switch (channelId){
                case "1":
                    hashMap.put("value", environmentDeviceList.get(i).getPassageOne());
                    break;
                case "2":
                    hashMap.put("value", environmentDeviceList.get(i).getPassageTwo());
                    break;
                case "3":
                    hashMap.put("value", environmentDeviceList.get(i).getPassageThree());
                    break;
                case "4":
                    hashMap.put("value", environmentDeviceList.get(i).getPassageFour());
                    break;
                case "5":
                    hashMap.put("value", environmentDeviceList.get(i).getPassageFive());
                    break;
                case "6":
                    hashMap.put("value", environmentDeviceList.get(i).getPassageSix());
                    break;
                case "7":
                    hashMap.put("value", environmentDeviceList.get(i).getPassageSeven());
                    break;
                case "8":
                    hashMap.put("value", environmentDeviceList.get(i).getPassageEigth());
                    break;
                case "9":
                    hashMap.put("value", environmentDeviceList.get(i).getPassageNine());
                    break;
                case "10":
                    hashMap.put("value", environmentDeviceList.get(i).getPassageTen());
                    break;
            }
            hashMap.put("time", environmentDeviceList.get(i).getInsertTime());
            arrayList.add(hashMap);
        }
        return arrayList;

    }

    // 获取所有环境量最新数据
    public ArrayList<HashMap<String,String>> queryDatasHJL(){
        List<EnvironmentDevice> environmentDataList = environmentDao.queryDatasHJL();
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for(int i=0;i<environmentDataList.size();i++){
            HashMap<String,String> map = new HashMap<>();
            map.put("partitionID", environmentDataList.get(i).getPartitionId());
            map.put("sd", environmentDataList.get(i).getPassageOne());
            map.put("wd", environmentDataList.get(i).getPassageTwo());
            map.put("yq", environmentDataList.get(i).getPassageSix());
            map.put("yyht", environmentDataList.get(i).getPassageSeven());
            map.put("lhq", environmentDataList.get(i).getPassageEigth());
            map.put("jw", environmentDataList.get(i).getPassageNine());
            map.put("time", environmentDataList.get(i).getInsertTime());

            arrayList.add(map);
        }
        return arrayList;
    }

    // 获取所有表皮温度最新数据
    public ArrayList<HashMap<String,String>> queryDatasBPWD(){
        List<EnvironmentDevice> environmentDataList = environmentDao.queryDatasBPWD();
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        for(int i=0;i<environmentDataList.size();i++){
            HashMap<String,String> map = new HashMap<>();
            map.put("partitionID", environmentDataList.get(i).getPartitionId());
            map.put("aPhase", environmentDataList.get(i).getPassageOne());
            map.put("bPhase", environmentDataList.get(i).getPassageTwo());
            map.put("cPhase", environmentDataList.get(i).getPassageThree());
            map.put("time", environmentDataList.get(i).getInsertTime());

            arrayList.add(map);
        }
        return arrayList;
    }
}
