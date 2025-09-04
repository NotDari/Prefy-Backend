package com.daribear.PrefyBackend.Report;

import com.daribear.PrefyBackend.Posts.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The repository which handles the submission and retrieval of the Report Entity.
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * Finds a page(determined by pageable) of reports where they are active and have not been reviewed.
     *
     * @param pageable the page to retrieve
     * @return the List of Reports
     */
    @Query("SELECT r FROM Report r WHERE r.active = TRUE")
    Optional<List<Report>> findActiveReports(Pageable pageable);
}
