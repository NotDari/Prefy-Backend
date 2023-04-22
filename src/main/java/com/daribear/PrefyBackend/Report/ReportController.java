package com.daribear.PrefyBackend.Report;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.Authentication.AuthenticationService;
import com.daribear.PrefyBackend.IncomeClasses.DefaultIncomePageable;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostIdList;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostListByCategory;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostListById;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Posts.PostService;
import com.daribear.PrefyBackend.Posts.PostVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "prefy/v1/Reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    Environment environment;


    @Autowired
    private AuthenticationService authService;

    @PostMapping("/SubmitReport")
    public void createReport(@RequestBody Report report){
        report.setActive(true);
        reportService.saveReport(report);
    }


    @GetMapping("/GetReport")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ArrayList<Report> getReport(DefaultIncomePageable defaultIncomePageable) throws UnknownHostException {
        return reportService.getReports(defaultIncomePageable);
    }

}







