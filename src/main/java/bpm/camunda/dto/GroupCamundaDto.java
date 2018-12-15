package bpm.camunda.dto;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupCamundaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private String id;
    private String name;
    private String type;

    public GroupCamundaDto() {
        super();
    }

    public GroupCamundaDto(String id, String name, String type) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
    }

}
