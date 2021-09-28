package com.xtw.bridge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xtw.bridge.model.Camera;
import com.xtw.bridge.model.CameraAlert;
import com.xtw.bridge.model.CameraTemperatureModel;
import com.xtw.bridge.model.Thermometry;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.CameraService;
import com.xtw.bridge.utils.GetCameraPreviewURLUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Mr.Chen
 * Date: 2021/8/24
 * Description: 摄像头控制器类
 */
@RestController
@RequestMapping("/camera")
public class CameraController {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ObjectMapper mapper = new ObjectMapper();
    @Resource
    CameraService cameraService;


    // 获取摄像头传递过来的参数
    @PostMapping("/attributes")
    @Operation(
            summary = "获取摄像头传递过来的参数"
    )
    public void getAttributes(@RequestBody CameraTemperatureModel cameraTemperatureModel){
        List<Map<String,String>> list = new LinkedList<>();

        List<Thermometry> thermometryList = cameraTemperatureModel.getThermometry();
        for(int i=0;i<thermometryList.size();i++){
            Map<String,String> map = new HashMap<>();
            map.put("indexCode", thermometryList.get(i).getTargetAttrs().getCameraIndexCode());     // 设备编码
            map.put("level", String.valueOf(thermometryList.get(i).getAlarmLevel() +1 ));               // 告警等级  1-预警，2-报警
            map.put("temperature", thermometryList.get(i).getCurTemperature());                     // 温度值
            map.put("dateTime", sdf.format(cameraTemperatureModel.getDateTime()));
            list.add(map);
        }

        // 根据摄像头编码查询该摄像头信息
        for(int i=0;i<list.size();i++){
            Camera camera = cameraService.queryTerminalIdByCode(list.get(i).get("indexCode"));
            // 判断是否告警
            if(Double.parseDouble(list.get(i).get("temperature")) >= camera.getAlarmValue()){
                // 向告警表插入数据
                CameraAlert cameraAlert = new CameraAlert();
                cameraAlert.setLineId(camera.getPartitionId());
                cameraAlert.setTerminalId(camera.getTerminalId());
                cameraAlert.setContent(camera.getPartitionId() + "号接头的摄像头" + camera.getTerminalId() + "的温度告警,值为：" + list.get(i).get("temperature"));
                cameraAlert.setAlertData(list.get(i).get("temperature"));
                try {
                    cameraAlert.setAlertDate(sdf.parse(list.get(i).get("dateTime")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cameraAlert.setIsConfirm(false);
                cameraAlert.setAlertType(list.get(i).get("level"));
                // 插入告警数据
                cameraService.saveAlarmData(cameraAlert);
            }
            // 更新设备表的最后数据的时间
            cameraService.updateDeviceDate(camera.getTerminalId(), list.get(i).get("dateTime"));
        }

    }


    // 根据设备ID获取摄像头的视频URL
    @GetMapping("/urls")
    @Operation(
            summary = "根据设备IP获取摄像头的视频URL",
            parameters = {
                    @Parameter(name = "terminalId", description = "设备ID")
            }
    )
    public ResponseFormat geuUrls(String terminalId){
        try{
            LinkedList<Object> linkedList = new LinkedList<>();
            LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();

            // 根据设备ID查询该摄像头的配置
            Camera camera = cameraService.getCameraConfig(terminalId);
            if(null == camera){
                return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "没有该摄像头的配置"));
            }
            // 根据设备IP获取摄像头的视频URL
            if(null != camera.getCameraOne()){
                String resultOne = GetCameraPreviewURLUtil.GetCameraPreviewURL(camera.getCameraOne()).replace("192.168.1.201", "192.168.4.179");
                linkedHashMap.put("channelOne", analysisUrl(resultOne));    // 一号通道视频URL
                linkedHashMap.put("name", camera.getName());
            }
            if(null != camera.getCameraTwo()){
                String resultTwo = GetCameraPreviewURLUtil.GetCameraPreviewURL(camera.getCameraTwo()).replace("192.168.1.201", "192.168.4.179");
                linkedHashMap.put("channelTwo", analysisUrl(resultTwo));    // 二号通道视频URL
                linkedHashMap.put("name", camera.getName());
            }
            linkedHashMap.put("terminalId", camera.getTerminalId());
            linkedHashMap.put("partitionId", camera.getPartitionId());
            linkedHashMap.put("typeName", camera.getTypeName());
            linkedHashMap.put("name", camera.getName());
            linkedList.add(linkedHashMap);

            return ResponseFormat.success("成功", linkedList);
        } catch (Exception e){
            e.printStackTrace();
        }
        return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));

    }


    // 根据分区ID查询该分区下所有摄像头的视频URL
    @GetMapping("/partitionurls")
    @Operation(
            summary = "根据分区ID查询该分区下所有摄像头的视频URL",
            parameters = {
                    @Parameter(name = "partitionId", description = "分区ID")
            }
    )
    public ResponseFormat getUrlsByPartitionId(String partitionId){
        try{
            LinkedList<Object> linkedList = new LinkedList<>();

            // 根据分区ID查询该分区下所有摄像头
            List<Camera> cameraList = cameraService.getAllCamera(partitionId);
            // 分别获取所有摄像头的视频URL
            for(int i=0;i<cameraList.size();i++){
                LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap<>();
                Camera camera = cameraList.get(i);

                if(null == camera){
                    return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "没有该摄像头的配置"));
                }

                if(null != camera.getCameraOne() & !"".equals(camera.getCameraOne())){
                    String resultOne = GetCameraPreviewURLUtil.GetCameraPreviewURL(camera.getCameraOne());
                    linkedHashMap.put("channelOne", analysisUrl(resultOne));    // 一号通道视频URL
                    linkedHashMap.put("name", camera.getName());
                }
                if(null != camera.getCameraTwo()  & !"".equals(camera.getCameraTwo())){
                    String resultTwo = GetCameraPreviewURLUtil.GetCameraPreviewURL(camera.getCameraTwo());
                    System.out.println(resultTwo);
                    linkedHashMap.put("channelTwo", analysisUrl(resultTwo));    // 二号通道视频URL
                    linkedHashMap.put("name", camera.getName());
                }
                linkedHashMap.put("terminalId", camera.getTerminalId());
                linkedHashMap.put("typeName", cameraList.get(i).getTypeName());
                linkedHashMap.put("name", camera.getName());
                linkedList.add(linkedHashMap);
            }
            return ResponseFormat.success("成功", linkedList);
        } catch (Exception e){
            System.out.println("异常：" + e.getStackTrace());
            e.printStackTrace();
        }
        return ResponseFormat.error(new CustomException(CustomExceptionType.QUERY_ERROR, "查询失败"));
    }



    // 解析出URL
    private String analysisUrl(String result){
        Map jsonMap = null;
        try {
            jsonMap = mapper.readValue(result, Map.class); //json转换成map
            result = jsonMap.get("data").toString();        // 转换为字符串准分截取
            return result.substring(5,result.length()-1);     // 截取出url
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
