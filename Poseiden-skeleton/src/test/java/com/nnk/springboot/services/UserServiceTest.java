package com.nnk.springboot.services;

import com.googlecode.jmapper.JMapper;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.exceptions.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    JMapper<UserDto, User> userJMapper;

    @Mock
    JMapper<User, UserDto> userUnJMapper;

    private static User user;

    private static User user1;

    private static UserDto userDto;

    private static UserDto userDto1;

    private static List<UserDto> listUserDto;

    @BeforeEach
    public void setup() {
        userDto = new UserDto(1,"Jack", "Ursul1pm*", "Jack", "USER");
        userDto1 = new UserDto(2,"John", "John21pm*", "John", "USER");
        user = new User(1,"Jack", "Ursul1pm*", "Jack", "USER");
        user1 = new User(2,"John", "John21pm*", "John", "USER");

        listUserDto = Arrays.asList(userDto, userDto1);
    }


    @Test
    public void givenSearchListOfUserDto_whenAllUser_thenReturnListOfUserDto() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user1));
        when(userJMapper.getDestination(user)).thenReturn(userDto);
        when(userJMapper.getDestination(user1)).thenReturn(userDto1);

        List<UserDto> result = userService.readAll();

        assertThat(result).isEqualTo(listUserDto);
        assertThat(result).asList();
        assertThat(result).size().isEqualTo(2);

        InOrder inOrder = inOrder(userRepository, userJMapper);
        inOrder.verify(userRepository).findAll();
        inOrder.verify(userJMapper).getDestination(user);
        inOrder.verify(userJMapper).getDestination(user1);
    }

    @Test
    public void givenUserDto_whenSaveUser_thenUserUserIsSavedCorrectly() {
        UserDto userDto3 = new UserDto("Tete", "USER", "UrPol1pm*", "Jack");
        User user3 = new User("Tete", "USER", "UrPol1pm*", "Jack");

        when(userUnJMapper.getDestination(any(UserDto.class))).thenReturn(user3);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userJMapper.getDestination(any(User.class))).thenReturn(userDto);

        UserDto asSave = userService.save(userDto3);

        assertThat(asSave).isEqualTo(userDto);
        InOrder inOrder = inOrder(userUnJMapper, userRepository, userJMapper);
        inOrder.verify(userUnJMapper).getDestination(any(UserDto.class));
        inOrder.verify(userRepository).save(any(User.class));
        inOrder.verify(userJMapper).getDestination(any(User.class));
    }

    @Test
    public void givenIdUserAndUserDto_whenUpdateUser_thenUserIsUpdateCorrectly() {
        User updateUser = new User(1,"Jack", "Ursul21pm*", "Jacki", "USER");
        UserDto updateDto = new UserDto(1,"Jack", "Ursul21pm*", "Jacki", "USER");

        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(user));
        when(userRepository.save(any(User.class))).thenReturn(updateUser);
        when(userJMapper.getDestination(any(User.class))).thenReturn(updateDto);

        UserDto result = userService.update(1, new UserDto(1,"Jack", "Ursul21pm*", "Jacki", "USER"));

        assertThat(result).isEqualTo(updateDto);
        InOrder inOrder = inOrder(userRepository, userJMapper);
        inOrder.verify(userRepository).findById(anyInt());
        inOrder.verify(userRepository).save(any(User.class));
        inOrder.verify(userJMapper).getDestination(any(User.class));
    }

    @Test
    public void givenIdUserDto_whenDeleteUser_thenBisListIsDeleteCorrectly() {
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(user));

        userService.delete(anyInt());

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository).findById(anyInt());
        inOrder.verify(userRepository).deleteById(anyInt());
    }

    @Test
    public void givenUnFoundUser_whenDeleteUser_thenUserNotFoundException() {
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,()->userService.delete(anyInt()));
    }

    @Test
    public void givenIdUserDto_whenFoundUser_thenReturnUserFound() {
        User userFind =  new User(1,"Jack", "Ursul1pm*", "Jack", "USER");;

        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(userFind));

        User result = userService.existById(1);

        assertThat(result).isEqualTo(user);
    }

    @Test
    public void givenUnFoundIdUserDto_whenFoundUser_thenUserNotFoundException() {
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,()->userService.existById(anyInt()));
    }


}
