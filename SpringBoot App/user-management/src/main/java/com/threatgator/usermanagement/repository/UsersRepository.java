package com.threatgator.usermanagement.repository;

import org.springframework.stereotype.Repository;
import com.threatgator.usermanagement.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
}
