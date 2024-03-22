package anudip.project.irctc.entity;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_destination")
public class Destination {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "destination_id")
	private int destinationID;
	
	@ManyToOne
	@JoinColumn(name = "train_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Train train;
	
	@ManyToOne
	@JoinColumn(name = "station_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Station station;
	
	@Column(name = "required_min", nullable = false)
	private int requiredMinutes;
	
	@Column(nullable = false, columnDefinition = "decimal(6,2) default 0.0")
	private float price;
}
