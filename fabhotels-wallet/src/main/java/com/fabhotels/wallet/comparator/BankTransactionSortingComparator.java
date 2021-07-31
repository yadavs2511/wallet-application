package com.fabhotels.wallet.comparator;

import java.util.Comparator;

import com.fabhotels.wallet.models.BankTransaction;

public class BankTransactionSortingComparator implements Comparator<BankTransaction> {

    public int compare(BankTransaction t1, BankTransaction t2) {
        return t2.getTimestamp().compareTo(t1.getTimestamp());
    }
}
