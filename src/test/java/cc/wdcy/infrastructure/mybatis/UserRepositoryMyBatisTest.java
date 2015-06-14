package cc.wdcy.infrastructure.mybatis;

import cc.wdcy.domain.user.User;
import cc.wdcy.domain.user.UserRepository;
import cc.wdcy.infrastructure.AbstractRepositoryTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

/**
 * @author Shengzhao Li
 */
public class UserRepositoryMyBatisTest extends AbstractRepositoryTest {

    @Autowired
    private UserRepository userRepository;


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
    @Test(enabled = false)
    public void testPrivilege() {

        String guid = "55b713df1c6f423e842ad68668523c49";
        final User user = userRepository.findByGuid(guid);

        assertNotNull(user);
        assertEquals(user.privileges().size(), 1);


    }


}