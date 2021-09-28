package com.xtw.publicmodel;

/**
 * 	所有设备最大值实体类
 * @author Mr.Chen
 * 2021年8月27日
 */
public class DeviceMaxValue {
	private String id;
	private String sd;		// 湿度
	private String wd;		// 温度
	private String yq;		// 氧气
	private String eyht;	// 二氧化碳
	private String lhq;		// 硫化氢
	private String jw;		// 甲烷
	private String bpwd;	// 表皮温度
	
	private String wzjf;	// 外置局放
	private String gxcw;	// 光纤测温
	
	public DeviceMaxValue() {
		super();
	}

	public DeviceMaxValue(String id, String sd, String wd, String yq, String eyht, String lhq, String jw, String bpwd,
			String wzjf, String gxcw) {
		super();
		this.id = id;
		this.sd = sd;
		this.wd = wd;
		this.yq = yq;
		this.eyht = eyht;
		this.lhq = lhq;
		this.jw = jw;
		this.bpwd = bpwd;
		this.wzjf = wzjf;
		this.gxcw = gxcw;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSd() {
		return sd;
	}

	public void setSd(String sd) {
		this.sd = sd;
	}

	public String getWd() {
		return wd;
	}

	public void setWd(String wd) {
		this.wd = wd;
	}

	public String getYq() {
		return yq;
	}

	public void setYq(String yq) {
		this.yq = yq;
	}

	public String getEyht() {
		return eyht;
	}

	public void setEyht(String eyht) {
		this.eyht = eyht;
	}

	public String getLhq() {
		return lhq;
	}

	public void setLhq(String lhq) {
		this.lhq = lhq;
	}

	public String getJw() {
		return jw;
	}

	public void setJw(String jw) {
		this.jw = jw;
	}

	public String getBpwd() {
		return bpwd;
	}

	public void setBpwd(String bpwd) {
		this.bpwd = bpwd;
	}

	public String getWzjf() {
		return wzjf;
	}

	public void setWzjf(String wzjf) {
		this.wzjf = wzjf;
	}

	public String getGxcw() {
		return gxcw;
	}

	public void setGxcw(String gxcw) {
		this.gxcw = gxcw;
	}

	@Override
	public String toString() {
		return "DeviceMaxValue [id=" + id + ", sd=" + sd + ", wd=" + wd + ", yq=" + yq + ", eyht=" + eyht + ", lhq="
				+ lhq + ", jw=" + jw + ", bpwd=" + bpwd + ", wzjf=" + wzjf + ", gxcw=" + gxcw + "]";
	}

}
