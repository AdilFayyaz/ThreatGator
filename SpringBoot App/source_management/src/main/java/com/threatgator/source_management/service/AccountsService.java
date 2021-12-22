package com.threatgator.source_management.service;


import com.threatgator.source_management.model.Account;

import java.util.List;

// Interface class list all functions of Accounts
public interface AccountsService {
    public Account saveAccount(Account source);
    public List<Account> getAllAccounts ();
    public String deleteAccount(int id);
}
