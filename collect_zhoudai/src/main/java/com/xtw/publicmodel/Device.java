package com.xtw.publicmodel;

public class Device {
	private Integer id;
	private String name;
	private String terminalId;
	private Integer lineId;
	private String joint;
	private DeviceConfig deviceConfig;
	public Device() {
		super();
	}
	public Device(Integer id, String name, String terminalId, Integer lineId, String joint, DeviceConfig deviceConfig) {
		super();
		this.id = id;
		this.name = name;
		this.terminalId = terminalId;
		this.lineId = lineId;
		this.joint = joint;
		this.deviceConfig = deviceConfig;
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
	public Integer getLineId() {
		return lineId;
	}
	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}
	public String getJoint() {
		return joint;
	}
	public void setJoint(String joint) {
		this.joint = joint;
	}
	public DeviceConfig getDeviceConfig() {
		return deviceConfig;
	}
	public void setDeviceConfig(DeviceConfig deviceConfig) {
		this.deviceConfig = deviceConfig;
	}
	@Override
	public String toString() {
		return "Device [id=" + id + ", name=" + name + ", terminalId=" + terminalId + ", lineId=" + lineId + ", joint="
				+ joint + ", deviceConfig=" + deviceConfig + "]";
	}
	
}
