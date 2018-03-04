package com.momo.pink.it;

import com.momo.pink.User;
import com.momo.pink.app.PinkApp;
import com.momo.pink.test.autoconfigure.AutoConfigureMariaDB;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PinkApp.class, properties = {
    "encrypt.key=${encrypt.rootKey}oUzxewPh",
    "spring.test.maria.database=pink",
    "mariaDB4j.port=3307",
    "spring.datasource.username=root",
    "spring.datasource.password=",
    "spring.cloud.etcd.uris=http://127.0.0.1:4001",
    "spring.cloud.test.etcd.enable=true"
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
