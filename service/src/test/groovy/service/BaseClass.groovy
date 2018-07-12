package service

import io.restassured.module.mockmvc.RestAssuredMockMvc
import spock.lang.Specification
/**
 * Created by mtumilowicz on 2018-07-12.
 */
class BaseClass extends Specification {
    
    void setup() {
        def personService = Mock(PersonService) {
            findPersonById(1) >> new Person(1, "foo", "bee")
        }
        RestAssuredMockMvc.standaloneSetup(new PersonRestController(personService))
    }
}
