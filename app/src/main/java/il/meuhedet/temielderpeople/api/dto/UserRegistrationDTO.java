package il.meuhedet.temielderpeople.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserRegistrationDTO {
    private String identityNumber;
    private String name;

    public UserRegistrationDTO(String identityNumber, String name) {
        this.identityNumber = identityNumber;
        this.name = name;
    }
}
