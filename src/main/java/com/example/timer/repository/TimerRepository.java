package com.example.timer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.timer.model.TimerEntity;

@Repository
public interface TimerRepository extends JpaRepository<TimerEntity, String>{

}
