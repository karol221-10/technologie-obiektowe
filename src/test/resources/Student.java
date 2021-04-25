package pl.kompikownia.yaml2prog.dto;

import pl.kompikownia.yaml2prog.dto.Person;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;

@Getter
@Setter
public class Student extends Person {
    @Builder
    protected Student(String name,String surname,Integer age,String studentIdNumber,Integer yearOfStudy) {
        super(name,surname,age);
        this.studentIdNumber = studentIdNumber;
        this.yearOfStudy = yearOfStudy;
    }
    private String studentIdNumber;
    private Integer yearOfStudy;
}
