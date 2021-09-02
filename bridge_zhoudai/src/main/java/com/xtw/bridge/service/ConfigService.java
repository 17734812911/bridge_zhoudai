package com.xtw.bridge.service;

import com.xtw.bridge.mapper.ConfigDao;
import com.xtw.bridge.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/5
 * Description: No Description
 */
@Service
public class ConfigService implements ConfigDao{

    @Resource
    ConfigDao configDao;

    // 查询所有类型设备名称
    @Override
    public List<String> queryAllDeviceName(){
        return configDao.queryAllDeviceName();
    }

    // 根据terminalId查询device表是否已经存在要添加的设备
    @Override
    public int queryIsExistDevice(String terminalId) {
        return configDao.queryIsExistDevice(terminalId);
    }
    // 根据terminalId查询device_config表是否已经存在要添加的设备
    @Override
    public int queryIsExistDeviceConfig(String terminalId) {
        return configDao.queryIsExistDeviceConfig(terminalId);
    }
    // 根据channel和partitionId判断表中是否已经存在要添加的配置
    @Override
    public int queryIsExist_GXCE(String channel, Integer partitionId) {
        return configDao.queryIsExist_GXCE(channel, partitionId);
    }

    // 查询外置局放所有配置
    @Override
    public List<HashMap<String, String>> queryOutPartialConfig() {
        return configDao.queryOutPartialConfig();
    }
    // 查询光纤测温所有配置
    @Override
    public List<HashMap<String, String>> queryFibreTemperatureConfig() {
        return configDao.queryFibreTemperatureConfig();
    }
    // 查询环境量和表皮测温所有配置
    @Override
    public List<HashMap<String, String>> queryEnvironmentDeviceConfigs() {
        return configDao.queryEnvironmentDeviceConfigs();
    }

