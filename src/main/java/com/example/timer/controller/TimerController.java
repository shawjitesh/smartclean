package com.example.timer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.timer.model.TimerEntity;
import com.example.timer.service.TimerService;

@RestController
@RequestMapping(value = "/v1/timers")
public class TimerController {

	@Autowired
	private TimerService timerService;
	
	@PostMapping("/_create")
	public ResponseEntity createNewTimer(@RequestParam long start, @RequestParam long step) {
		TimerEntity timerEntity = timerService.createNewTimer(start, step);
		Thread newTimerThread = new Thread(() -> {
			while(true) {
				timerService.updateCounterValue(timerEntity.getId());
				try {
					Thread.sleep(step * 1000L);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		});
		newTimerThread.start();
		return new ResponseEntity<>(timerEntity.getId(), HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/_check", params = "id")
	public ResponseEntity checkTimer(@RequestParam String id) {
		try {
			return new ResponseEntity<>(timerService.getTimerById(id), HttpStatus.OK);
		} catch(Exception exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/_check")
	public ResponseEntity checkAllTimers() {
		try {
			return new ResponseEntity<>(timerService.getAllTimers(), HttpStatus.OK);
		} catch(Exception exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/_render")
	public ResponseEntity renderTimer() {
		String renderTimer = timerService.renderTimer();
		return new ResponseEntity<>(renderTimer, HttpStatus.OK);
	}
	
	@DeleteMapping("/_clear")
	public ResponseEntity deleteTimer(@RequestParam String id) {
		timerService.deleteTimerById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/_pause")
	public ResponseEntity pauseTimer(@RequestParam String id) {
		return new ResponseEntity<>(timerService.pauseTimer(id), HttpStatus.OK);
	}
}
