package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.exceptions.UserExistException;
import com.nnk.springboot.services.exceptions.UserNotFoundException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;

@Log
@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User save(User user) {
        if (existsByUsername(user.getUsername())) {
            log.log(Level.WARNING, "This user name is already exiting");
            throw new UserExistException("This user name is already exiting");
        } else {
            return userRepository.save(user);
        }
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(Integer id, User user) {

        User updateUser = existById(user.getId());
        updateUser.setPassword(user.getPassword());
        updateUser.setPassword(user.getUsername());
        updateUser.setFullname(user.getFullname());
        updateUser.setRole(user.getRole());
        return save(updateUser);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(existById(id).getId());
    }

    @Override
    public User readByid(Integer id) {
        return null;
    }


    public User existById(Integer id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("There is no user with this id"));
    }

    public boolean existsByUsername(String userName) {
        return userRepository.existsByUsername(userName);
    }
}

