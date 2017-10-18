package com.momo.pink.it;

import com.momo.pink.User;
import com.momo.pink.app.PinkApp;
import com.momo.pink.test.autoconfigure.AutoConfigureMariaDB;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PinkApp.class, properties = {
    "spring.test.maria.database=pink",
    "spring.datasource.username=pink",
    "spring.datasource.password=pink",
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMariaDB
public class UserTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateUser() {
        User user = this.restTemplate.postForObject(
            "/api/v1.0/users", new User()
                .setEmail("bphanzhu@gmail.com")
                .setName("qiyi"), User.class);
        assertNotNull(user.getId());
        User queryUser = this.restTemplate.getForObject(
            "/api/v1.0/users/" + user.getName(), User.class);
        assertEquals(user.getId(), queryUser.getId());
        assertEquals(user.getName(), queryUser.getName());
        assertEquals(user.getEmail(), queryUser.getEmail());
    }
}
