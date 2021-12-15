package com.threatgator.source_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.threatgator.source_management.model.Source;

public interface SourceRepository extends JpaRepository<Source, Integer> {
}