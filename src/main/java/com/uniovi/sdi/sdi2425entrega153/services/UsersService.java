package com.uniovi.sdi.sdi2425entrega153.services;

import java.util.*;
import java.util.stream.Collectors;

import org.passay.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<User> getUsers(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }

    public List<User> getUsers() {
        return (List<User>) usersRepository.findAll();
    }

    public List<User> getStandardUsers() {
        return ((List<User>) usersRepository.findAll()).stream().filter(u -> u.getRole().equals("ROLE_STANDARD")).collect(Collectors.toList());
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

    public void editUser(User user) {
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

    public void changePassword(User user) {
        String newPassword = bCryptPasswordEncoder.encode(user.getPassword());
        usersRepository.updatePasswordByDni(user.getDni(), newPassword);
    }

    public boolean checkValidPassword(String password) {
        if(password.length() < 12) return false;

        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>-_].*");

        return hasLower && hasUpper && hasDigit && hasSpecial;
    }
}
