package com.daribear.PrefyBackend.Report;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class ReportService {
    @Autowired
    private ReportRepository reportRepo;

    public void saveReport(Report report){
        reportRepo.save(report);
    }
}
