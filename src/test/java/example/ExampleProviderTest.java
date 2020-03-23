package example;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.RestPactRunner;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.target.MockMvcTarget;
import example.client.WeatherClient;
import example.controller.ExampleController;
import example.model.Person;
import example.repository.PersonRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;

@RunWith(RestPactRunner.class)
@Provider("person_provider")
@PactFolder("target/pacts")
public class ExampleProviderTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private WeatherClient weatherClient;

    private ExampleController exampleController;

    @TestTarget
    public final MockMvcTarget target = new MockMvcTarget();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        exampleController = new ExampleController(personRepository, weatherClient);
        target.setControllers(exampleController);
    }

    @State("person data")
    public void personData() {
        Person person = new Person("Peter", "Pan");
        doReturn(Optional.of(person)).when(personRepository).findByLastName(person.getLastName());
    }
}
