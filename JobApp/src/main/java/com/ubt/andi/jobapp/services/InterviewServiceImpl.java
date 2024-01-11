package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.Interview;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.repositories.InterviewRepository;
import org.springframework.stereotype.Service;
@Service
public class InterviewServiceImpl implements InterviewService {
    private final InterviewRepository interviewRepository;
    public InterviewServiceImpl(InterviewRepository interviewRepository){
        this.interviewRepository=interviewRepository;
    }
    @Override
    public void createInterview(Interview interview, Job job, Profile applicantProfile) {
        if(interview == null || job == null || applicantProfile == null) return;
        interview.setJob(job);
        interview.setProfile(applicantProfile);
        interviewRepository.save(interview);
    }

    @Override
    public Interview findInterviewById(Long id) {
        if(id == 0) return null;
        return interviewRepository.findById(id).get();
    }

    @Override
    public Interview findInterviewByProfileAndJob(Profile profile, Job job) {
        if(profile == null || job == null) return null;
        return interviewRepository.findByProfileAndJob(profile,job);
    }
}
