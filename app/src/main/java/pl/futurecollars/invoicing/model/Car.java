package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Car {

@ApiModelProperty(value = "Car registraion number" , required = true, example ="EPI 123456" )
private String registrationNumber;


@ApiModelProperty(value = "Specifies if car is used for personal reasons" , required = true, example = "true")
private boolean personalUse;

}