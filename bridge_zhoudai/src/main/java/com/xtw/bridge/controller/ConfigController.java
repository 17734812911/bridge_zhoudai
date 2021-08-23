package com.xtw.bridge.controller;

import com.xtw.bridge.model.EnvironmentConfig;
import com.xtw.bridge.model.FibreTemperatureConfig;
import com.xtw.bridge.model.OutPartialConfig;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * User: Mr.Chen
 * Date: 2021/8/5
 * Description: No Description
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Resource
    ConfigService configService;

    // 查询所有类型设备名称
    @GetMapping("/devicenames")
    @Operation(
            summary = "查询所有类型设备名称"
    )
    public ResponseFormat queryAllDeviceName(){
        List<String> deviceNames = configService.queryAllDeviceName();
        if(deviceNames != null){
            return ResponseFormat.success("查询成功", deviceNames);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }

    // 查询外置局放所有配置
    @GetMapping("/outpartialconfigs")
    @Operation(
            summary = "查询外置局放所有配置"
    )
    public ResponseFormat queryOutPartialConfig(){
        List<HashMap<String, String>> configList = configService.queryOutPartialConfig();

        if(! configList.isEmpty()){
            return ResponseFormat.success("查询成功", configList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }

    }

    // 查询光纤测温所有配置
    @GetMapping("/fibretemperatureconfigs")
    @Operation(
            summary = "查询光纤测温所有配置"
    )
    public ResponseFormat queryFibreTemperatureConfig(){
        List<HashMap<String, String>> configList = configService.queryFibreTemperatureConfig();

        if(! configList.isEmpty()){
            return ResponseFormat.success("查询成功", configList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }

    // 查询环境量和表皮测温所有配置
    @GetMapping("/environmentconfigs")
    @Operation(
            summary = "查询环境量和表皮测温所有配置"
    )
    public ResponseFormat queryEnvironmentConfigs(){
        List<HashMap<String, String>> configList = configService.queryEnvironmentDeviceConfigs();
        if(! configList.isEmpty()){
            return ResponseFormat.success("查询成功", configList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }

    // 添加外置局放设备
    @PostMapping("/outpartial")
    @Operation(
            summary = "添加外置局放设备",
            parameters = {
                    @Parameter(name = "name", description = "产品名称"),
                    @Parameter(name = "devicename", description = "设备名称"),
                    @Parameter(name = "communicationmode", description = "通讯方式"),
                    @Parameter(name = "terminalid", description = "设备ID"),
                    @Parameter(name = "joint", description = "接头名称"),
                    @Parameter(name = "linename", description = "线路名称"),
                    @Parameter(name = "partitionid", description = "分区ID"),
                    @Parameter(name = "alarmvalue", description = "报警值"),
                    @Parameter(name = "alarmfrequencyvalue", description = "放电频次报警值"),
                    @Parameter(name = "offsetvalue", description = "偏移量"),
                    @Parameter(name = "deviceip", description = "设备IP"),
                    @Parameter(name = "port", description = "监听端口"),
                    @Parameter(name = "intervalslave", description = "设备间访问间隔时间（毫秒）"),
                    @Parameter(name = "retrycount", description = "失败重试次数"),
                    @Parameter(name = "retryinterval", description = "失败后重发次数中的时间间隔（毫秒）"),
                    @Parameter(name = "pollinginterval", description = "多久采集一遍数据（毫秒）")
            }
    )
    public ResponseFormat addOutPartial(@RequestBody HashMap<String,String> map){
        // 生成外置局放配置实体类
        OutPartialConfig outPartialConfig = getOutPartialConfig(map);

        int result = configService.addOutPartialConfig(outPartialConfig);
        if(result > 0){
            return ResponseFormat.success("添加成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.ADD_ERROR, "添加失败"));
        }
    }

    // 更新外置局放设备
    @PutMapping("/outpartial")
    @Operation(
            summary = "更新外置局放设备配置",
            parameters = {
                    @Parameter(name = "name", description = "产品名称"),
                    @Parameter(name = "devicename", description = "设备名称"),
                    @Parameter(name = "communicationmode", description = "通讯方式"),
                    @Parameter(name = "terminalid", description = "设备ID"),
                    @Parameter(name = "joint", description = "接头名称"),
                    @Parameter(name = "linename", description = "线路名称"),
                    @Parameter(name = "partitionid", description = "分区ID"),
                    @Parameter(name = "alarmvalue", description = "报警值"),
                    @Parameter(name = "alarmfrequencyvalue", description = "放电频次报警值"),
                    @Parameter(name = "offsetvalue", description = "偏移量"),
                    @Parameter(name = "deviceip", description = "设备IP"),
                    @Parameter(name = "port", description = "监听端口"),
                    @Parameter(name = "intervalslave", description = "设备间访问间隔时间（毫秒）"),
                    @Parameter(name = "retrycount", description = "失败重试次数"),
                    @Parameter(name = "retryinterval", description = "失败后重发次数中的时间间隔（毫秒）"),
                    @Parameter(name = "pollinginterval", description = "多久采集一遍数据（毫秒）")
            }
    )
    public ResponseFormat editOutPartial(@RequestBody HashMap<String,String> map){
        // 生成外置局放配置实体类
        OutPartialConfig outPartialConfig = getOutPartialConfig(map);

        int result = configService.editOutPartialConfig(outPartialConfig);
        if(result > 0){
            return ResponseFormat.success("更新成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.ADD_ERROR, "更新失败"));
        }
    }

    // 删除外置局放配置
    @DeleteMapping("/outpartial")
    @Operation(
            summary = "删除外置局放配置",
            parameters = {
                    @Parameter(name = "terminalid", description = "设备ID")
            }
    )
    public ResponseFormat delOutPartial(String terminalid){
        int result = configService.delOutPartialConfigs(terminalid);
        if(result > 0){
            return ResponseFormat.success("删除成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.ADD_ERROR, "删除失败"));
        }
    }


    // 添加光纤测温配置
    @PostMapping("/fibretemperature")
    @Operation(
            summary = "添加光纤测温配置",
            parameters = {
                    @Parameter(name = "channel", description = "通道ID"),
                    @Parameter(name = "partitionid", description = "分区ID"),
                    @Parameter(name = "startposition", description = "分区起始点位"),
                    @Parameter(name = "endposition", description = "分区结束点位"),
                    @Parameter(name = "deviceip", description = "光纤测温采集设备IP"),
                    @Parameter(name = "offsetvalue", description = "偏移量"),
                    @Parameter(name = "alarmvalue", description = "报警值"),
                    @Parameter(name = "read_order", description = "通道读取顺序")
            }
    )
    public ResponseFormat addFibreTemperatureConfig(@RequestBody HashMap<String,String> map){
        FibreTemperatureConfig fibreTemperatureConfig = getFibreTemperatureConfig(map);

        int result = configService.addFibreTemperatureConfig(fibreTemperatureConfig);
        if(result > 0){
            return ResponseFormat.success("添加成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.ADD_ERROR, "添加失败"));
        }
    }

    // 修改光纤测温配置
    @PutMapping("/fibretemperature")
    @Operation(
            summary = "修改光纤测温配置",
            parameters = {
                    @Parameter(name = "channel", description = "通道ID"),
                    @Parameter(name = "partitionid", description = "分区ID"),
                    @Parameter(name = "startposition", description = "分区起始点位"),
                    @Parameter(name = "endposition", description = "分区结束点位"),
                    @Parameter(name = "deviceip", description = "光纤测温采集设备IP"),
                    @Parameter(name = "offsetvalue", description = "偏移量"),
                    @Parameter(name = "alarmvalue", description = "报警值"),
                    @Parameter(name = "read_order", description = "通道读取顺序")
            }
    )
    public ResponseFormat editFibreTemperatureConfig(@RequestBody HashMap<String,String> map){
        FibreTemperatureConfig fibreTemperatureConfig = getFibreTemperatureConfig(map);

        int result = configService.editFibreTemperatureConfig(fibreTemperatureConfig);
        if(result > 0){
            return ResponseFormat.success("修改成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.ADD_ERROR, "修改失败"));
        }
    }

    @DeleteMapping("/fibretemperature")
    @Operation(
            summary = "删除光纤测温配置",
            parameters = {
                    @Parameter(name = "channel", description = "通道ID"),
                    @Parameter(name = "partitionid", description = "分区ID")
            }
    )
    public ResponseFormat delFibreTemperatureConfig(@RequestBody HashMap<String,String> map){
        String channel = map.get("channel");
        Integer partitionId = Integer.parseInt(map.get("partitionid"));
        int result = configService.delFibreTemperatureConfig(channel, partitionId);
        if(result > 0){
            return ResponseFormat.success("删除成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.ADD_ERROR, "删除失败"));
        }
    }


    // 添加环境量和表皮测温配置
    @PostMapping("/environment")
    @Operation(
            summary = "添加环境量和表皮测温配置",
            parameters = {
                    @Parameter(name = "name", description = "产品名称"),
                    @Parameter(name = "devicename", description = "设备名称"),
                    @Parameter(name = "terminalid", description = "设备ID"),
                    @Parameter(name = "linename", description = "线路名称"),
                    @Parameter(name = "joint", description = "接头名称"),
                    @Parameter(name = "deviceip", description = "设备IP"),
                    @Parameter(name = "port", description = "监听端口"),
                    @Parameter(name = "partitionid", description = "分区ID"),
                    @Parameter(name = "intervalslave", description = "设备间访问间隔时间（毫秒）"),
                    @Parameter(name = "retrycount", description = "失败重试次数"),
                    @Parameter(name = "retryinterval", description = "失败后重发次数中的时间间隔（毫秒）"),
                    @Parameter(name = "pollinginterval", description = "再次遍历间隔"),
                    @Parameter(name = "channnelname", description = "通道名称"),
                    @Parameter(name = "channelid", description = "通道ID"),
                    @Parameter(name = "channeltype", description = "通道类型"),
                    @Parameter(name = "rangeup", description = "量程上限"),
                    @Parameter(name = "rangelow", description = "量程下限"),
                    @Parameter(name = "alarmup", description = "报警上限"),
                    @Parameter(name = "offsetvalue", description = "偏移量"),
                    @Parameter(name = "use", description = "通道是否在使用")
            }
    )
    public ResponseFormat addEnvironmentConfigs(@RequestBody HashMap<String,String> map){
        EnvironmentConfig environmentConfig = getEnvironmentConfig(map);
        int result = configService.addEnvironmentConfigs(environmentConfig);
        if(result > 0){
            return ResponseFormat.success("添加成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.ADD_ERROR, "添加失败"));
        }
    }


    // 更新环境量和表皮测温配置
    @PutMapping("/environment")
    @Operation(
            summary = "更新环境量和表皮测温配置",
            parameters = {
                    @Parameter(name = "name", description = "产品名称"),
                    @Parameter(name = "devicename", description = "设备名称"),
                    @Parameter(name = "terminalid", description = "设备ID"),
                    @Parameter(name = "linename", description = "线路名称"),
                    @Parameter(name = "joint", description = "接头名称"),
                    @Parameter(name = "deviceip", description = "设备IP"),
                    @Parameter(name = "port", description = "监听端口"),
                    @Parameter(name = "partitionid", description = "通道ID"),
                    @Parameter(name = "intervalslave", description = "设备间访问间隔时间（毫秒）"),
                    @Parameter(name = "retrycount", description = "失败重试次数"),
                    @Parameter(name = "retryinterval", description = "失败后重发次数中的时间间隔（毫秒）"),
                    @Parameter(name = "pollinginterval", description = "再次遍历间隔"),
                    @Parameter(name = "channnelname", description = "通道名称"),
                    @Parameter(name = "channelid", description = "通道ID"),
                    @Parameter(name = "channeltype", description = "通道类型"),
                    @Parameter(name = "rangeup", description = "量程上限"),
                    @Parameter(name = "rangelow", description = "量程下限"),
                    @Parameter(name = "alarmup", description = "报警上限"),
                    @Parameter(name = "offsetvalue", description = "偏移量"),
                    @Parameter(name = "use", description = "通道是否在使用")
            }
    )
    public ResponseFormat editEnvironmentConfigs(@RequestBody HashMap<String,String> map){
        EnvironmentConfig environmentConfig = getEnvironmentConfig(map);
        int result = configService.editEnvironmentConfigs(environmentConfig);
        if(result > 0){
            return ResponseFormat.success("更新成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.ADD_ERROR, "更新失败"));
        }
    }


    // 删除环境量和表皮测温配置
    @DeleteMapping("/environment")
    @Operation(
            summary = "删除环境量和表皮测温配置",
            parameters = {
                    @Parameter(name = "terminalid", description = "设备ID")
            }
    )
    public ResponseFormat addEnvironmentConfigs(String terminalid){
        int result = configService.delEnvironmentConfigs(terminalid);
        if(result > 0){
            return ResponseFormat.success("删除成功");
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.ADD_ERROR, "删除失败"));
        }
    }


    // 条件查询光纤测温配置
    @GetMapping("/gxcw")
    @Operation(
            summary = "条件查询光纤测温配置",
            parameters = {
                    @Parameter(name = "channel", description = "通道ID"),
                    @Parameter(name = "partitionId", description = "分区ID")
            }
    )
    public ResponseFormat queryGxcwByCondition(String channel, Integer partitionId){
        List<HashMap<String, String>> gxcwList = configService.queryGxcwByCondition(channel, partitionId);
        if(gxcwList.size() >= 0){
            return ResponseFormat.success("查询成功", gxcwList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }

    // 条件查询环境量和表皮测温配置
    @GetMapping("/hjl")
    @Operation(
            summary = "条件查询环境量和表皮测温配置",
            parameters = {
                    @Parameter(name = "deviceName", description = "设备名称"),
                    @Parameter(name = "channelId", description = "通道ID"),
                    @Parameter(name = "deviceIp", description = "设备IP"),
                    @Parameter(name = "partitionId", description = "分区ID")
            }
    )
    public ResponseFormat queryEnvironmentByCondition(String deviceName, Integer channelId, String deviceIp, Integer partitionId){
        List<HashMap<String, String>> environmentList = configService.queryEnvironmentByCondition(deviceName, channelId, deviceIp, partitionId);
        if(environmentList != null){
            return ResponseFormat.success("查询成功", environmentList);
        } else{
            return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
        }
    }


    // 生成外置局放配置实体类
    private OutPartialConfig getOutPartialConfig(HashMap<String,String> map){
        String name = map.get("name");
        String deviceName = map.get("devicename");
        String communicationMode = map.get("communicationmode");
        String terminalId = map.get("terminalid");
        String joint = map.get("joint");
        String lineName = map.get("linename");
        String partitionId = map.get("partitionid");
        Double alarmValue = Double.parseDouble(map.get("alarmvalue"));
        Integer alarmFrequencyValue = Integer.parseInt(map.get("alarmfrequencyvalue"));
        Double offsetValue = Double.parseDouble(map.get("offsetvalue"));
        String deviceIp = map.get("deviceip");
        Integer port = Integer.parseInt(map.get("port"));
        Integer intervalSlave = Integer.parseInt(map.get("intervalslave"));
        Integer retryCount = Integer.parseInt(map.get("retrycount"));
        Integer retryInterval = Integer.parseInt(map.get("retryinterval"));
        Integer pollingInterval = Integer.parseInt(map.get("pollinginterval"));

        OutPartialConfig outPartialConfig = new OutPartialConfig();
        outPartialConfig.setName(name);
        outPartialConfig.setDeviceName(deviceName);
        outPartialConfig.setCommunicationMode(communicationMode);
        outPartialConfig.setTerminalId(terminalId);
        outPartialConfig.setJoint(joint);
        outPartialConfig.setLineName(lineName);
        outPartialConfig.setPartitionId(partitionId);
        outPartialConfig.setAlarmValue(alarmValue);
        outPartialConfig.setAlarmFrequencyValue(alarmFrequencyValue);
        outPartialConfig.setOffsetValue(offsetValue);
        outPartialConfig.setDeviceIp(deviceIp);
        outPartialConfig.setPort(port);
        outPartialConfig.setIntervalSlave(intervalSlave);
        outPartialConfig.setRetryCount(retryCount);
        outPartialConfig.setRetryInterval(retryInterval);
        outPartialConfig.setPollingInterval(pollingInterval);

        return outPartialConfig;
    }

    // 生成光纤测温实体类
    private FibreTemperatureConfig getFibreTemperatureConfig(HashMap<String,String> map){
        FibreTemperatureConfig fibreTemperatureConfig = new FibreTemperatureConfig();
        String channel = map.get("channel");
        Integer partitionId = Integer.parseInt(map.get("partitionid"));
        Integer startPosition = Integer.parseInt(map.get("startposition"));
        Integer endPosition = Integer.parseInt(map.get("endposition"));
        String device_ip = map.get("deviceip");
        Double offsetValue = Double.parseDouble(map.get("offsetvalue"));
        Double alarmValue = Double.parseDouble(map.get("alarmvalue"));
        boolean readOrder = map.get("read_order").equals("1");

        fibreTemperatureConfig.setChannel(channel);
        fibreTemperatureConfig.setPartitionId(partitionId);
        fibreTemperatureConfig.setStartPosition(startPosition);
        fibreTemperatureConfig.setEndPosition(endPosition);
        fibreTemperatureConfig.setDeviceIp(device_ip);
        fibreTemperatureConfig.setOffsetValue(offsetValue);
        fibreTemperatureConfig.setAlarmValue(alarmValue);
        fibreTemperatureConfig.setReadOrder(readOrder);

        return fibreTemperatureConfig;
    }

    // 生成环境量配置实体类
    private EnvironmentConfig getEnvironmentConfig(HashMap<String,String> map){
        EnvironmentConfig environmentConfig = new EnvironmentConfig();
        String name = map.get("name");
        String deviceName = map.get("devicename");
        String terminalId = map.get("terminalid");
        String joint = map.get("joint");
        String lineName = map.get("linename");
        Integer partitionId = Integer.parseInt(map.get("partitionid"));
        Double offsetValue = Double.parseDouble(map.get("offsetvalue"));
        String deviceIp = map.get("deviceip");
        String port = map.get("port");
        Integer intervalSlave = Integer.parseInt(map.get("intervalslave"));
        Integer retryCount = Integer.parseInt(map.get("retrycount"));
        Integer retryInterval = Integer.parseInt(map.get("retryinterval"));
        Integer pollingInterval = Integer.parseInt(map.get("pollinginterval"));
        String channelName = map.get("channnelname");
        Integer channelId = Integer.parseInt(map.get("channelid"));
        String channelType = map.get("channeltype");
        Integer rangeUp = Integer.parseInt(map.get("rangeup"));
        Integer rangeLow = Integer.parseInt(map.get("rangelow"));
        String alarmUp = map.get("alarmup");
        boolean use = map.get("use").equals("1");

        environmentConfig.setName(name);
        environmentConfig.setDeviceName(deviceName);
        environmentConfig.setTerminalId(terminalId);
        environmentConfig.setJoint(joint);
        environmentConfig.setLineName(lineName);
        environmentConfig.setPartitionId(partitionId);
        environmentConfig.setOffsetValue(offsetValue);
        environmentConfig.setDeviceIp(deviceIp);
        environmentConfig.setPort(port);
        environmentConfig.setIntervalSlave(intervalSlave);
        environmentConfig.setRetryCount(retryCount);
        environmentConfig.setRetryInterval(retryInterval);
        environmentConfig.setPollingInterval(pollingInterval);
        environmentConfig.setChannnelName(channelName);
        environmentConfig.setChannelId(channelId);
        environmentConfig.setChannelType(channelType);
        environmentConfig.setRangeUp(rangeUp);
        environmentConfig.setRangeLow(rangeLow);
        environmentConfig.setAlarmUp(alarmUp);
        environmentConfig.setUse(use);

        return environmentConfig;
    }


}
