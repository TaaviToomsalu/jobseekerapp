package com.taavi.jobseekerapp.repository;

import com.taavi.jobseekerapp.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}