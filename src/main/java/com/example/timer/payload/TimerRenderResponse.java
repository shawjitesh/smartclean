package com.example.timer.payload;

public class TimerRenderResponse {

	private String id;
	private long counterValue;
	
	public TimerRenderResponse(String id, long counterValue) {
		super();
		this.id = id;
		this.counterValue = counterValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCounterValue() {
		return counterValue;
	}

	public void setCounterValue(long counterValue) {
		this.counterValue = counterValue;
	}
}
