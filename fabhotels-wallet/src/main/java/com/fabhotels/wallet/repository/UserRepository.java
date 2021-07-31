package com.fabhotels.wallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fabhotels.wallet.models.User;
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	/*@Query("SELECT u FROM User u WHERE u.email=:email")
	User findUserByEmail(@Param("email") String email);

*/}
