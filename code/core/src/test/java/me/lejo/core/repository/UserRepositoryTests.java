package me.lejo.core.repository;

import me.lejo.core.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {

        // given
        User user = new User();
        user.setUsername("john");
        user.setPassword("password");
        user.setFirstName("John");
        user.setLastName("Tom");
        user.setActive(true);
        user.setDateCreated(new Date());
        userRepository.save(user);
//        entityManager.persist(user);
//        entityManager.flush();

        // when
        User found = userRepository.findByUsername("john");

        //then
        assertThat(found.getFirstName().equals("John"));
    }
}
