package cn.zll.tqyb.entity;

public class ZhiShu {
	private String name;
	private String value;
	private String detail;

	public ZhiShu() {
		super();
	}

	public ZhiShu(String name, String value, String detail) {
		super();
		this.name = name;
		this.value = value;
		this.detail = detail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
