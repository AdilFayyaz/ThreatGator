package com.threatgator.source_management.service;

import com.threatgator.source_management.model.Account;
import com.threatgator.source_management.repository.AccountRepository;
import com.threatgator.source_management.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountsServiceImpl implements AccountsService{
    @Autowired
    private AccountRepository accountRepository;
    private SourceRepository sourceRepository;

    public AccountsServiceImpl(AccountRepository accountRepository, SourceRepository sourceRepository) {
        this.accountRepository = accountRepository;
        this.sourceRepository = sourceRepository;
    }

    @Override
    public Account saveAccount(Account account) {
        account.setSource(sourceRepository.findById(account.getSource_id()).get());
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts(){return accountRepository.findAll();}

    @Override
    public String deleteAccount(int id) {
        accountRepository.deleteById(id);
        return "{'message': 'Account deleted successfully'}";
    }

}
