package com.fabhotels.wallet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fabhotels.wallet.exception.PasswordIncorrectException;
import com.fabhotels.wallet.exception.UserDoesNotExistException;
import com.fabhotels.wallet.models.ServiceResponse;
import com.fabhotels.wallet.models.User;
import com.fabhotels.wallet.repository.UserRepository;
import com.fabhotels.wallet.services.UserService;

@RestController
public class UserController {
	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserRepository userRepository;

	@Autowired
	private UserService userService;

	/*This controller is for fetching the user details by Id.
	 *@param userId : User Id To fetched
	 */
	@GetMapping("/api/findUser/{userId}")
	public User findUserById(@PathVariable("userId") int id) {

		return userRepository.findById(id).orElse(null);
	}

	/*This controller is used for creating a new User. If user already exists with userId then it will update the same. 
	 * User Object is passed as requestBody where user userId and Email should be unique.
	 */

	@PostMapping("/api/createUser")
	public User createUser(@RequestBody User user) {

		User existingUser = userRepository.findById(user.getUserId()).orElse(null);
		try {
			if(existingUser ==null){
				LOGGER.info("CREATE !! User created successfully for userId: {}" + user.getUserId());
				return userRepository.save(user);  
			} else{
				LOGGER.info("UPDATE !! User updated successfully for userId: {}" + user.getUserId());
				return userRepository.save(user);  
			}
		}catch(Exception e){
			LOGGER.error("ERROR !! upsert failed for userId: {}. Failed reason: {}" +user.getUserId(),e.getCause());
			return null;
		}
	}

	/*This controller is used for Login Purpose. if user is present and password is matching with given userId's password then it will allow to Show the details of USer. 
	 * @param userId : userId of user
	 * @param password : password of user
	 */
	@GetMapping("/api/userLogin")
	public ResponseEntity<ServiceResponse> userLogin(@RequestParam("userId") int userId, @RequestParam("password") String password) {
		ServiceResponse response = new ServiceResponse();

		try {
			User user = userService.userLogin(userId,password);
			response.setStatus("200");
			response.setDescription("User Logged in successfully!");
			response.setData(user);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (UserDoesNotExistException u) {
			response.setStatus(String.valueOf(HttpStatus.NOT_FOUND));
			response.setDescription(u.getMessage());
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}catch (PasswordIncorrectException p) {
			response.setStatus(String.valueOf(HttpStatus.EXPECTATION_FAILED));
			response.setDescription(p.getMessage());
			return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
		}
	}

}
