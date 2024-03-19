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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_stations")
public class Station {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "station_id")
	private int stationId;
	
	@Column(name = "station_name", length = 100, nullable = false, unique = true)
	@NotBlank(message = "*Name should not be blank")
	@Size(min = 3, message = "*At least 3 characters long")
	private String stationName;
	
	@Column(name = "station_code", length = 5, nullable = false, unique = true)
	@NotBlank(message = "Code should not be blank")
	@Size(min = 3, max = 5, message = "Between 3 to 5 characters only")
	private String stationCode;

}