    /**
     *  添加外置局放配置
     */
    @Transactional      // Spring事务(会等到本方法中包含的方法都执行完后一起提交，否则都不提交)
    public int addOutPartialConfig(OutPartialConfig outPartialConfig){
        // 构造device对象
        Device device = structureDevice(outPartialConfig);
        // 查询设备类型ID
        String deviceTypeId = queryDeviceTypeId(outPartialConfig.getName());
        // 判断设备是否已经添加过
        if(queryIsExistDevice(device.getTerminalId()) > 0){
            return 0;
        }

        // 添加device表配置数据
        int result = addDevice(device);
        // 添加outpartial_config表配置数据
        addOutpartialConfig(outPartialConfig);
        // 添加device_config表配置数据
        addDeviceConfig(outPartialConfig,deviceTypeId);
        if(result == 1){
            return 1;
        }
        return 0;
    }
    // 添加device表配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int addDevice(Device device) {
        return configDao.addDevice(device);
    }
    // 添加outpartial_config表配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int addOutpartialConfig(OutPartialConfig outPartialConfig) {
        return configDao.addOutpartialConfig(outPartialConfig);
    }
    // 添加device_config表配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    public int addDeviceConfig(OutPartialConfig outPartialConfig, String deviceTypeId){
        // 构建map对象
        HashMap<String,Object> map = structureMap(outPartialConfig, deviceTypeId);
        return addDeviceConfig(map);
    }
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int addDeviceConfig(HashMap<String,Object> map) {
        return configDao.addDeviceConfig(map);
    }
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public String queryDeviceTypeId(String name) {
        return configDao.queryDeviceTypeId(name);
    }
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public String queryLineId(String name) {
        return configDao.queryLineId(name);
    }


    /**
     *  修改外置局放配置
     */
    @Transactional      // Spring事务(会等到本方法中包含的方法都执行完后一起提交，否则都不提交)
    public int editOutPartialConfig(OutPartialConfig outPartialConfig){
        Device device = structureDevice(outPartialConfig);
        String deviceTypeId = queryDeviceTypeId(outPartialConfig.getName());
        // 更新device表配置
        int result = editDevice(device);
        // 更新device_config表配置
        editDeviceConfig(outPartialConfig, deviceTypeId);
        // 更新outpartial_config表配置
        editOutpartialConfig(outPartialConfig);
         if(result == 1){
             return 1;
         }
         return 0;
    }
    // 更新device表配置
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int editDevice(Device device){
        return configDao.editDevice(device);
    }
    // 更新device_config表配置
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    public int editDeviceConfig(OutPartialConfig outPartialConfig, String deviceTypeId) {
        // 构建map对象
        HashMap<String,Object> map = structureMap(outPartialConfig, deviceTypeId);
        return editDeviceConfig(map);
    }
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int editDeviceConfig(HashMap<String,Object> map) {
        return configDao.editDeviceConfig(map);
    }
    // 更新outpartial_config表配置
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int editOutpartialConfig(OutPartialConfig outPartialConfig) {
        return configDao.editOutpartialConfig(outPartialConfig);
    }


    /**
     *  删除外置局放配置
     */
    @Transactional      // Spring事务(会等到本方法中包含的方法都执行完后一起提交，否则都不提交)
    public int delOutPartialConfigs(String terminalId){
        int result = delDevice(terminalId);
        delDeviceConfig(terminalId);
        delOutpartialConfig(terminalId);
        if(result == 1){
            return 1;
        }
        return 0;
    }
    // 删除device表对应配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int delDevice(String terminalId) {
        return configDao.delDevice(terminalId);
    }
    // 删除device_config表对应配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int delDeviceConfig(String terminalId) {
        return configDao.delDeviceConfig(terminalId);
    }
    // 删除outpartial_config表对应配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int delOutpartialConfig(String terminalId) {
        return configDao.delOutpartialConfig(terminalId);
    }


    /**
     *  添加光纤测温配置
     */
    @Override
    public int addFibreTemperatureConfig(FibreTemperatureConfig fibreTemperatureConfig) {
        // 根据channel和partitionId判断表中是否已经存在要添加的配置
        int result = queryIsExist_GXCE(fibreTemperatureConfig.getChannel(), fibreTemperatureConfig.getPartitionId());
        if(result > 0){
            return 0;
        }
        return configDao.addFibreTemperatureConfig(fibreTemperatureConfig);
    }

    /**
     *  修改光纤测温配置
     */
    @Override
    public int editFibreTemperatureConfig(FibreTemperatureConfig fibreTemperatureConfig) {
        return configDao.editFibreTemperatureConfig(fibreTemperatureConfig);
    }

    /**
     *  删除光纤测温配置
     */
    @Override
    public int delFibreTemperatureConfig(String channel, Integer partitionId) {
        return configDao.delFibreTemperatureConfig(channel, partitionId);
    }



    /**
     *  添加环境量和表皮测温配置
     */
    @Transactional      // Spring事务(会等到本方法中包含的方法都执行完后一起提交，否则都不提交)
    public int addEnvironmentConfigs(EnvironmentConfig environmentConfig) {
        int result = 0;
        Device device = structureDevice(environmentConfig);
        String deviceTypeId = queryDeviceTypeId(environmentConfig.getName());
        int number_device = queryIsExistDevice(environmentConfig.getTerminalId());
        int number_device_config = queryIsExistDeviceConfig(environmentConfig.getTerminalId());
        if(number_device == 0){
            // 向device表添加配置数据
            addDevice(device);
        }
        if(number_device_config == 0){
            // 向device_config表中添加配置数据
            addDeviceConfig(environmentConfig, deviceTypeId);
        }
        // 向channel_attribute表添加配置数据
        result = addEnvironmentConfig(environmentConfig);
        if(result > 0){
            return 1;
        }
        return 0;
    }
    // 向device_config表中添加配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    public void addDeviceConfig(EnvironmentConfig environmentConfig, String deviceTypeId){
        // 构建map对象
        HashMap<String,Object> map = structureMap(environmentConfig, deviceTypeId);
        addDeviceConfig(map);
    }
    // 向channel_attribute表添加配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public int addEnvironmentConfig(EnvironmentConfig environmentConfig) {
        return configDao.addEnvironmentConfig(environmentConfig);
    }

    /**
     *  修改环境量和表皮测温配置
     */
    @Transactional      // Spring事务(会等到本方法中包含的方法都执行完后一起提交，否则都不提交)
    public int editEnvironmentConfigs(EnvironmentConfig environmentConfig){
        Device device = structureDevice(environmentConfig);
        String deviceTypeId = queryDeviceTypeId(environmentConfig.getName());
        // 更新device表配置数据
        int result = editDevice(device);
        // 更新device_config表配置数据
        editEnvironmentDeviceConfig(environmentConfig, deviceTypeId);
        // 更新channel_attribute表配置数据
        editEnvironmentConfig(environmentConfig);

        if(result > 0){
            return 1;
        }
        return 0;
    }
    //更新device_config表配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    public void editEnvironmentDeviceConfig(EnvironmentConfig environmentConfig, String deviceTypeId){
        // 构建map对象
        HashMap<String,Object> map = structureMap(environmentConfig, deviceTypeId);
        editDeviceConfig(map);
    }
    // 更新channel_attribute表配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public void editEnvironmentConfig(EnvironmentConfig environmentConfig) {
        configDao.editEnvironmentConfig(environmentConfig);
    }


    /**
     *  删除环境量和表皮测温配置
     */
    @Transactional      // Spring事务(会等到本方法中包含的方法都执行完后一起提交，否则都不提交)
    public int delEnvironmentConfigs(String terminalId){
        // 删除device表配置数据
        int result = delDevice(terminalId);
        // 删除device_config表配置数据
        delDeviceConfig(terminalId);
        // 删除channel_attribute表配置数据
        delEnvironmentConfig(terminalId);

        if(result > 0){
            return 1;
        }
        return 0;
    }
    // 删除channel_attribute表配置数据
    @Transactional(propagation = Propagation.REQUIRED)     // 如果当前没有事务，就新建一个事务，如果已经存在在一个事务中，就加入到这个事务中
    @Override
    public void delEnvironmentConfig(String terminalId) {
        configDao.delEnvironmentConfig(terminalId);
    }

    /**
     *  条件查询光纤测温配置
     */
    @Override
    public List<HashMap<String, String>> queryGxcwByCondition(String channel, Integer partitionId) {
        return configDao.queryGxcwByCondition(channel, partitionId);
    }

    /**
     *  条件查询环境量和表皮测温配置
     */
    @Override
    public List<HashMap<String, String>> queryEnvironmentByCondition(String deviceName, Integer channelId, String deviceIp, Integer partitionId) {
        return configDao.queryEnvironmentByCondition(deviceName, channelId, deviceIp, partitionId);
    }

    /**
     *  摄像头配置
     * @param camera
     * @return
     */
    @Override
    public int addCameraConfig(Camera camera) {
        // 查看是否有该设备的配置
        if(queryIsExistDevice(camera.getTerminalId()) > 0){
            return 0;
        }
        // 构造device对象
        Device device = structureDevice(camera);
        // 添加device表配置数据
        int result = addDevice(device);
        // 添加camera表配置
        configDao.addCameraConfig(camera);

        return result;
    }


    // 构建device类
    private Device structureDevice(OutPartialConfig outPartialConfig){
        String lineId = queryLineId(outPartialConfig.getLineName());
        String deviceTypeId = queryDeviceTypeId(outPartialConfig.getName());
        Device device = new Device();
        device.setDeviceName(outPartialConfig.getDeviceName());
        device.setDeviceTypeId(Integer.parseInt(deviceTypeId));
        device.setTerminalId(outPartialConfig.getTerminalId());
        device.setLineId(Integer.parseInt(lineId));
        device.setJoint(outPartialConfig.getJoint());
        return device;
    }

    // 构建device类
    private Device structureDevice(EnvironmentConfig environmentConfig){
        String lineId = queryLineId(environmentConfig.getLineName());
        String deviceTypeId = queryDeviceTypeId(environmentConfig.getName());
        Device device = new Device();
        device.setDeviceName(environmentConfig.getDeviceName());
        device.setDeviceTypeId(Integer.parseInt(deviceTypeId));
        device.setTerminalId(environmentConfig.getTerminalId());
        device.setLineId(Integer.parseInt(lineId));
        device.setJoint(environmentConfig.getJoint());
        return device;
    }

    // 构建device类
    private Device structureDevice(Camera camera){
        String lineId = queryLineId(camera.getLineName());
        String deviceTypeId = queryDeviceTypeId(camera.getProduceName());
        Device device = new Device();
        device.setDeviceName(camera.getDeviceName());
        device.setDeviceTypeId(Integer.parseInt(deviceTypeId));
        device.setTerminalId(camera.getTerminalId());
        device.setLineId(Integer.parseInt(lineId));
        device.setJoint(camera.getJoint());
        return device;

    }

    // 构造OutPartialConfig的map对象
    private HashMap<String, Object> structureMap(OutPartialConfig outPartialConfig, String deviceTypeId){
        HashMap<String,Object> map = new HashMap<>();
        map.put("terminalId", outPartialConfig.getTerminalId());
        map.put("deviceTypeId", deviceTypeId);
        map.put("deviceIp", outPartialConfig.getDeviceIp());
        map.put("port", outPartialConfig.getPort());
        map.put("partitionId", outPartialConfig.getPartitionId());
        map.put("intervalSlave", outPartialConfig.getIntervalSlave());
        map.put("retryCount", outPartialConfig.getRetryCount());
        map.put("retryInterval", outPartialConfig.getRetryInterval());
        map.put("pollingInterval", outPartialConfig.getPollingInterval());
        return map;
    }

    // 构造EnvironmentConfig的map对象
    private HashMap<String, Object> structureMap(EnvironmentConfig environmentConfig, String deviceTypeId){
        HashMap<String,Object> map = new HashMap<>();
        map.put("terminalId", environmentConfig.getTerminalId());
        map.put("deviceTypeId", deviceTypeId);
        map.put("deviceIp", environmentConfig.getDeviceIp());
        map.put("port", environmentConfig.getPort());
        map.put("partitionId", environmentConfig.getPartitionId());
        map.put("intervalSlave", environmentConfig.getIntervalSlave());
        map.put("retryCount", environmentConfig.getRetryCount());
        map.put("retryInterval", environmentConfig.getRetryInterval());
        map.put("pollingInterval", environmentConfig.getPollingInterval());
        return map;
    }
}
