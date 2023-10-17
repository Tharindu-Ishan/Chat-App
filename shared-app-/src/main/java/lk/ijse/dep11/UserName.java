package lk.ijse.dep11;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserName implements Serializable {
//    private static final long serialVersionUID=1L;

    public  String username;
    public static UserName name;



}
