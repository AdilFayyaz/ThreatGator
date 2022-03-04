package com.threatgator.source_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.threatgator.source_management.model.Source;
// Interface class extends JPA - hibernate
public interface SourceRepository extends JpaRepository<Source, Integer> {
}