package service;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by mtumilowicz on 2018-07-10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public abstract class BaseClass {

    @Autowired
    PersonRestController personRestController;

    @MockBean
    PersonService personService;

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(personRestController);

        Mockito.when(personService.findPersonById(1))
                .thenReturn(new Person(1, "foo", "bee"));
    }

}
