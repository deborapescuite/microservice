package com.dpgb.microservice.repository;

import com.dpgb.microservice.entity.Audit;
import com.dpgb.microservice.entity.User;
import com.dpgb.microservice.utils.UserType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AuditRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    private  Integer savedUserId;

    @Before
    public void setUp() {
        user = new User();
        user.setName("User1");
        user.setCreateDate(new Date());
        user.setUserType(UserType.ADMIN);

        savedUserId = userRepository.save(user).getId();
    }

    @Test
    public void createAudit() {

        Audit audit = new Audit("Delete user",1,LocalTime.now(), savedUserId);

        Integer savedAuditId = auditRepository.save(audit).getId();

        Audit result = auditRepository.findById(savedAuditId).get();

        assertThat(result.getAction())
                .isEqualTo(audit.getAction());
    }

}
