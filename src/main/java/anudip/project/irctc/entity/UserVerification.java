package anudip.project.irctc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_validate_user")
public class UserVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int validateId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private int otp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    public UserVerification(String email, int otp, Date date) {
        this.email = email;
        this.otp = otp;
        this.time = date;
    }
}
