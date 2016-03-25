package auto.parcel.sample;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import model1.HeightBucket;
import model2.Address;
import model3.Person;

public interface SampleData {

    Person ALICE = Person.create("Alice", 1L, HeightBucket.AVERAGE, new HashMap<String, Address>() {{
                put("home", Address.create(new double[]{0.3, 0.7}, "Rome"));
            }}, Collections.<Person>emptyList());

    Person BOB = Person.builder().name("Bob").id(2L)
            .heightType(HeightBucket.TALL)
            .addresses(new HashMap<String, Address>() {{
                put("home", Address.create(new double[]{3.2, 143.2}, "Turin"));
                put("work", Address.create(new double[]{5.9, 156.1}, "Genoa"));
            }}).friends(Arrays.asList(ALICE)).build();
}
