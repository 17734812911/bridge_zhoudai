package com.xtw.bridge.model;

/**
 * User: Mr.Chen
 * Date: 2021/8/27
 * Description: No Description
 */

import lombok.Data;

@Data
public class MaxValue {
    private String sd;		// 湿度
    private String wd;		// 温度
    private String yq;		// 氧气
    private String eyht;	// 一氧化碳
    private String lhq;		// 硫化氢
    private String jw;		// 甲烷
    private String bpwd;	// 表皮温度
    private String wzjf;	// 外置局放
    private String gxcw;	// 光纤测温
}
