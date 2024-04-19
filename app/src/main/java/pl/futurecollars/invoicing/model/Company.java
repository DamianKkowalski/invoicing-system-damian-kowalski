package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Company {

  @ApiModelProperty(value = "Tax Identification number", required = true, example = "123-123-123-12")
  private String taxIdentifications;
  @ApiModelProperty(value = "Company address", required = true, example = "ul. Słowianska 123;  33-333 Zielona Gora")

  private String address;
  @ApiModelProperty(value = "Company name", required = true, example = "KowalPol.sp. z o. o.")

  private String name;

  public Company(String taxIdentifications, String address, String name) {
    this.taxIdentifications = taxIdentifications;
    this.address = address;
    this.name = name;
  }
}
