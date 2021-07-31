package com.fabhotels.wallet.exception;

public class WalletIdDoesNotExistException extends  Exception {
	public WalletIdDoesNotExistException(int walletId) {
		super("Wallet with walletId : "+walletId+" does not exist");
	}
}
