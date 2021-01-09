package com.example.timer.payload;

public class TimerResponse {
	
	private long counterValue;
	private long stepTime;
	
	public TimerResponse(long counterValue, long stepTime) {
		super();
		this.counterValue = counterValue;
		this.stepTime = stepTime;
	}
	public long getCounterValue() {
		return counterValue;
	}
	public void setCounterValue(long counterValue) {
		this.counterValue = counterValue;
	}
	public long getStepTime() {
		return stepTime;
	}
	public void setStepTime(long stepTime) {
		this.stepTime = stepTime;
	}
}
