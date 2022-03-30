package com.threatgator.usermanagement.repository;

import com.threatgator.usermanagement.model.Assets;
import com.threatgator.usermanagement.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetsRepository extends JpaRepository<Assets,Integer> {
    List<Assets> findByAdmin(int admin);

}
