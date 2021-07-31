package com.fabhotels.wallet.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fabhotels.wallet.exception.UserDoesNotExistException;

import com.fabhotels.wallet.exception.PasswordIncorrectException;
import com.fabhotels.wallet.models.User;
import com.fabhotels.wallet.repository.UserRepository;

@Service("UserService")
public class UserServiceImpl implements UserService{
	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Override
	public User userLogin(int userId, String password) throws UserDoesNotExistException,PasswordIncorrectException {

		User user = userRepository.findById(userId).orElse(null);
		if (user == null) {
			throw new UserDoesNotExistException(userId);
		}
		User u = new User();
		if(password.equals(user.getPassword())){
			u.setFname(user.getFname());
			u.setLname(user.getLname());
			u.setUserId(user.getUserId());
			u.setEmail(user.getEmail());
			return u; 
		}else {
			throw new PasswordIncorrectException(userId);
		}
	}

}
