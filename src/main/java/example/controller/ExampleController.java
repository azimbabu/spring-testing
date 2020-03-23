package example.controller;

import example.client.WeatherClient;
import example.model.WeatherResponse;
import example.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    private final PersonRepository personRepository;
    private final WeatherClient weatherClient;

    @Autowired
    public ExampleController(PersonRepository personRepository, WeatherClient weatherClient) {
        this.personRepository = personRepository;
        this.weatherClient = weatherClient;
    }

    @GetMapping("/hello/{lastName}")
    public String hello(@PathVariable final String lastName) {
        return personRepository.findByLastName(lastName)
                .map(person -> String.format("Hello %s %s!", person.getFirstName(), person.getLastName()))
                .orElse(String.format("Who is this '%s' you're talking about?", lastName));
    }

    @GetMapping("/weather")
    public String weather() {
        return weatherClient.fetchWeather()
                .map(WeatherResponse::getSummary)
                .orElse("Sorry, I couldn't fetch the weather for you :(");
    }
}
