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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_train_available_days")
public class TrainAvailableDays {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "day_id")
	private int dayId;
	
	@ManyToOne
	@JoinColumn(name = "train_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@NotNull(message = "Train id cannot be null")
	private Train train;
	
	@Column(nullable = false)
	private int day;

	
}
