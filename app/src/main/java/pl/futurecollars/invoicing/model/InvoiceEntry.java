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
  private BigDecimal netPrice;

  @ApiModelProperty(value = "Product/service tax value", required = true, example = "123")
  @Builder.Default
  private BigDecimal vatValue = BigDecimal.ZERO;
  @ApiModelProperty(value = "Tax rate", required = true)
  private Vat vatRate;

  @ApiModelProperty(value = "Car this expense is related to, empty if expense is not related to car")
  private Car expensionRelatedToCar;

}
