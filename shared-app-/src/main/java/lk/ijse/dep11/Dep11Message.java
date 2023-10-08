package lk.ijse.dep11;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dep11Message implements Serializable {
    private Dep11Headers header;
    private Object body;
    private static final long serialVersionUID=1L;

}
