package example.client;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit.PactProviderRule;
import au.com.dius.pact.consumer.junit.PactVerification;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import example.helper.FileLoader;
import example.model.WeatherResponse;
import org.apache.http.entity.ContentType;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherClientConsumerTest {

    @Autowired
    private WeatherClient weatherClient;

    @Rule
    public PactProviderRule weatherProvider = new PactProviderRule("weather_provider", "localhost", 8089, this);

    @Pact(consumer = "simple_microservice")
    public RequestResponsePact createPact(PactDslWithProvider builder) throws IOException {
        RequestResponsePact requestResponsePact = builder
                .given("weather forcast data")
                .uponReceiving("a request for a weather request for Hamburg")
                .path("/some-test-api-key/53.5511,9.9937")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(FileLoader.read("classpath:weatherApiResponse.json"), ContentType.APPLICATION_JSON)
                .toPact();
        return requestResponsePact;
    }

    @Test
    @PactVerification("weather_provider")
    public void shouldFetchWeatherInformation() {
        Optional<WeatherResponse> weatherResponseOptional = weatherClient.fetchWeather();
        assertThat(weatherResponseOptional.isPresent(), is(true));
        assertThat(weatherResponseOptional.get().getSummary(), is("Rain"));
    }
}
