package cn.zll.tqyb.entity;

import java.util.ArrayList;
import java.util.List;

public class Weather {
	private String date;
	private String high;
	private String low;

	public Weather() {
		super();
	}

	public Weather(String date, String high, String low) {
		super();
		this.date = date;
		this.high = high;
		this.low = low;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	@Override
	public String toString() {
		return "Weather [date=" + date + ", high=" + high + ", low=" + low
				+ "]";
	}

}
