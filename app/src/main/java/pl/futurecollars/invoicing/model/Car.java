package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Car {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "Car id (generated by application)", required = true, example = "1")
  private int id;

  @ApiModelProperty(value = "Car registraion number", required = true, example = "EPI 123456")
private String registrationNumber;

  @ApiModelProperty(value = "Specifies if car is used for personal reasons", required = true, example = "true")
private boolean personalUse;

}
