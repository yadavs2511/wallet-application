package com.fabhotels.wallet.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fabhotels.wallet.exception.*;
import com.fabhotels.wallet.models.*;
import com.fabhotels.wallet.services.WalletService;

import java.util.*;

@RestController
public class WalletController {
	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	WalletService walletService;

	/* Create a new wallet for a user. 
	 * Constraint : A user can have only one wallet.
	 */
	@PostMapping("/api/createWallet/{userId}")
	public ResponseEntity<ServiceResponse> createWallet(@PathVariable("userId") int userId) throws UserAlreadyHasWalletException {

		ServiceResponse response = new ServiceResponse();

		try {
			Wallet newWallet = walletService.createWallet(userId);
			response.setStatus("200");
			response.setDescription("Wallet created successfully!");
			response.setData(newWallet);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}catch(UserDoesNotExistException e) {
			response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		catch(UserAlreadyHasWalletException e) {
			response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}

	}

	/* Return current account balance - balance in wallet
	 * wallet can have multiple accounts.
	 * pass account number as account id and wallet holder id as wallet id
	 */

	@GetMapping("/api/wallet/{walletId}/account/{accountId}/getAccountBalance")
	public  ResponseEntity<ServiceResponse>  getAccountBalance (
			@PathVariable("walletId") int walletId,
			@PathVariable("accountId") int accountId){



		ServiceResponse response = new ServiceResponse();

		try {
			Float balance = walletService.getAccountBalanceForCurrentWallet(walletId, accountId);
			response.setStatus("200");
			response.setDescription("Balance fetched successfully!!");
			response.setData(balance);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}catch(WalletIdDoesNotExistException e) {
			response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		catch(AccountNotAssociatedWithWalletException e) {
			response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}

	}
	//controller to add amount to the wallet it accept walletId(wallet holder id),accountNumber and amount 
	@PostMapping("/api/wallet/{walletId}/account/{accountId}/addAmount/{amount}")
	public ResponseEntity<ServiceResponse> addAmmount(@PathVariable("walletId") int walletId,
			@PathVariable("accountId") int accountId,
			@PathVariable("amount") float amount) {


		ServiceResponse response = new ServiceResponse();

		try {
			Account ac = walletService.depositToAccount(walletId, accountId, amount, "ADDAMOUNT");
			response.setStatus("200");
			response.setDescription("Amount "+ amount+ " deposited successfully!!");
			response.setData(ac);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}catch(WalletIdDoesNotExistException e) {
			response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		catch(AccountNotAssociatedWithWalletException e) {
			response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}

	/*Controller to transfer amount from one wallet to another wallet
	 * @param fromWalletId : wallet from which amount to be transfered.
	 * @param toWalletId : wallet to which amount to be transfered.
	 * @param fromAccountId : account from which amount to be transfered.
	 * @param toAccountId : account to which amount to be transfered.
	 * @param amount : amount to be transfered.
	 */
	@PostMapping("/api/wallet/{fromWalletId}/account/{fromAccountId}/transfer/wallet/{toWalletId}/account/{toAccountId}/amount/{amount}")
	public ResponseEntity<ServiceResponse> transfer(@PathVariable ("fromWalletId") int fromWalletId,
			@PathVariable ("fromAccountId") int fromAccountId,
			@PathVariable ("toWalletId") int toWalletId,
			@PathVariable ("toAccountId") int toAccountId,
			@PathVariable ("amount") float amount) throws WalletIdDoesNotExistException,
	AccountNotAssociatedWithWalletException, InsufficientBalanceInWalletException {

		ServiceResponse response = new ServiceResponse();

		try {
			walletService.transferToAccount(fromWalletId, fromAccountId,toWalletId, toAccountId, amount);
			response.setStatus("200");
			response.setDescription("Amount "+ amount+ " transferred successfully!!");
			response.setData(amount);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}catch(WalletIdDoesNotExistException e) {
			response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		catch(AccountNotAssociatedWithWalletException e) {
			response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
		catch(InsufficientBalanceInWalletException e) {
			response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}


	}

	/* This Controller is used for fetching last n transactions.
	 * @param accountId: account id of account.
	 * @param walletId : wallet id.
	 * @param n: number of transactions.
	 */

	@GetMapping("/api/wallet/{walletId}/account/{accountId}/getLastNTransactions/{n}")
	public ResponseEntity<ServiceResponse> getStatement(@PathVariable ("walletId") int walletId,
			@PathVariable ("accountId") int accountId,
			@PathVariable ("n") int n) {

		ServiceResponse response = new ServiceResponse();

		try {
			List<BankTransaction> lb = walletService.getStatement(walletId, accountId, n);
			response.setStatus("200");
			response.setDescription("Statement fetched successfully!!");
			response.setData(lb);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}catch(WalletIdDoesNotExistException e) {
			response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		catch(AccountNotAssociatedWithWalletException e) {
			response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
			response.setDescription(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}

	}

}
