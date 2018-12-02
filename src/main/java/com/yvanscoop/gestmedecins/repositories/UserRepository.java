package com.yvanscoop.gestmedecins.repositories;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.yvanscoop.gestmedecins.entities.User;

public interface UserRepository extends JpaRepository<User,Long> {
	@Query("select u from User u where u.username like :username")
	List<User> findByUser(@Param("username") String username);

	@Query("select u from User u where u.username = :username")
    User getByUsername(@Param("username") String name);
	
	@Modifying
	@Transactional
	@Query("update User u set u.active = false where u.username = ?1")
	void desactivateUser(String username);
	
	@Modifying
	@Transactional
	@Query("update User u set u.active = true where u.username = ?1")
	void activateUser(String username);

}
