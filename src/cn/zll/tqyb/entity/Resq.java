package cn.zll.tqyb.entity;

import java.util.List;

public class Resq {
	private String city;
	private String updatetime;
	private String wendu;
	private String fengli;
	private String shidu;
	private String fengxiang;
	private String sunrise_1;
	private String sunset_1;
	private String sunrise_2;
	private String sunset_2;

	private Environment environment;
	private List<Weather> forecast;
	private List<ZhiShu> zhishu;

	public Resq() {
		super();
	}

	public Resq(String city, String updatetime, String wendu, String fengli,
			String shidu, String fengxiang, String sunrise_1, String sunset_1,
			String sunrise_2, String sunset_2) {
		super();
		this.city = city;
		this.updatetime = updatetime;
		this.wendu = wendu;
		this.fengli = fengli;
		this.shidu = shidu;
		this.fengxiang = fengxiang;
		this.sunrise_1 = sunrise_1;
		this.sunset_1 = sunset_1;
		this.sunrise_2 = sunrise_2;
		this.sunset_2 = sunset_2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getWendu() {
		return wendu;
	}

	public void setWendu(String wendu) {
		this.wendu = wendu;
	}

	public String getFengli() {
		return fengli;
	}

	public void setFengli(String fengli) {
		this.fengli = fengli;
	}

	public String getShidu() {
		return shidu;
	}

	public void setShidu(String shidu) {
		this.shidu = shidu;
	}

	public String getFengxiang() {
		return fengxiang;
	}

	public void setFengxiang(String fengxiang) {
		this.fengxiang = fengxiang;
	}

	public String getSunrise_1() {
		return sunrise_1;
	}

	public void setSunrise_1(String sunrise_1) {
		this.sunrise_1 = sunrise_1;
	}

	public String getSunset_1() {
		return sunset_1;
	}

	public void setSunset_1(String sunset_1) {
		this.sunset_1 = sunset_1;
	}

	public String getSunrise_2() {
		return sunrise_2;
	}

	public void setSunrise_2(String sunrise_2) {
		this.sunrise_2 = sunrise_2;
	}

	public String getSunset_2() {
		return sunset_2;
	}

	public void setSunset_2(String sunset_2) {
		this.sunset_2 = sunset_2;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public List<Weather> getForecast() {
		return forecast;
	}

	public void setForecast(List<Weather> forecast) {
		this.forecast = forecast;
	}

	public List<ZhiShu> getZhishu() {
		return zhishu;
	}

	public void setZhishu(List<ZhiShu> zhishu) {
		this.zhishu = zhishu;
	}

}
