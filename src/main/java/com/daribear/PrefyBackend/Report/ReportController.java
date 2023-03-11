package com.daribear.PrefyBackend.Report;

import com.daribear.PrefyBackend.Authentication.Authentication;
import com.daribear.PrefyBackend.IncomeClasses.DefaultIncomePageable;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostIdList;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostListByCategory;
import com.daribear.PrefyBackend.IncomeClasses.IncomePostListById;
import com.daribear.PrefyBackend.Posts.Post;
import com.daribear.PrefyBackend.Posts.PostService;
import com.daribear.PrefyBackend.Posts.PostVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(path = "prefy/v1/Reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/SubmitReport")
    public void createNewPost(@RequestBody Report report){
        report.setActive(true);
        reportService.saveReport(report);
    }

}







