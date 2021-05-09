package pl.kompikownia.yaml2prog.dto;

import lombok.Setter;
import lombok.Getter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class Person {
    private String name;
    private String surname;
    private Double age;
}
