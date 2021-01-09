package com.example.timer.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Timer")
public class TimerEntity {
	
	@Id
	private String id;
	@Column
	private long counterValue;
	@Column
	private LocalDateTime creationTime;
	@Column
	private long stepTime;
	@Column
	private long startValue;
	@Column
	private LocalDateTime modifiedAt;
	
	public TimerEntity() {
		super();
	}

	public TimerEntity(String id, long counterValue, LocalDateTime creationTime, long stepTime, long startValue,
			LocalDateTime modifiedAt) {
		super();
		this.id = id;
		this.counterValue = counterValue;
		this.creationTime = creationTime;
		this.stepTime = stepTime;
		this.startValue = startValue;
		this.modifiedAt = modifiedAt;
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

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public long getStepTime() {
		return stepTime;
	}

	public void setStepTime(long stepTime) {
		this.stepTime = stepTime;
	}

	public long getStartValue() {
		return startValue;
	}

	public void setStartValue(long startValue) {
		this.startValue = startValue;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
