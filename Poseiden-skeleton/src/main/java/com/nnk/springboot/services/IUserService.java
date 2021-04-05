package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;

public interface IUserService extends ICrudService<UserDto> {


    public User existById(Integer id);
    public boolean existsByUsername(String userName);

}
