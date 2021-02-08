package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.exceptions.UserExistException;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Log4j2
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



    public void givenUser_shouldSave_userIsNewAndGoodFormat()
    {
        User nUser = iUserService.save(user);
        log.info(user.toString());
        Assert.assertNotNull(nUser.getId());
        Assert.assertTrue(nUser.getFullname().equals("Dupont"));
    }



    public void givenUser_shouldUser_userExistretunExcepion()
    {
        exceptionRule.expect(UserExistException.class);
        exceptionRule.expectMessage("This user name is already exiting");
        iUserService.save(user);
    }


    public void givenUser_shouldUser_userDelete()
    {

        iUserService.delete(user.getId());
    }


}