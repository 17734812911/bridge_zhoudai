package com.xtw.bridge.mapper;

import com.xtw.bridge.model.*;

import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/5
 * Description: 设备配置接口
 */
public interface ConfigDao {

    // 查询所有类型设备名称
    public List<String> queryAllDeviceName();
    // 根据terminalId查询device表是否已经存在要添加的设备
    public int queryIsExistDevice(String terminalId);
    // 根据terminalId查询device_config表是否已经存在要添加的设备
    public int queryIsExistDeviceConfig(String terminalId);
    // 根据channel和partitionId判断表中是否已经存在要添加的配置
    public int queryIsExist_GXCE(String channel, Integer partitionId);

    // 查询外置局放所有配置
    public List<HashMap<String, String>> queryOutPartialConfig();

    // 查询光纤测温所有配置
    public List<HashMap<String, String>> queryFibreTemperatureConfig();

    // 查询环境量和表皮测温所有配置
    public List<HashMap<String, String>> queryEnvironmentDeviceConfigs();

    /**
     *  添加外置局放配置
     */
    // 添加device表配置数据
    public int addDevice(Device device);
    // 添加outpartial_config表配置数据
    public int addOutpartialConfig(OutPartialConfig outPartialConfig);
    // 添加device_config表配置数据
    public int addDeviceConfig(HashMap<String,Object> map);
    // 根据产品名称(比如外置局放)查询设备的type_id
    public String queryDeviceTypeId(String name);
    // 根据线路名称查询对应的line_id
    public String queryLineId(String name);

    /**
     *  修改外置局放配置
     */
    // 更新device表配置
    public int editDevice(Device device);
    // 更新device_config表配置
    public int editDeviceConfig(HashMap<String,Object> map);
    // 更新outpartial_config表配置
    public int editOutpartialConfig(OutPartialConfig outPartialConfig);

    /**
     *  删除外置局放配置
     */
    // 删除device表对应配置数据
    public int delDevice(String terminalId);
    // 删除device_config表对应配置数据
    public int delDeviceConfig(String terminalId);
    // 删除outpartial_config表对应配置数据
    public int delOutpartialConfig(String terminalId);

    /**
     *  添加光纤测温配置
     */
    public int addFibreTemperatureConfig(FibreTemperatureConfig fibreTemperatureConfig);

    /**
     *  修改光纤测温配置
     */
    public int editFibreTemperatureConfig(FibreTemperatureConfig fibreTemperatureConfig);

    /**
     * 删除光纤测温配置
     */
    public int delFibreTemperatureConfig(String channel, Integer partitionId);


    /**
     *  添加环境量配置
     */
    // 添加channel_attribute表配置数据
    public int addEnvironmentConfig(EnvironmentConfig environmentConfig);
    // 更新channel_attribute表配置数据
    public void editEnvironmentConfig(EnvironmentConfig environmentConfig);
    // 删除channel_attribute表配置数据
    public void delEnvironmentConfig(String terminalId);

    // 条件查询光纤测温配置
    public List<HashMap<String, String>> queryGxcwByCondition(String channel, Integer partitionId);
    // 条件查询环境量和表皮测温配置
    public List<HashMap<String, String>> queryEnvironmentByCondition(String deviceName, Integer channelId, String deviceIp, Integer partitionId);
}
