package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userBrandon = new User("bb@example.com", "bb2022", "Brandon", "Bocek");
		userBrandon.addRole(roleAdmin);
		
		User savedUser = repo.save(userBrandon);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User userRyan = new User("ryan@gmail.com", "ryan22", "Ryan", "Reagan");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		userRyan.addRole(roleEditor);
		userRyan.addRole(roleAssistant);
		
		User savedUser = repo.save(userRyan);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userBran = repo.findById(1).get();
		System.out.println(userBran);
		assertThat(userBran).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userBran = repo.findById(1).get();
		userBran.setEnabled(true);
		userBran.setEmail("bb@gmail.com");
		repo.save(userBran);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userRyan = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesPerson = new Role(2);
		userRyan.getRoles().remove(roleEditor);
		userRyan.addRole(roleSalesPerson);
		repo.save(userRyan);
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
}
