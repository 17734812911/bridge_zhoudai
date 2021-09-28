package com.xtw.outdischarge.model;

import java.util.Date;

/**
 * 	外置局放实体类
 * @author Mr.Chen
 * 2021年7月12日
 */
public class OutPartial {
	private Integer id;
	private String terminalId;
	private String aElectric;		// A相放电波形
	private String aFrequency;		// A相放电频次
	private Integer aMaxElectric;	// A相最大放电量
	private Integer aMaxFrequency;	// A相最大放电频次
	private String bElectric;		// B相放电波形
	private String bFrequency;		// B相放电频次
	private Integer bMaxElectric;	// B相最大放电量
	private Integer bMaxFrequency;	// A相最大放电频次
	private String cElectric;		// C相放电波形
	private String cFrequency;		// C相放电频次
	private Integer cMaxElectric;	// C相最大放电量
	private Integer cMaxFrequency;	// A相最大放电频次
	private Date createTime;		// 时间
	private Integer aStatus;
	private Integer bStatus;
	private Integer cStatus;
	public OutPartial() {
		super();
	}
	public OutPartial(Integer id, String terminalId, String aElectric, String aFrequency, Integer aMaxElectric,
			Integer aMaxFrequency, String bElectric, String bFrequency, Integer bMaxElectric, Integer bMaxFrequency,
			String cElectric, String cFrequency, Integer cMaxElectric, Integer cMaxFrequency, Date createTime,
			Integer aStatus, Integer bStatus, Integer cStatus) {
		super();
		this.id = id;
		this.terminalId = terminalId;
		this.aElectric = aElectric;
		this.aFrequency = aFrequency;
		this.aMaxElectric = aMaxElectric;
		this.aMaxFrequency = aMaxFrequency;
		this.bElectric = bElectric;
		this.bFrequency = bFrequency;
		this.bMaxElectric = bMaxElectric;
		this.bMaxFrequency = bMaxFrequency;
		this.cElectric = cElectric;
		this.cFrequency = cFrequency;
		this.cMaxElectric = cMaxElectric;
		this.cMaxFrequency = cMaxFrequency;
		this.createTime = createTime;
		this.aStatus = aStatus;
		this.bStatus = bStatus;
		this.cStatus = cStatus;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getaElectric() {
		return aElectric;
	}
	public void setaElectric(String aElectric) {
		this.aElectric = aElectric;
	}
	public String getaFrequency() {
		return aFrequency;
	}
	public void setaFrequency(String aFrequency) {
		this.aFrequency = aFrequency;
	}
	public Integer getaMaxElectric() {
		return aMaxElectric;
	}
	public void setaMaxElectric(Integer aMaxElectric) {
		this.aMaxElectric = aMaxElectric;
	}
	public Integer getaMaxFrequency() {
		return aMaxFrequency;
	}
	public void setaMaxFrequency(Integer aMaxFrequency) {
		this.aMaxFrequency = aMaxFrequency;
	}
	public String getbElectric() {
		return bElectric;
	}
	public void setbElectric(String bElectric) {
		this.bElectric = bElectric;
	}
	public String getbFrequency() {
		return bFrequency;
	}
	public void setbFrequency(String bFrequency) {
		this.bFrequency = bFrequency;
	}
	public Integer getbMaxElectric() {
		return bMaxElectric;
	}
	public void setbMaxElectric(Integer bMaxElectric) {
		this.bMaxElectric = bMaxElectric;
	}
	public Integer getbMaxFrequency() {
		return bMaxFrequency;
	}
	public void setbMaxFrequency(Integer bMaxFrequency) {
		this.bMaxFrequency = bMaxFrequency;
	}
	public String getcElectric() {
		return cElectric;
	}
	public void setcElectric(String cElectric) {
		this.cElectric = cElectric;
	}
	public String getcFrequency() {
		return cFrequency;
	}
	public void setcFrequency(String cFrequency) {
		this.cFrequency = cFrequency;
	}
	public Integer getcMaxElectric() {
		return cMaxElectric;
	}
	public void setcMaxElectric(Integer cMaxElectric) {
		this.cMaxElectric = cMaxElectric;
	}
	public Integer getcMaxFrequency() {
		return cMaxFrequency;
	}
	public void setcMaxFrequency(Integer cMaxFrequency) {
		this.cMaxFrequency = cMaxFrequency;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getaStatus() {
		return aStatus;
	}
	public void setaStatus(Integer aStatus) {
		this.aStatus = aStatus;
	}
	public Integer getbStatus() {
		return bStatus;
	}
	public void setbStatus(Integer bStatus) {
		this.bStatus = bStatus;
	}
	public Integer getcStatus() {
		return cStatus;
	}
	public void setcStatus(Integer cStatus) {
		this.cStatus = cStatus;
	}
	@Override
	public String toString() {
		return "OutPartial [id=" + id + ", terminalId=" + terminalId + ", aElectric=" + aElectric + ", aFrequency="
				+ aFrequency + ", aMaxElectric=" + aMaxElectric + ", aMaxFrequency=" + aMaxFrequency + ", bElectric="
				+ bElectric + ", bFrequency=" + bFrequency + ", bMaxElectric=" + bMaxElectric + ", bMaxFrequency="
				+ bMaxFrequency + ", cElectric=" + cElectric + ", cFrequency=" + cFrequency + ", cMaxElectric="
				+ cMaxElectric + ", cMaxFrequency=" + cMaxFrequency + ", createTime=" + createTime + ", aStatus="
				+ aStatus + ", bStatus=" + bStatus + ", cStatus=" + cStatus + "]";
	}
	
}
