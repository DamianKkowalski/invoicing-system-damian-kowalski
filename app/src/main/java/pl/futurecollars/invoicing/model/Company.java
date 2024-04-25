package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
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

  @Builder.Default
  @ApiModelProperty(value = "Pension insurance amount", required = true, example = "1328.75")
  private BigDecimal pensionInsurance = BigDecimal.ZERO;

  @Builder.Default
  @ApiModelProperty(value = "Health insurance amount", required = true, example = "458.34")
  private BigDecimal healthInsurance = BigDecimal.ZERO;


}
