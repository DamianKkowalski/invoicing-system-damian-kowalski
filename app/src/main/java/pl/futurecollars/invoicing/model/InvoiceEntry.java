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
public class InvoiceEntry {

  @ApiModelProperty(value = "Product/service description", required = true, example = "Laptop")
  private String description;

  @ApiModelProperty(value = "Number of items", required = true, example = "85")
  private int quantity;

  @ApiModelProperty(value = "Product/service netto price", required = true, example = "555")
  private BigDecimal price;

  @ApiModelProperty(value = "Product/service tax value", required = true, example = "123")
  private BigDecimal vatValue;
  @ApiModelProperty(value = "Tax rate", required = true)
  private Vat vatRate;

}
