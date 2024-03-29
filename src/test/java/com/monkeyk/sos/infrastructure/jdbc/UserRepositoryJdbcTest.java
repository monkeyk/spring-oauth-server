/*
 * Copyright (c) 2015 MONKEYK Information Technology Co. Ltd
 * www.monkeyk.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * MONKEYK Information Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with MONKEYK Information Technology Co. Ltd.
 */
package com.monkeyk.sos.infrastructure.jdbc;

import com.monkeyk.sos.domain.user.User;
import com.monkeyk.sos.domain.user.UserRepository;
import com.monkeyk.sos.infrastructure.AbstractRepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/*
 * @author Shengzhao Li
 */
public class UserRepositoryJdbcTest extends AbstractRepositoryTest {


    @Autowired
    private UserRepository userRepository;


    /**
     * @since 3.0.0
     */
    @Test
    void findProfileByUsername() {

        String username = "userxxxx";
        User user = userRepository.findProfileByUsername(username);
        assertNull(user);

        User user2 = new User(username, "{123}", "123", "ewo@honyee.cc");
        user2.address("address").nickname("nick-name");
        userRepository.saveUser(user2);

        User user3 = userRepository.findProfileByUsername(username);
        assertNotNull(user3);
        assertNotNull(user3.phone());
        assertNotNull(user3.email());

    }


    @Test
    public void findByGuid() {
        User user = userRepository.findByGuid("oood");
        assertNull(user);

        user = new User("user", "123", "123", "ewo@honyee.cc");
        userRepository.saveUser(user);

        user = userRepository.findByGuid(user.guid());
        assertNotNull(user);
        assertNotNull(user.email());


    }

    @Test
    public void findUsersByUsername() {
        User user = userRepository.findByGuid("oood");
        assertNull(user);

        user = new User("user", "123", "123", "ewo@honyee.cc");
        userRepository.saveUser(user);

        final List<User> list = userRepository.findUsersByUsername(user.username());
        assertNotNull(list);

        assertEquals(list.size(), 1);

    }


    @Test
    public void updateUser() {
        User user = new User("user", "123", "123", "ewo@honyee.cc");
        userRepository.saveUser(user);

        user = userRepository.findByGuid(user.guid());
        assertNotNull(user);
        assertNotNull(user.email());

        String newEmail = "test@honyee.cc";
        user.email(newEmail).phone("12344444");
        userRepository.updateUser(user);

        user = userRepository.findByGuid(user.guid());
        assertNotNull(user);
        assertEquals(user.email(), newEmail);
    }


    @Test
    public void findByUsername() {
        String username = "user";
        User user = new User(username, "123", "123", "ewo@honyee.cc");
        userRepository.saveUser(user);

        User result = userRepository.findByUsername(username);
        assertNotNull(result);
    }


    /*
     * Run the test must initial db firstly
     * */
//    @Test()
    public void testPrivilege() {

        String guid = "55b713df1c6f423e842ad68668523c49";
        final User user = userRepository.findByGuid(guid);

        assertNotNull(user);
        assertEquals(user.privileges().size(), 1);


    }


}