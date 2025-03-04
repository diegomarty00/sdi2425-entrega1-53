package com.uniovi.sdi.sdi2425entrega153.repositories;

import com.uniovi.sdi.sdi2425entrega153.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {

    User findByDni(String dni);
}