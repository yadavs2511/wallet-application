package com.fabhotels.wallet.services;

import java.util.List;

import com.fabhotels.wallet.exception.*;
import com.fabhotels.wallet.models.Account;
import com.fabhotels.wallet.models.BankTransaction;
import com.fabhotels.wallet.models.Wallet;

public interface WalletService {

    public Wallet createWallet(Integer customerId) throws UserDoesNotExistException, UserAlreadyHasWalletException;
    
    public Float getAccountBalanceForCurrentWallet(Integer walledId, Integer accountId) throws WalletIdDoesNotExistException, AccountNotAssociatedWithWalletException;
    
    public Account withdrawFromAccount(Integer walletId, Integer accountId, Float amount, String type) throws WalletIdDoesNotExistException,
            AccountNotAssociatedWithWalletException, InsufficientBalanceInWalletException;
    
    public Account depositToAccount(Integer walletId, Integer accountId, Float amount, String type) throws WalletIdDoesNotExistException,
            AccountNotAssociatedWithWalletException;
    
    public void transferToAccount(Integer fromWalletId, Integer fromAccountId, Integer toWalletId, Integer toAccountId, Float amount) throws WalletIdDoesNotExistException,
    AccountNotAssociatedWithWalletException, InsufficientBalanceInWalletException;
    
    public List<BankTransaction> getStatement(Integer walletId, Integer accountId, Integer n) throws WalletIdDoesNotExistException,
            AccountNotAssociatedWithWalletException;

}
