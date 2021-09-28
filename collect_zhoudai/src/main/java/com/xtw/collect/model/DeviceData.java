package com.xtw.collect.model;

import java.util.Date;

/**
 * 	设备采集数据实体类
 * @author Mr.Chen
 * 2021年7月5日
 */
public class DeviceData {
	private Integer id;
	private String terminalId;
	private String terminalIp;
	private String passageOne;
	private String passageTwo;
	private String passageThree;
	private String passageFour;
	private String passageFive;
	private String passageSix;
	private String passageSeven;
	private String passageEight;
	private String passageNine;
	private String passageTen;
	private Date insertTime;
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
	public String getTerminalIp() {
		return terminalIp;
	}
	public void setTerminalIp(String terminalIp) {
		this.terminalIp = terminalIp;
	}
	public String getPassageOne() {
		return passageOne;
	}
	public void setPassageOne(String passageOne) {
		this.passageOne = passageOne;
	}
	public String getPassageTwo() {
		return passageTwo;
	}
	public void setPassageTwo(String passageTwo) {
		this.passageTwo = passageTwo;
	}
	public String getPassageThree() {
		return passageThree;
	}
	public void setPassageThree(String passageThree) {
		this.passageThree = passageThree;
	}
	public String getPassageFour() {
		return passageFour;
	}
	public void setPassageFour(String passageFour) {
		this.passageFour = passageFour;
	}
	public String getPassageFive() {
		return passageFive;
	}
	public void setPassageFive(String passageFive) {
		this.passageFive = passageFive;
	}
	public String getPassageSix() {
		return passageSix;
	}
	public void setPassageSix(String passageSix) {
		this.passageSix = passageSix;
	}
	public String getPassageSeven() {
		return passageSeven;
	}
	public void setPassageSeven(String passageSeven) {
		this.passageSeven = passageSeven;
	}
	public String getPassageEight() {
		return passageEight;
	}
	public void setPassageEight(String passageEight) {
		this.passageEight = passageEight;
	}
	public String getPassageNine() {
		return passageNine;
	}
	public void setPassageNine(String passageNine) {
		this.passageNine = passageNine;
	}
	public String getPassageTen() {
		return passageTen;
	}
	public void setPassageTen(String passageTen) {
		this.passageTen = passageTen;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public DeviceData(Integer id, String terminalId, String terminalIp,
			String passageOne, String passageTwo, String passageThree, String passageFour, String passageFive,
			String passageSix, String passageSeven, String passageEight, String passageNine, String passageTen,
			Date insertTime) {
		super();
		this.id = id;
		this.terminalId = terminalId;
		this.terminalIp = terminalIp;
		this.passageOne = passageOne;
		this.passageTwo = passageTwo;
		this.passageThree = passageThree;
		this.passageFour = passageFour;
		this.passageFive = passageFive;
		this.passageSix = passageSix;
		this.passageSeven = passageSeven;
		this.passageEight = passageEight;
		this.passageNine = passageNine;
		this.passageTen = passageTen;
		this.insertTime = insertTime;
	}
	public DeviceData() {
		super();
	}
	@Override
	public String toString() {
		return "DeviceData [id=" + id + ", terminalId=" + terminalId + ", terminalIp=" + terminalIp + ", passageOne=" + passageOne
				+ ", passageTwo=" + passageTwo + ", passageThree=" + passageThree + ", passageFour=" + passageFour
				+ ", passageFive=" + passageFive + ", passageSix=" + passageSix + ", passageSeven=" + passageSeven
				+ ", passageEight=" + passageEight + ", passageNine=" + passageNine + ", passageTen=" + passageTen
				+ ", insertTime=" + insertTime + "]";
	}
	
}
