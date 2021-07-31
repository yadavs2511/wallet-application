package com.fabhotels.wallet.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fabhotels.wallet.comparator.BankTransactionSortingComparator;
import com.fabhotels.wallet.constant.Constants;
import com.fabhotels.wallet.exception.*;
import com.fabhotels.wallet.models.Account;
import com.fabhotels.wallet.models.BankTransaction;
import com.fabhotels.wallet.models.User;
import com.fabhotels.wallet.models.Wallet;
import com.fabhotels.wallet.repository.AccountRepository;
import com.fabhotels.wallet.repository.BankTansactionRepository;
import com.fabhotels.wallet.repository.UserRepository;
import com.fabhotels.wallet.repository.WalletRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service("walletService")
public class WalletServiceImpl implements WalletService{
	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WalletRepository walletRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BankTansactionRepository bankTansactionRepository;

	@Override
	public Wallet createWallet(Integer userId) throws UserDoesNotExistException, UserAlreadyHasWalletException {

		User user = userRepository.findById(userId).orElse(null);

		if (user == null) {
			throw new UserDoesNotExistException(userId);
		}
		if (user.getWallet() != null) {
			throw new UserAlreadyHasWalletException(user);
		}

		Wallet wallet = new Wallet();

		wallet.setWalletOfUser(user);
		if (user.getUserAccounts()!=null && !user.getUserAccounts().isEmpty()) {
			wallet.setAccountsInWallet(new ArrayList<>(user.getUserAccounts()));
		}
		return  walletRepository.save(wallet);
	}

