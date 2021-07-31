package com.fabhotels.wallet.exception;

import com.fabhotels.wallet.models.User;
import com.fabhotels.wallet.models.Wallet;

public class WalletDoesNotBelongToCustomer extends Exception {
	public WalletDoesNotBelongToCustomer(User user, Wallet wallet) {
		super("Customer with id"+user.getUserId()+" does not have associated walletId : "+wallet.getWalletId());
	}
}
