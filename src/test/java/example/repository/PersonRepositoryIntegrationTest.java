package example.repository;

import example.model.Person;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonRepositoryIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @After
    public void tearDown() throws Exception {
        personRepository.deleteAll();
    }

    @Test
    public void shouldSaveAndDeletePerson() {
        Person person = new Person("Peter", "Pan");
        personRepository.save(person);

        Optional<Person> personOptional = personRepository.findByLastName(person.getLastName());

        assertThat(personOptional, is(Optional.of(person)));
    }
}