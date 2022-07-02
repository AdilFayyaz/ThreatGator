package com.threatgator.usermanagement.repository;

import com.threatgator.usermanagement.model.Assets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetsRepository extends JpaRepository<Assets,Integer> {
    List<Assets> findByOrganizationId(Integer organization_id);

}
