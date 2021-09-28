package com.xtw.collect.model;

/**
 * 	通道配置实体类
 * @author Mr.Chen
 * 2021年7月5日
 */
public class ChannelAttribute {
	
	private Integer id;
	private String name;		// 通道名称
	private String terminalId;		// 从机ID
	private String channelId;	// 通道ID
	private String channelType;	// 通道类型
	private Integer rangeUp;		// 量程上限
	private Integer rangeLow;	// 量程下限
	private String warningValue;	// 预警值
	private String alarmUp;		// 报警值
	private String criticalAlarm;	// 严重报警值
	private Double offsetValue;		// 偏移量
	private boolean isUse;			// 通道是否使用
	public ChannelAttribute() {
		super();
	}

	public ChannelAttribute(Integer id, String name, String terminalId, String channelId, String channelType, Integer rangeUp, Integer rangeLow, String warningValue, String alarmUp, String criticalAlarm, Double offsetValue, boolean isUse) {
		this.id = id;
		this.name = name;
		this.terminalId = terminalId;
		this.channelId = channelId;
		this.channelType = channelType;
		this.rangeUp = rangeUp;
		this.rangeLow = rangeLow;
		this.warningValue = warningValue;
		this.alarmUp = alarmUp;
		this.criticalAlarm = criticalAlarm;
		this.offsetValue = offsetValue;
		this.isUse = isUse;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public Integer getRangeUp() {
		return rangeUp;
	}

	public void setRangeUp(Integer rangeUp) {
		this.rangeUp = rangeUp;
	}

	public Integer getRangeLow() {
		return rangeLow;
	}

	public void setRangeLow(Integer rangeLow) {
		this.rangeLow = rangeLow;
	}

	public String getWarningValue() {
		return warningValue;
	}

	public void setWarningValue(String warningValue) {
		this.warningValue = warningValue;
	}

	public String getAlarmUp() {
		return alarmUp;
	}

	public void setAlarmUp(String alarmUp) {
		this.alarmUp = alarmUp;
	}

	public String getCriticalAlarm() {
		return criticalAlarm;
	}

	public void setCriticalAlarm(String criticalAlarm) {
		this.criticalAlarm = criticalAlarm;
	}

	public Double getOffsetValue() {
		return offsetValue;
	}

	public void setOffsetValue(Double offsetValue) {
		this.offsetValue = offsetValue;
	}

	public boolean isUse() {
		return isUse;
	}

	public void setUse(boolean use) {
		isUse = use;
	}

	@Override
	public String toString() {
		return "ChannelAttribute{" +
				"id=" + id +
				", name='" + name + '\'' +
				", terminalId='" + terminalId + '\'' +
				", channelId='" + channelId + '\'' +
				", channelType='" + channelType + '\'' +
				", rangeUp=" + rangeUp +
				", rangeLow=" + rangeLow +
				", warningValue='" + warningValue + '\'' +
				", alarmUp='" + alarmUp + '\'' +
				", criticalAlarm='" + criticalAlarm + '\'' +
				", offsetValue=" + offsetValue +
				", isUse=" + isUse +
				'}';
	}
}