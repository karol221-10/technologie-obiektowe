package pl.kompikownia.yaml2prog.dto;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class Person {
    private String name;
    private String surname;
    private Integer age;
}
