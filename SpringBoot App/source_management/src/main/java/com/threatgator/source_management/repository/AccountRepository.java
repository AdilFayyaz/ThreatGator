package com.threatgator.source_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.threatgator.source_management.model.Account;

public interface AccountRepository extends JpaRepository<Account,Integer>
{
}
