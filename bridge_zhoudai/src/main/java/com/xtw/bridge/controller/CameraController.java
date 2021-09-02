package com.xtw.bridge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xtw.bridge.model.Camera;
import com.xtw.bridge.model.CameraAlert;
import com.xtw.bridge.model.CameraModel;
import com.xtw.bridge.myexception.CustomException;
import com.xtw.bridge.myexception.CustomExceptionType;
import com.xtw.bridge.myexception.ResponseFormat;
import com.xtw.bridge.service.CameraService;
import com.xtw.bridge.utils.GetCameraPreviewURLUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    ObjectMapper mapper = new ObjectMapper();
    @Resource
    CameraService cameraService;


    // 获取摄像头传递过来的参数
    @GetMapping("/attributes")
    @Operation(
            summary = "获取摄像头传递过来的参数"
    )
    public void getAttributes(String body){

        try{
            Date recvTime = null;               // 时间
            String cameraIndexCode = null;      // 摄像头编码
            String curTemperature = "0.0";      // 温度值
            // 转换为实体类
            CameraModel cameraModel = mapper.readValue(body, CameraModel.class);

            if(cameraModel.getRecvTime() != null){
                recvTime = cameraModel.getRecvTime();   // 时间
            }
            if(cameraModel.getThermometry().get(0).getTargetAttrs().getCameraIndexCode() != null && !("".equals(cameraModel.getThermometry().get(0).getTargetAttrs().getCameraIndexCode()))){
                cameraIndexCode = cameraModel.getThermometry().get(0).getTargetAttrs().getCameraIndexCode();    // 摄像头编码
            }
            if(cameraModel.getThermometry().get(0).getCurTemperature() != null && !("".equals(cameraModel.getThermometry().get(0).getCurTemperature()))){
                curTemperature = cameraModel.getThermometry().get(0).getCurTemperature();   // 温度值
            }
            // 根据摄像头编码查询该摄像头信息
            Camera camera = cameraService.queryTerminalIdByCode(cameraIndexCode);
            // 判断是否告警
            if(Double.parseDouble(curTemperature) >= camera.getAlarmValue()){
                // 向告警表插入数据
                CameraAlert cameraAlert = new CameraAlert();
                cameraAlert.setLineId(camera.getPartitionId());
                cameraAlert.setTerminalId(camera.getTerminalId());
                cameraAlert.setContent(camera.getPartitionId() + "号接头的摄像头" + camera.getTerminalId() + "的温度告警");
                cameraAlert.setAlertData(curTemperature);
                cameraAlert.setAlertDate(recvTime);
                cameraAlert.setIsConfirm(false);
                // 插入告警数据
                cameraService.saveAlarmData(cameraAlert);
            }
            // 更新设备表的最后数据的时间
            cameraService.updateDeviceDate(camera.getTerminalId(), String.valueOf(recvTime));

        } catch (JsonMappingException e) {
            System.out.println("JSON转换实体类异常");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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
                String resultOne = GetCameraPreviewURLUtil.GetCameraPreviewURL(camera.getCameraOne());
                linkedHashMap.put("channelOne", analysisUrl(resultOne));    // 一号通道视频URL
            }
            if(null != camera.getCameraTwo()){
                String resultTwo = GetCameraPreviewURLUtil.GetCameraPreviewURL(camera.getCameraTwo());
                linkedHashMap.put("channelTwo", analysisUrl(resultTwo));    // 二号通道视频URL
            }
            linkedHashMap.put("terminalId", camera.getTerminalId());
            linkedHashMap.put("partitionId", camera.getPartitionId());
            linkedHashMap.put("typeName", camera.getTypeName());
            linkedList.add(linkedHashMap);

            return ResponseFormat.success("成功", linkedList);
        } catch (Exception e){
            System.out.println("异常：" + e.getMessage());
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
                    continue;
                }

                if(null != camera.getCameraOne()){
                    String resultOne = GetCameraPreviewURLUtil.GetCameraPreviewURL(camera.getCameraOne());
                    linkedHashMap.put("channelOne", analysisUrl(resultOne));    // 一号通道视频URL
                }
                if(null != camera.getCameraTwo()){
                    String resultTwo = GetCameraPreviewURLUtil.GetCameraPreviewURL(camera.getCameraTwo());
                    linkedHashMap.put("channelTwo", analysisUrl(resultTwo));    // 二号通道视频URL
                }
                linkedHashMap.put("terminalId", camera.getTerminalId());
                linkedHashMap.put("typeName", cameraList.get(i).getTypeName());
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
