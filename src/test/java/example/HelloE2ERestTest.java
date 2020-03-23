package example;

import example.model.Person;
import example.repository.PersonRepository;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloE2ERestTest {

    @Autowired
    private PersonRepository personRepository;

    @LocalServerPort
    private int port;

    @After
    public void tearDown() throws Exception {
        personRepository.deleteAll();
    }

    @Test
    public void shouldReturnGreeting() {
        Person person = new Person("Peter", "Pan");
        personRepository.save(person);

        when()
                .get(String.format("http://localhost:%s/hello/%s", port, person.getLastName()))
                .then()
                .statusCode(Matchers.is(200))
                .body(Matchers.containsString(String.format("Hello %s %s!", person.getFirstName(), person.getLastName())));
    }
}
