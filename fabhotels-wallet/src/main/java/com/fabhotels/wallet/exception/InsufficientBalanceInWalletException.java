package com.fabhotels.wallet.exception;

public class InsufficientBalanceInWalletException extends Exception {

    public InsufficientBalanceInWalletException(int walletId) {
        super("Wallet with walletId : "+walletId+" does not have sufficient balance");
    }
}