	@Override
	public Float getAccountBalanceForCurrentWallet(Integer walletId, Integer accountId) throws WalletIdDoesNotExistException, AccountNotAssociatedWithWalletException {

		Wallet wallet = walletRepository.findById(walletId).orElse(null);

		// handle walletId does not exist
		if (wallet==null) {
			throw new WalletIdDoesNotExistException(walletId);
		}
		List<Account> a =  wallet.getAccountsInWallet().stream().filter(aa -> aa.getAccountNumber() == accountId).collect(Collectors.toList());
		if (a.isEmpty()) {
			throw new AccountNotAssociatedWithWalletException(walletId, accountId);
		}

		return a.get(0).getBalance();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
	public Account withdrawFromAccount(Integer walletId, Integer accountId, Float amount, String type) throws WalletIdDoesNotExistException,
	AccountNotAssociatedWithWalletException, InsufficientBalanceInWalletException {

		Wallet wallet = walletRepository.findById(walletId).orElse(null);

		// handle walletId does not exist
		if (wallet==null) {
			throw new WalletIdDoesNotExistException(walletId);
		}
		List<Account> associateAccount =  wallet.getAccountsInWallet().stream().filter(aa -> aa.getAccountNumber() == accountId).collect(Collectors.toList());;
		if (associateAccount.isEmpty()) {
			throw new AccountNotAssociatedWithWalletException(walletId, accountId);
		}
		if (associateAccount.get(0).getBalance() < amount) {
			throw new InsufficientBalanceInWalletException(walletId);
		}

		float currentBalance = associateAccount.get(0).getBalance();
		associateAccount.get(0).setBalance(currentBalance - amount);

		Account ac = accountRepository.save(associateAccount.get(0));
		// bank transactions not required to be shown in this case, hence setting them null
		ac.setBankTransactions(null);

		// Make Entry in Transaction table
		if ("WITHDRAW".equals(type)) {
			makeEntryInTransaction(Constants.WITHDRAW, amount, ac.getBalance(), Constants.WITHDRAW_DESCRIPTION, ac);
		}
		return ac;
	}

	/**
	 * Method is used to make entry into BankTransaction table for the appropriate transaction - deposit, withdrawl or transfer
	 * @param amount : Amount to be deposited || withdrawn || transferred
	 * @param postBalance : Balance in account after transaction has occurred
	 * @param description : Custom String description associated with deposit || withdrawl || transfer
	 * @param associatedAccount : Account associated with the transaction
	 */
	private void makeEntryInTransaction(String typeOfTransaction, float amount, float postBalance, String description, Account associatedAccount) {
		BankTransaction bankTransaction = new BankTransaction(typeOfTransaction, new Date(), amount, postBalance, description, associatedAccount);

		bankTansactionRepository.save(bankTransaction);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
	public Account depositToAccount(Integer walletId, Integer accountId, Float amount, String type) throws WalletIdDoesNotExistException,
	AccountNotAssociatedWithWalletException {

		Wallet wallet = walletRepository.findById(walletId).orElse(null);

		// handle walletId does not exist
		if (wallet==null) {
			throw new WalletIdDoesNotExistException(walletId);
		}
		List<Account> associateAccount =  wallet.getAccountsInWallet().stream().filter(aa -> aa.getAccountNumber() == accountId).collect(Collectors.toList());;
		if (associateAccount.isEmpty()) {
			throw new AccountNotAssociatedWithWalletException(walletId, accountId);
		}

		float currentBalance = associateAccount.get(0).getBalance();
		associateAccount.get(0).setBalance(currentBalance + amount);

		Account ac = accountRepository.save(associateAccount.get(0));
		// bank transactions not required to be shown in this case, hence setting them null
		ac.setBankTransactions(null);

		// Make Entry in Transaction table

		if ("DEPOSIT".equals(type)) {
			makeEntryInTransaction(Constants.DEPOSIT, amount, ac.getBalance(), Constants.DEPOSIT_DESCRIPTION, ac);
		}else if ("ADDAMOUNT".equals(type)) {
			makeEntryInTransaction(Constants.ADDAMOUNT, amount, ac.getBalance(), Constants.ADDAMOUNT_DESCRIPTION, ac);
		}

		return ac;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {AccountNotAssociatedWithWalletException.class, WalletIdDoesNotExistException.class, Exception.class})
	public void transferToAccount(Integer fromWalletId, Integer fromAccountId, Integer toWalletId, Integer toAccountId, Float amount) throws WalletIdDoesNotExistException,
	AccountNotAssociatedWithWalletException, InsufficientBalanceInWalletException {

		Wallet fromWallet = walletRepository.findById(fromWalletId).orElse(null);
		Wallet toWallet = walletRepository.findById(toWalletId).orElse(null);

		// handle walletId does not exist
		if (fromWallet == null || toWallet == null) {
			throw new WalletIdDoesNotExistException(fromWallet==null? fromWalletId: toWalletId);
		}

		List<Account> fromAssociateAccount =  fromWallet.getAccountsInWallet().stream().filter(aa -> aa.getAccountNumber() == fromAccountId).collect(Collectors.toList());
		List<Account> toAssociateAccount =  toWallet.getAccountsInWallet().stream().filter(aa -> aa.getAccountNumber() == toAccountId).collect(Collectors.toList());

		if (fromAssociateAccount.isEmpty()) {
			throw new AccountNotAssociatedWithWalletException(fromWalletId, fromAccountId);
		}
		if (toAssociateAccount.isEmpty()) {
			throw new AccountNotAssociatedWithWalletException(toWalletId, toAccountId);
		}

		// Withdraw
		Account fromAccount = this.withdrawFromAccount(fromWalletId,fromAccountId,amount,"TRANSFER");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
		.append("$")
		.append(amount)
		.append(" transferred to accountId : ")
		.append(toAccountId);
		makeEntryInTransaction(Constants.TRANSFER, amount, fromAccount.getBalance(), stringBuilder.toString(), fromAccount);


		// deposit
		Account toAccount = this.depositToAccount(toWalletId, toAccountId, amount,"TRANSFER");
		StringBuilder stringBuilder1 = new StringBuilder();
		stringBuilder1
		.append("$")
		.append(amount)
		.append(" transferred from accountId : ")
		.append(fromAccountId);
		makeEntryInTransaction(Constants.TRANSFER, amount, toAccount.getBalance(), stringBuilder1.toString(), toAccount);

	}

	@Override
	public List<BankTransaction> getStatement(Integer walletId, Integer accountId, Integer n) throws WalletIdDoesNotExistException,
	AccountNotAssociatedWithWalletException{

		Wallet wallet = walletRepository.findById(walletId).orElse(null);

		// handle walletId does not exist
		if (wallet==null) {
			throw new WalletIdDoesNotExistException(walletId);
		}
		List<Account> associateAccount =  wallet.getAccountsInWallet().stream().filter(aa -> aa.getAccountNumber() == accountId).collect(Collectors.toList());;
		if (associateAccount.isEmpty()) {
			throw new AccountNotAssociatedWithWalletException(walletId, accountId);
		}


		List<BankTransaction> bankTransactions = associateAccount.get(0).getBankTransactions();

		Collections.sort(bankTransactions, new BankTransactionSortingComparator());

		// handling length of last N transactions
		n = bankTransactions.size()>=n?n:bankTransactions.size();
		return bankTransactions.subList(0, n);

	}
}
