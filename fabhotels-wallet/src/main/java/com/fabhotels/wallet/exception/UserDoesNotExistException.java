package com.fabhotels.wallet.exception;


public class UserDoesNotExistException extends Exception {

	public UserDoesNotExistException(Integer userId) {
		super("User with user ID:" + userId + " does not exist");
	}
	public UserDoesNotExistException(String userName) {
		super("User with user name:" + userName + " does not exist");
	}
}
