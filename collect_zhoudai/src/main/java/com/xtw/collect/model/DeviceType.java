package com.xtw.collect.model;

/**
 * 	设备类型实体类
 * @author Mr.Chen
 * 2021年8月25日
 */
public class DeviceType {
	private Integer deviceTypeId;
	private String name;
	private String communicationMode;
	public DeviceType() {
		super();
	}
	public DeviceType(Integer deviceTypeId, String name, String communicationMode) {
		super();
		this.deviceTypeId = deviceTypeId;
		this.name = name;
		this.communicationMode = communicationMode;
	}
	public Integer getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(Integer deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCommunicationMode() {
		return communicationMode;
	}
	public void setCommunicationMode(String communicationMode) {
		this.communicationMode = communicationMode;
	}
	@Override
	public String toString() {
		return "DeviceType [deviceTypeId=" + deviceTypeId + ", name=" + name + ", communicationMode="
				+ communicationMode + "]";
	}
}
