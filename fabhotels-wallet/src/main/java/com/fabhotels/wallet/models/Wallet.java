package com.fabhotels.wallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Wallet implements Serializable {

    @Id //to set as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // to set as autoincrement
    private int walletId;

    @OneToMany(mappedBy = "walletHolder")
    private List<Account> accountsInWallet;

    @OneToOne
    private User walletOfUser;

    private static final long serialVersionUID = 1L;

    public Wallet() {
    }


    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public List<Account> getAccountsInWallet() {
        return accountsInWallet;
    }

    public void setAccountsInWallet(List<Account> accountsInWallet) {
        this.accountsInWallet = accountsInWallet;
        for (Account account : accountsInWallet) {
            account.setWalletHolder(this);
        }
    }

    public User getWalletOfUser() {
        return walletOfUser;
    }

    public void setWalletOfUser(User walletOfUser) {

        this.walletOfUser = walletOfUser;
    }
}
