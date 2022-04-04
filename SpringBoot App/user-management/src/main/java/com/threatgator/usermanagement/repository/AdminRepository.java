package com.threatgator.usermanagement.repository;

import org.springframework.stereotype.Repository;
import com.threatgator.usermanagement.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

// Admin interface extending JPA Repo - Hibernate
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{
}
