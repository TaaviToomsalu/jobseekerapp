package com.taavi.jobseekerapp.service;

import com.taavi.jobseekerapp.entity.Job;
import com.taavi.jobseekerapp.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    // Lisa uus töökuulutus
    public Job addJob(Job job) {
        return jobRepository.save(job);
    }

    // Uuenda olemasolevat töökuulutust
    public Job updateJob(Long id, Job jobDetails) {
        Optional<Job> existingJob = jobRepository.findById(id);
        if (existingJob.isPresent()) {
            Job job = existingJob.get();
            job.setTitle(jobDetails.getTitle());
            job.setDescription(jobDetails.getDescription());
            job.setCompany(jobDetails.getCompany());
            job.setLocation(jobDetails.getLocation());
            job.setSalary(jobDetails.getSalary());
            job.setJobType(jobDetails.getJobType());
            return jobRepository.save(job);
        }
        return null;
    }

    // Kuvab kõik töökuulutused
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Kuvab töökuulutuse ID järgi
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    // Kustuta töökuulutus
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}
