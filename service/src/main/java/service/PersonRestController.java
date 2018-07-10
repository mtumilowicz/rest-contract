package service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mtumilowicz on 2018-07-10.
 */
@RestController
class PersonRestController {

    private final PersonService personService;

    public PersonRestController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/person/{id}")
    public Person findPersonById(@PathVariable("id") Long id) {
        return personService.findPersonById(1);
    }
}
