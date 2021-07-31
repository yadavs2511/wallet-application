package com.fabhotels.wallet.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements Serializable{
	@Id //to set as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // to set as autoincrement
    private int userId;
    private String fname;
    private String lname;
    @Column(unique=true)
    private String email;

    private String password;
    
    @OneToOne(mappedBy = "walletOfUser")
    @JsonIgnore
    private Wallet wallet;

    @OneToMany(mappedBy = "accountHolder")
    @JsonIgnore
    private List<Account> userAccounts;


    public User() {
        super();
    }

    public User(String fname, String lname, String email ) {
        super();
        this.fname = fname;
        this.lname = lname;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public List<Account> getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(List<Account> userAccounts) {
        this.userAccounts = userAccounts;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
