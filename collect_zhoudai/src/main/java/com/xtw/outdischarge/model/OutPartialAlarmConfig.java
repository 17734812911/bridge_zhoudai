package com.xtw.outdischarge.model;

/**
 *	  外置局放报警配置实体类
 * @author Mr.Chen
 * 2021年7月28日
 */
public class OutPartialAlarmConfig {
	private Integer id;
	private Integer partitionId;	// 所属分区
	private String terminalId;		// 设备ID
	private double alarmValue;		// 报警阈值
	private double warningValue;	// 预警值
	private double criticalAlarm;	// 严重报警值
	private Integer alarmFrequencyValue;	// 放电频次报警值
	private double offsetValue;	// 偏移量
	public OutPartialAlarmConfig() {
		super();
	}

	public OutPartialAlarmConfig(Integer id, Integer partitionId, String terminalId, double alarmValue, double warningValue, double criticalAlarm, Integer alarmFrequencyValue, double offsetValue) {
		this.id = id;
		this.partitionId = partitionId;
		this.terminalId = terminalId;
		this.alarmValue = alarmValue;
		this.warningValue = warningValue;
		this.criticalAlarm = criticalAlarm;
		this.alarmFrequencyValue = alarmFrequencyValue;
		this.offsetValue = offsetValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(Integer partitionId) {
		this.partitionId = partitionId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public double getAlarmValue() {
		return alarmValue;
	}

	public void setAlarmValue(double alarmValue) {
		this.alarmValue = alarmValue;
	}

	public double getWarningValue() {
		return warningValue;
	}

	public void setWarningValue(double warningValue) {
		this.warningValue = warningValue;
	}

	public double getCriticalAlarm() {
		return criticalAlarm;
	}

	public void setCriticalAlarm(double criticalAlarm) {
		this.criticalAlarm = criticalAlarm;
	}

	public Integer getAlarmFrequencyValue() {
		return alarmFrequencyValue;
	}

	public void setAlarmFrequencyValue(Integer alarmFrequencyValue) {
		this.alarmFrequencyValue = alarmFrequencyValue;
	}

	public double getOffsetValue() {
		return offsetValue;
	}

	public void setOffsetValue(double offsetValue) {
		this.offsetValue = offsetValue;
	}

	@Override
	public String toString() {
		return "OutPartialAlarmConfig{" +
				"id=" + id +
				", partitionId=" + partitionId +
				", terminalId='" + terminalId + '\'' +
				", alarmValue=" + alarmValue +
				", warningValue=" + warningValue +
				", criticalAlarm=" + criticalAlarm +
				", alarmFrequencyValue=" + alarmFrequencyValue +
				", offsetValue=" + offsetValue +
				'}';
	}
}
