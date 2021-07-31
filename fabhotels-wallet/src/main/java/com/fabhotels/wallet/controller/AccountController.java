package com.fabhotels.wallet.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fabhotels.wallet.models.Account;
import com.fabhotels.wallet.repository.AccountRepository;
import com.fabhotels.wallet.repository.UserRepository;

@RestController
public class AccountController {
    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    /*This controller is for account creation.
     * Accept object of account 
     */
    @PostMapping("/api/createAccount")
    public Account createAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

}
