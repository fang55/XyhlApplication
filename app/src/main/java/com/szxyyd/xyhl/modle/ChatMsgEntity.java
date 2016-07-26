package com.szxyyd.xyhl.modle;
/**
 * 聊天信息
 */
public class ChatMsgEntity {
	private String date;
	private String name;
	private String text;
	 private int type; 
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	

}
