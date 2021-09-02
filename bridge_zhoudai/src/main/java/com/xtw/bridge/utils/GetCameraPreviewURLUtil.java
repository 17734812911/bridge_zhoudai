package com.xtw.bridge.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Mr.Chen
 * Date: 2021/8/24
 * Description: 获取视频的 URL
 */
public class GetCameraPreviewURLUtil {

    public static String GetCameraPreviewURL(String indexCode) {

        ArtemisConfig.host = "192.168.100.191:443"; // artemis网关服务器ip端口
        ArtemisConfig.appKey = "22341657";  // 秘钥appkey
        ArtemisConfig.appSecret = "HcCxpYbQV4jLrlPYfQLR";// 秘钥appSecret

        /**
         * STEP2：设置OpenAPI接口的上下文
         */
        final String ARTEMIS_PATH = "/artemis";

        /**
         * STEP3：设置接口的URI地址
         */
        final String previewURLsApi = ARTEMIS_PATH + "/api/video/v2/cameras/previewURLs";
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", previewURLsApi);    //根据现场环境部署确认是http还是https
            }
        };

        /**
         * STEP4：设置参数提交方式
         */
        String contentType = "application/json";

        /**
         * STEP5：组装请求参数
         */
        ObjectMapper mapper = new ObjectMapper(); //转换器
        HashMap<String,String> map = new HashMap<>();
        map.put("cameraIndexCode", indexCode);     // 2 505496ce6c65466d8ea5c799432de290   // 1 65a4d558ba7b4f8ab5562db07ba8543c
        map.put("protocol", "hls");
        // map.put("streamType", "0");
        // map.put("transmode", "1");
        // map.put("expand", "streamform=ps");
        String body = null;     //map转json
        try {
            body = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        /**
         * STEP6：调用接口
         */
        return ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);// post请求application/json类型参数

    }
}
