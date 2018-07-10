package service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mtumilowicz on 2018-07-10.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class Person {
    Integer id;
    String name;
    String surname;
}
