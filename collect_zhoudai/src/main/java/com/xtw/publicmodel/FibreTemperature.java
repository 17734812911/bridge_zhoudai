package com.xtw.publicmodel;

/**
 * 	光纤测温最大值实体类
 * @author Mr.Chen
 * 2021年8月27日
 */
public class FibreTemperature {
	private double maxValue;

	public FibreTemperature() {
		super();
	}

	public FibreTemperature(double maxValue) {
		super();
		this.maxValue = maxValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	@Override
	public String toString() {
		return "FibreTemperature [maxValue=" + maxValue + "]";
	}

}
