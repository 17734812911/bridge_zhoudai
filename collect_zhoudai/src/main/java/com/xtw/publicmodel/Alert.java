package com.xtw.publicmodel;

import java.util.Date;

public class Alert {
	private Integer lineId;
	private String terminalId;
	private String content;
	private String alertData;
	private Date alertDate;
	private Date alertEnterDate;
	private boolean isconfirm;
	private String alertType;
	public Alert() {
		super();
	}

	public Alert(Integer lineId, String terminalId, String content, String alertData, Date alertDate, Date alertEnterDate, boolean isconfirm, String alertType) {
		this.lineId = lineId;
		this.terminalId = terminalId;
		this.content = content;
		this.alertData = alertData;
		this.alertDate = alertDate;
		this.alertEnterDate = alertEnterDate;
		this.isconfirm = isconfirm;
		this.alertType = alertType;
	}

	public Integer getLineId() {
		return lineId;
	}

	public void setLineId(Integer lineId) {
		this.lineId = lineId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAlertData() {
		return alertData;
	}

	public void setAlertData(String alertData) {
		this.alertData = alertData;
	}

	public Date getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}

	public Date getAlertEnterDate() {
		return alertEnterDate;
	}

	public void setAlertEnterDate(Date alertEnterDate) {
		this.alertEnterDate = alertEnterDate;
	}

	public boolean isIsconfirm() {
		return isconfirm;
	}

	public void setIsconfirm(boolean isconfirm) {
		this.isconfirm = isconfirm;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	@Override
	public String toString() {
		return "Alert{" +
				"lineId=" + lineId +
				", terminalId='" + terminalId + '\'' +
				", content='" + content + '\'' +
				", alertData='" + alertData + '\'' +
				", alertDate=" + alertDate +
				", alertEnterDate=" + alertEnterDate +
				", isconfirm=" + isconfirm +
				", alertType='" + alertType + '\'' +
				'}';
	}
}
