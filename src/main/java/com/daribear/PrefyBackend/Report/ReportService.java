package com.daribear.PrefyBackend.Report;


import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationRepository;
import com.daribear.PrefyBackend.IncomeClasses.DefaultIncomePageable;
import com.daribear.PrefyBackend.Security.ApplicationUserRole;
import com.google.firebase.database.core.Repo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;

@Service
@Component
public class ReportService {
    @Autowired
    private ReportRepository reportRepo;





    public void saveReport(Report report){
        reportRepo.save(report);
    }

    public ArrayList<Report> getReports(DefaultIncomePageable defaultIncomePageable){
        Pageable pageable = createPageable(defaultIncomePageable.getPageNumber(), defaultIncomePageable.getLimit());
        return (ArrayList<Report>) reportRepo.findActiveReports(pageable).get();

    }

    private Pageable createPageable(Integer pageNumber, Integer limit){
        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by("creationDate").descending());
        return pageable;
    }



}
