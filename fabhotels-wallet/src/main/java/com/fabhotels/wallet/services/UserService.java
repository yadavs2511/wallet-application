package com.fabhotels.wallet.services;

import com.fabhotels.wallet.exception.PasswordIncorrectException;
import com.fabhotels.wallet.exception.UserDoesNotExistException;
import com.fabhotels.wallet.models.User;

public interface UserService {
     public User userLogin(int userId,String password) throws UserDoesNotExistException, PasswordIncorrectException;
}
