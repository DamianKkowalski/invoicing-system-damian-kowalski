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
public class Company {

  @ApiModelProperty(value = "Tax Identification number", required = true, example = "123-123-123-12")
  private String taxIdentifications;
  @ApiModelProperty(value = "Company address", required = true, example = "ul. Słowianska 123;  33-333 Zielona Gora")

  private String address;
  @ApiModelProperty(value = "Company name", required = true, example = "KowalPol.sp. z o. o.")

  private String name;

}
