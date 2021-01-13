package com.example.timer.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timer.model.TimerEntity;
import com.example.timer.payload.TimerRenderResponse;
import com.example.timer.payload.TimerResponse;
import com.example.timer.repository.TimerRepository;

@Service
public class TimerService {
	
	private static List<TimerEntity> timers = new ArrayList<>();
	
	@Autowired
	private TimerRepository repository;
	
//	public TimerService(TimerRepository repository) {
//		super();
//		this.repository = repository;
//	}
	
	public TimerEntity createNewTimer(long startValue, long stepTime) {
		TimerEntity timerEntity = new TimerEntity(UUID.randomUUID().toString(), startValue, LocalDateTime.now(), stepTime, startValue, LocalDateTime.now());
		timers.add(timerEntity);
		return repository.save(timerEntity);
	}
	
	public TimerEntity updateCounterValue(String id) {
		TimerEntity timerEntity = repository.findById(id).orElseThrow(() -> new RuntimeException("No entry found for id: " + id));
		timerEntity.setCounterValue(timerEntity.getCounterValue() + 1);
		return repository.save(timerEntity);
	}
	
	public TimerResponse getTimerById(String id) {
		TimerEntity timerEntity = repository.findById(id).orElseThrow(() -> new RuntimeException("No entry found for id: " + id));
		return new TimerResponse(timerEntity.getCounterValue(), timerEntity.getStepTime());
	}
	
	public List<TimerResponse> getAllTimers() {
		List<TimerResponse> timerResponses = new ArrayList<>();
		repository.findAll().forEach((TimerEntity timer) -> {
			timerResponses.add(new TimerResponse(timer.getCounterValue(), timer.getStepTime()));
		});
		return timerResponses;
	}
	
	public void deleteTimerById(String id) {
		repository.deleteById(id);
	}
	
	public List<TimerRenderResponse> getRenderedTimers() {
		List<TimerRenderResponse> renderResponses = new ArrayList<>();
		repository.findAll().forEach((TimerEntity timer) -> {
			renderResponses.add(new TimerRenderResponse(timer.getId(), timer.getCounterValue()));
		});
		return renderResponses;
	}
	
	public String renderTimer() {
        List<TimerEntity> all = repository.findAll();
        StringBuilder stringBuilder =
                new StringBuilder("<html>\n" +
                        "<body>\n" +
                        "\n" +
                        "<table style=\"width:100%\">\n" +
                        "  <tr>\n" +
                        "    <th>Id</th>\n" +
                        "    <th>Current Counter Value</th> \n" +
                        "  </tr>\n");

        all.forEach(timerEntity -> {
            stringBuilder.append(_render(timerEntity));
        });

        stringBuilder.append("</table>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>");
        return stringBuilder.toString();
    }

    public String _render(TimerEntity timerEntity) {
        return "<tr><td>" + timerEntity.getId() + "</td><td>" + timerEntity.getCounterValue()
                + "</td></tr>";
    }
    
    public TimerResponse pauseTimer(String id) {
    	TimerEntity timerEntity = repository.findById(id).orElseThrow(() -> new RuntimeException("No entry found for id: " + id));
    	timerEntity.setModifiedAt(LocalDateTime.now());
	repository.save(timerEntity);
    	return new TimerResponse(timerEntity.getCounterValue(), timerEntity.getStepTime());
    }
}
