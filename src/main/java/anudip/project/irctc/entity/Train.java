package anudip.project.irctc.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Table(name = "tbl_trains")
@Entity
public class Train {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "train_id")
	private int trainId;
	
	@Column(name = "train_no", unique = true, nullable = false)
	@NotBlank(message = "*train number should not blank")
	private int trainNo;
	
	@Column(name = "train_name", length = 50, nullable = false, unique = true)
	@NotBlank(message = "*Train name should not blank")
	@Size(min=3, message="*At least 3 charater required")
	private String name;
	
	@Column(length = 50, nullable = false)
	@NotBlank(message = "*Source should not blank")
	@Size(min=3, message="*At least 3 charater required")
	private String source;
	
	@Column(length = 50, nullable = false)
	@NotBlank(message = "*Destination should not blank")
	@Size(min=3, message="*At least 3 charater required")
	private String destination;
	
	@Column(name = "arrival_time", length = 5, nullable = false)
	@NotBlank(message = "*Invalid Time")
	@Size(min=5, max = 5, message="*Invalid Time")
	private String arrivalTime;
	
	@Column(name = "departure_time", length = 5, nullable = false)
	@NotBlank(message = "*Invalid Time")
	@Size(min=5, max = 5, message="*Invalid Time")
	private String departureTime;
	
	@Column(name = "seat_1a_count", nullable = false, columnDefinition = "int default 0")
	@NotBlank(message = "*Invalid Number at least fill 0")
	private int seat1ACount;
	
	@Column(name = "seat_1a_price", nullable = false, columnDefinition = "decimal(6,2) default 0.0")
	@NotBlank(message = "*Invalid Price at least fill 0")
	private float seat1APrice;
	
	@Column(name = "seat_2a_count", nullable = false, columnDefinition = "int default 0")
	@NotBlank(message = "*Invalid Number at least fill 0")
	private int seat2ACount;
	
	@Column(name = "seat_2a_price", nullable = false, columnDefinition = "decimal(6,2) default 0.0")
	@NotBlank(message = "*Invalid Price at least fill 0")
	private float seat2APrice;
	
	@Column(name = "seat_sl_count", nullable = false, columnDefinition = "int default 0")
	@NotBlank(message = "*Invalid Number at least fill 0")
	private int seatSlCount;
	
	@Column(name = "seat_sl_price", nullable = false, columnDefinition = "decimal(6,2) default 0.0")
	@NotBlank(message = "*Invalid Price at least fill 0")
	private float seatSlPrice;
	
	@Column(name = "seat_gen_count", nullable = false, columnDefinition = "int default 0")
	@NotBlank(message = "*Invalid Number at least fill 0")
	private int seatGenCount;
	
	@Column(name = "seat_gen_price", nullable = false, columnDefinition = "decimal(6,2) default 0.0")
	@NotBlank(message = "*Invalid Price at least fill 0")
	private float seatGenPrice;

}
