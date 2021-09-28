package com.xtw.publicmodel;

/**
 * 	设备配置参数类
 * @author Mr.Chen
 * 2021年7月5日
 */
public class DeviceConfig {

	private Integer id;
	private String terminalId;		// 从机ID
	private Integer	deviceTypeId;	// 设备类型ID
	private String deviceIp;		// 从机IP
	private String port;		// 通信端口
	private Integer partitionId;
	private Integer intervalSlave;		// 从机间访问间隔时间（秒）
	private Integer retryCount;			// 失败重试次数
	private Integer retryInterval;		// 失败后重发次数中的时间间隔（秒）
	private Integer pollingInterval;	// 多久采集一遍数据（秒）
	public DeviceConfig() {
		super();
	}
	public DeviceConfig(Integer id, String terminalId, Integer deviceTypeId, String deviceIp,
			String port, Integer partitionId, Integer intervalSlave, Integer retryCount, Integer retryInterval,
			Integer pollingInterval) {
		super();
		this.id = id;
		this.terminalId = terminalId;
		this.deviceTypeId = deviceTypeId;
		this.deviceIp = deviceIp;
		this.port = port;
		this.partitionId = partitionId;
		this.intervalSlave = intervalSlave;
		this.retryCount = retryCount;
		this.retryInterval = retryInterval;
		this.pollingInterval = pollingInterval;
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
	public Integer getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(Integer deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public String getDeviceIp() {
		return deviceIp;
	}
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public Integer getPartitionId() {
		return partitionId;
	}
	public void setPartitionId(Integer partitionId) {
		this.partitionId = partitionId;
	}
	public Integer getIntervalSlave() {
		return intervalSlave;
	}
	public void setIntervalSlave(Integer intervalSlave) {
		this.intervalSlave = intervalSlave;
	}
	public Integer getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	public Integer getRetryInterval() {
		return retryInterval;
	}
	public void setRetryInterval(Integer retryInterval) {
		this.retryInterval = retryInterval;
	}
	public Integer getPollingInterval() {
		return pollingInterval;
	}
	public void setPollingInterval(Integer pollingInterval) {
		this.pollingInterval = pollingInterval;
	}
	@Override
	public String toString() {
		return "DeviceConfig [id=" + id + ", terminalId=" + terminalId + ", deviceTypeId=" + deviceTypeId
				+ ", deviceIp=" + deviceIp + ", port=" + port + ", partitionId=" + partitionId + ", intervalSlave="
				+ intervalSlave + ", retryCount=" + retryCount + ", retryInterval=" + retryInterval
				+ ", pollingInterval=" + pollingInterval + "]";
	}
	
}
