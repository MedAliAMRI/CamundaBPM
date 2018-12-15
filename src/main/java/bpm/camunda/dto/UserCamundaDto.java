package bpm.camunda.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserCamundaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    public UserCamundaDto() {
        super();
    }

    public UserCamundaDto(String id, String firstName, String lastName, String password, String email) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
    }

}
