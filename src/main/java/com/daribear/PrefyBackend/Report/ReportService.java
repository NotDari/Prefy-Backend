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


/**
 * This is the service that handles the reports submissions/retrieval.
 */
@Service
@Component
public class ReportService {
    @Autowired
    private ReportRepository reportRepo;


    /**
     *  Saves a report to the database
     * @param report report to be saved
     */
    public void saveReport(Report report){
        reportRepo.save(report);
    }

    /**
     * Gets a list of reports for the given page.
     *
     * @param defaultIncomePageable pageable details, such as the given page
     * @return list of reports
     */
    public ArrayList<Report> getReports(DefaultIncomePageable defaultIncomePageable){
        Pageable pageable = createPageable(defaultIncomePageable.getPageNumber(), defaultIncomePageable.getLimit());
        return (ArrayList<Report>) reportRepo.findActiveReports(pageable).get();

    }

    /**
     * Creates a pageable given a pagenumber and a limit.
     * Orders the items by newest first.
     *
     * @param pageNumber pageNumber to be added
     * @param limit limit of items per page to be added
     * @return the created pageable
     */
    private Pageable createPageable(Integer pageNumber, Integer limit){
        Pageable pageable = PageRequest.of(pageNumber, limit, Sort.by("creationDate").descending());
        return pageable;
    }



}
