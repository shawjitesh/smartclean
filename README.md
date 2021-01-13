# smartclean
Smart Clean Technologies Round 1


## DB used -> H2(in- memory database)

## TimerController.java
```
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
          } catch(InterruptedException e) {
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
```

## TimerService.java
```
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
```

## TimerEntity.java
```
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
```

## TimerRepository.java
```
  package com.example.timer.repository;

  import org.springframework.data.jpa.repository.JpaRepository;
  import org.springframework.stereotype.Repository;

  import com.example.timer.model.TimerEntity;

  @Repository
  public interface TimerRepository extends JpaRepository<TimerEntity, String>{

  }
```

## TimerResponse.java
```
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
```

## TimerRenderResponse.java
```
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
```

## TimerApplication.java
```
  package com.example.timer;

  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;

  @SpringBootApplication
  public class TimerApplication {

    public static void main(String[] args) {
      SpringApplication.run(TimerApplication.class, args);
    }

  }
```

## application.properties
```
  logging.level.org.springframework.web= DEBUG

  spring.h2.console.enabled=true
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.datasource.driverClassName=org.h2.Driver
  spring.datasource.username=sa
  spring.datasource.password=password
  spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```
