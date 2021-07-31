package com.fabhotels.wallet.exception;
import com.fabhotels.wallet.models.User;

public class UserAlreadyHasWalletException extends Exception {
	public UserAlreadyHasWalletException(User user) {
		super("Customer "+user.getFname()+" "+user.getLname()+" already owns a wallet : "+user.getWallet().getWalletId());
	}
}
