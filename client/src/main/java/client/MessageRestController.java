package client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by mtumilowicz on 2018-07-10.
 */
@RestController
public class MessageRestController {
    private final RestTemplate restTemplate;

    MessageRestController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @RequestMapping("/message/{personId}")
    String getMessage(@PathVariable("personId") Integer personId) {
        Person person = this.restTemplate.getForObject("http://localhost:8080/person/{personId}", Person.class, personId);
        return "Hello " + person.getName();
    }
}
