package service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mtumilowicz on 2018-07-10.
 */
@Service
class PersonService {

    private final Map<Integer, Person> personMap;

    public PersonService() {
        personMap = new HashMap<>();
        personMap.put(1, new Person(1, "Richard", "Gere"));
        personMap.put(2, new Person(2, "Emma", "Choplin"));
        personMap.put(3, new Person(3, "Anna", "Carolina"));
    }

    Person findPersonById(Integer id) {
        return personMap.get(id);
    }
}