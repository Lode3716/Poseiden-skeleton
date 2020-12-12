package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

@Log
@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User save(User user) {
        if(existsByUsername(user.getUsername()))
        {
            log.log(Level.WARNING,"This user name is already exiting");
            throw new EntityExistsException("This user name is already exiting");
        }else{
         return userRepository.save(user);
        }
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> update(User user) {
        return Optional.empty();
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    @Override
    public boolean exist(User user) {
        return userRepository.existsById(user.getId());
    }

    public boolean existsByUsername(String userName) {
        return userRepository.existsByUsername(userName);
    }
}

