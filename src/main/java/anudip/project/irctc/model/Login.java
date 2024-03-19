package anudip.project.irctc.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@NotNull(message = "Login can not be null")
public class Login {

	@NotBlank(message = "email should not be blank")
	@NotNull(message = "email can not be null")
	@Email(message = "Not a valid Email")
	private String email;

	@NotBlank(message = "password should not be blank")
	@NotNull(message = "password can not be null")
	private String password;
}
