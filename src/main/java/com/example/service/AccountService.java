package com.example.service;

import java.util.*;
import java.util.stream.Collectors;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



@Service
public class AccountService {
    
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    //Get All Accounts
    public List<Account> getAllAccounts(){
        Iterable<Account> allAccounts = accountRepository.findAll();
        List<Account> allAccountsList = new ArrayList();
        allAccounts.forEach(allAccountsList::add);
        return allAccountsList;
    }
    
    //Add Account
    public Account addAccount(Account newAccount){
       return accountRepository.save(newAccount);
    }
    //Get Account by ID
    public Account getAccountByID (int account_ID){
        Optional<Account> person = getAllAccounts().stream()
                                                    .filter(a -> a.getAccount_id() == account_ID)
                                                    .findFirst();
        return person.isPresent()? person.get() : null;
        
    }

    //Get Account by Username
    public Account getAccountByUsername(String account_username){
        Optional<Account> person = accountRepository.findByUsername(account_username)
                                                    .stream()
                                                    .findFirst();
        return person.isPresent() ? person.get(): null;
    }
    //Login
    public Account login(String account_username, String account_password){
        Account loginAccount = getAccountByUsername(account_username);
        if (loginAccount == null){
            return null;
        }
        if (loginAccount.getPassword().equals(account_password)){
            return loginAccount;
        } else {
            return null;
        }
    }
    
}
