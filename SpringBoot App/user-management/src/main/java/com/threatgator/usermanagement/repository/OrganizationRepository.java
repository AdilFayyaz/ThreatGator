package com.threatgator.usermanagement.repository;

import com.threatgator.usermanagement.model.Organization;
import org.springframework.stereotype.Repository;
import com.threatgator.usermanagement.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


// Users interface extending JPA Repo - Hibernate
@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Integer> {
}
