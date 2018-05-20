package com.dpgb.microservice.repository;

import com.dpgb.microservice.entity.User;
import com.dpgb.microservice.utils.UserType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createAndFindUserTest() {
        User user = new User();
        user.setId(1);
        user.setName("UserA");
        user.setCreateDate(new Date());
        user.setUserType(UserType.CUSTOMER);

        Integer id = userRepository.save(user).getId();

        User result = userRepository.findById(id).get();

        assertThat(result.getName())
                .isEqualTo(user.getName());
    }

    @Test
    public void updateUserTest() {
        User user = new User();
        user.setName("UserB");
        user.setCreateDate(new Date());
        user.setUserType(UserType.ADMIN);

        Integer userId = userRepository.save(user).getId();

        user.setName("UserBNew");

        userRepository.save(user);

        assertThat(userRepository.getOne(userId).getName()).isEqualTo("UserBNew");
    }

    @Test
    public void deleteProductTest() {
        User user = new User();
        user.setId(3);
        user.setName("UserC");
        user.setCreateDate(new Date());
        user.setUserType(UserType.CUSTOMER);

        userRepository.save(user);

        userRepository.delete(user);

        List<User> userList = userRepository.findAll();

        assertThat(user).isNotIn(userList);
    }

}
