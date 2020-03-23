package example.controller;

import example.client.WeatherClient;
import example.model.Person;
import example.repository.PersonRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class ExampleControllerTest {

    private ExampleController controller;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private WeatherClient weatherClient;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        controller = new ExampleController(personRepository, weatherClient);
    }

    @Test
    public void shouldReturnFullNameOfAPerson() {
        Person person = new Person("Peter", "Pan");
        given(personRepository.findByLastName(anyString()))
                .willReturn(Optional.of(person));

        String greeting = controller.hello(person.getLastName());

        assertThat(greeting, is(String.format("Hello %s %s!", person.getFirstName(), person.getLastName())));
    }

    @Test
    public void shouldTellIfPersonIsUnknown() {
        given(personRepository.findByLastName(anyString()))
                .willReturn(Optional.empty());

        String lastName = "Pan";
        String greeting = controller.hello(lastName);

        assertThat(greeting, is(String.format("Who is this '%s' you're talking about?", lastName)));
    }
}