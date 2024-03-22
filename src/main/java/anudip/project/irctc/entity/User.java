package anudip.project.irctc.entity;
import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tbl_user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @NotBlank(message="* First Name can't not be null")
    @Size(min=3,   max=12,   message="* First Name must be between 3 to 12")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    
    @NotBlank(message="* last Name can't not be null")
    @Size(min=3,   max=20,   message="* last Name must be between 3 to 12")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank(message="*Email is required")
    @Email(message="Invalid email")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    
    @NotBlank(message="* phone number is required")
    @Column(name = "phone", nullable = false, length = 15)
    private String contact;

    
    @NotBlank(message="* password can't be null")
    @Size(min=6,  message="*Password must contain atleast 6 character")
    @Column(nullable = false, length = 100)
    private String password;

    @NotBlank(message="* kindly select user Type")
    @Column(nullable = false, length = 10)
    private String role;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int status;
}