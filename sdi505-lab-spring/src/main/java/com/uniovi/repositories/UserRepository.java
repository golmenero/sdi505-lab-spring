package com.uniovi.repositories;

import com.uniovi.entities.*;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByDni(String dni);
}