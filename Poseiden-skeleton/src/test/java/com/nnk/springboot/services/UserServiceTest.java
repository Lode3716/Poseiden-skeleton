package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.exceptions.UserExistException;
import lombok.extern.java.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService iUserService;


    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private User user;

    @Before
    public void setup()
    {
        user = new User("Titi","1plPM1_2Mv","Dupont","USER");
    }


    @Test
    public void givenUser_shouldSave_userIsNewAndGoodFormat()
    {
        User nUser = iUserService.save(user);
        log.info(user.toString());
        Assert.assertNotNull(nUser.getId());
        Assert.assertTrue(nUser.getFullname().equals("Dupont"));
    }


    @Test
    public void givenUser_shouldUser_userExistretunExcepion()
    {
        exceptionRule.expect(UserExistException.class);
        exceptionRule.expectMessage("This user name is already exiting");
        iUserService.save(user);
    }

    @Test
    public void givenUser_shouldUser_userDelete()
    {
        iUserService.delete(user);
    }


}