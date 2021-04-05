package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.exceptions.UserExistException;
import com.nnk.springboot.services.exceptions.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JMapper<UserDto, User> userJMapper;

    @Autowired
    JMapper<User, UserDto> userUnJMapper;

    /**
     * If username exist throw error
     * When it's recorded, we return here.
     *
     * @param userDto
     * @return user create
     */
    @Override
    public UserDto save(UserDto userDto) {
        if (existsByUsername(userDto.getUsername())) {
            log.error("This user name is already existing : " + userDto.getUsername());
            throw new UserExistException("This user name is already existing");
        } else {
            User user = userUnJMapper.getDestination(userDto);
            return saveUser(user);
        }
    }

    /**
     * Find list UserList and Convert UserDto
     *
     * @return the list of UserDto
     */
    @Override
    public List<UserDto> readAll() {
        List<UserDto> listUserDto = new ArrayList<>();
        userRepository.findAll()
                .forEach(user ->
                {
                    listUserDto.add(userJMapper.getDestination(user));
                });
        log.debug("Service : create list user : {} ", listUserDto.size());
        return listUserDto;
    }

    /**
     * Check id exist, if valid update User
     *
     * @param id
     * @param userDto to update
     * @return the user update
     */
    @Override
    public UserDto update(Integer id, UserDto userDto) {
        User updateUser = existById(userDto.getId());
        if (updateUser.getUsername().equals(userDto.getUsername()) && updateUser.getId() == userDto.getId()) {
            updateUser.setPassword(userDto.getPassword());
            updateUser.setPassword(userDto.getUsername());
            updateUser.setFullname(userDto.getFullname());
            updateUser.setRole(userDto.getRole());
            log.debug("Service : update user : {} ", updateUser.getId());
            return saveUser(updateUser);
        } else {
            userRepository.findByUsername(userDto.getUsername())
                    .ifPresent(user ->
                    {
                        if (user.getId() == updateUser.getId()) {

                            log.info("Save update");
                        } else {
                            log.error("This user name is already existing : " + userDto.getUsername());
                            throw new UserExistException("This user name is already existing");
                        }
                    });

        }
        updateUser.setPassword(userDto.getPassword());
        updateUser.setPassword(userDto.getUsername());
        updateUser.setFullname(userDto.getFullname());
        updateUser.setRole(userDto.getRole());
        log.debug("Service : update user  : {} ", updateUser.getId());
        return saveUser(updateUser);
    }

    /**
     * Check id exist, if valid delete User
     *
     * @param id to delete
     */
    @Override
    public void delete(Integer id) {
        userRepository.deleteById(existById(id).getId());
        log.info("Service delete User id : {}", id);
    }


    /**
     * Find user By id
     *
     * @param id
     * @return the userDto find or issue IllegalArgumentException
     */
    @Override
    public UserDto readByid(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        log.info("Service : Read by Id USER - SUCCESS");
        return userJMapper.getDestination(user);
    }


    /**
     * Find user By id
     *
     * @param id
     * @return the userDto find or issue UserNotFoundException
     */
    @Override
    public User existById(Integer id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("There is no user with this id"));
    }

    /**
     * Find by username if exist return true
     *
     * @param userName
     * @return true if exist
     */
    @Override
    public boolean existsByUsername(String userName) {
        return userRepository.existsByUsername(userName);
    }


    /**
     * Crypt PassWord user and save it in the database.
     * Convert in UserDto
     *
     * @param user
     * @return userDto
     */
    private UserDto saveUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        User retour = userRepository.save(user);
        log.info("Service : user is save in Bdd : {} ", user.getId());
        return userJMapper.getDestination(retour);

    }

}

