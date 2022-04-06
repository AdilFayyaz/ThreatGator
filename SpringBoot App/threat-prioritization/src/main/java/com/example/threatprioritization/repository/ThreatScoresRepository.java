package com.example.threatprioritization.repository;

import com.example.threatprioritization.model.ThreatScores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


// Users interface extending JPA Repo - Hibernate
@Repository
public interface ThreatScoresRepository extends JpaRepository<ThreatScores,Integer> {
    public List<ThreatScores> findByOrganizationId(Integer organization_id);
    public List<ThreatScores> findByOrganizationIdAndReportId(Integer organization_id, Integer report_id);
    public boolean existsThreatScoresByOrganizationIdAndReportId(Integer organization_id, Integer report_id);
}
