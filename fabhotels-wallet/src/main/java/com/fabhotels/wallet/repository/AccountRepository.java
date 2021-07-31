package com.fabhotels.wallet.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fabhotels.wallet.models.Account;


public interface AccountRepository extends CrudRepository<Account, Integer> {

    @Query("SELECT a FROM Account  a WHERE a.accountNumber=:accountNumber")
    Iterable<Account> findAccountByNumber(@Param("accountNumber") Integer accountNumber);
}
