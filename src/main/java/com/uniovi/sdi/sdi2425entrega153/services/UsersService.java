package com.uniovi.sdi.sdi2425entrega153.services;

import java.util.*;

import org.passay.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.uniovi.sdi.sdi2425entrega153.entities.*;
import com.uniovi.sdi.sdi2425entrega153.repositories.UsersRepository;

import javax.annotation.PostConstruct;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsersService(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void init() {
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        usersRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUser(Long id) {
        return usersRepository.findById(id).get();
    }

    public User getUserByDni(String dni) {
        return usersRepository.findByDni(dni);
    }

    public void addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }

    public String generateUserPassword() {
        PasswordGenerator generator = new PasswordGenerator();

        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase, 2);
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 2);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.Digit, 2);
        CharacterRule specialRule = new CharacterRule(new CharacterData() {
            public String getErrorCode() { return "INSUFFICIENT_SPECIAL"; }
            public String getCharacters() { return "!@#$%^&*()-_=+[]{}|;:,.<>?"; }
        }, 2);

        return generator.generatePassword(12, Arrays.asList(upperCaseRule, lowerCaseRule, digitRule, specialRule));
    }
}
