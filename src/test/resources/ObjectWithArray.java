package pl.kompikownia.yaml2prog.dto;

import pl.kompikownia.yaml2prog.dto.TestObject;
import java.util.List;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class ObjectWithArray {
    private List<Integer>  integerArray;
    private List<TestObject>  objectArray;
}
