package com.daribear.PrefyBackend.Report;

import com.daribear.PrefyBackend.Posts.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


    @Query("SELECT r FROM Report r WHERE r.active = TRUE")
    Optional<List<Report>> findActiveReports(Pageable pageable);
}
