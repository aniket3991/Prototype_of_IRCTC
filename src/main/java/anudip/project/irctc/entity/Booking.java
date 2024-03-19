package anudip.project.irctc.entity;

import java.time.LocalDate;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "tbl_booking")
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
	private int bookingId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private User user;
	
	@Column(name = "first_name", length = 50, nullable = false)
	@NotBlank(message = "*First name should not blank")
	@Size(min = 3, message = "*At least 3 characters")
	private String firstName;
	
	@Column(name = "last_name", length = 50, nullable = false)
	@NotBlank(message = "*Last name should not blank")
	@Size(min = 3, message = "*At least 3 characters")
	private String lastName;
	
	@ManyToOne
	@JoinColumn(name = "train_id")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Train train;
	
	@Column(length = 50, nullable = false)
	@NotBlank(message = "*Source should not blank")
	@Size(min = 3, message = "*At least 3 characters")
	private String source;
	
	@Column(length = 50, nullable = false)
	@NotBlank(message = "*Destination should not blank")
	@Size(min = 3, message = "*At least 3 characters")
	private String destination;
	
	@Column(nullable = false, columnDefinition = "decimal(6,2) default 0.0")
	private float price;
	
	@Column(name = "travel_date", nullable = false)
	private LocalDate travelDate;
	
	@Column(nullable = false)
	private int age;
	
	@Column(length = 6, nullable = false)
	@NotBlank(message = "*must be selected")
	private String gender;
	
	@Column(length = 10, nullable = false)
	private String pnr;
	
	@Column(length = 16, nullable = false)
	@NotBlank(message = "*aadhaar should not blank")
	@Size(min = 16, max = 16, message = "*Must be of 16 digits")
	private String aadhaar;
	
	@Column(name = "seat_type", length = 5, nullable = false)
	@NotBlank(message = "*Please select Seat Type")
	private String seatType;
	
	@Column(name = "seat_no", length = 10, nullable = false)
	private String seatNo;
	
	@Column(length = 20, nullable = false)
	private String status;

	public Booking(User user,
			@NotBlank(message = "*First name should not blank") @Size(min = 3, message = "*At least 3 characters") String firstName,
			@NotBlank(message = "*Last name should not blank") @Size(min = 3, message = "*At least 3 characters") String lastName,
			Train train,
			@NotBlank(message = "*Source should not blank") @Size(min = 3, message = "*At least 3 characters") String source,
			@NotBlank(message = "*Destination should not blank") @Size(min = 3, message = "*At least 3 characters") String destination,
			float price, LocalDate travelDate, int age, @NotBlank(message = "*must be selected") String gender,
			String pnr,
			@NotBlank(message = "*aadhaar should not blank") @Size(min = 16, max = 16, message = "*Must be of 16 digits") String aadhaar,
			@NotBlank(message = "*Please select Seat Type") String seatType, String seatNo, String status) {
		super();
		this.user = user;
		this.firstName = firstName;
		this.lastName = lastName;
		this.train = train;
		this.source = source;
		this.destination = destination;
		this.price = price;
		this.travelDate = travelDate;
		this.age = age;
		this.gender = gender;
		this.pnr = pnr;
		this.aadhaar = aadhaar;
		this.seatType = seatType;
		this.seatNo = seatNo;
		this.status = status;
	}
	
	
}
