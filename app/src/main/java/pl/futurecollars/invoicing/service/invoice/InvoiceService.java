package pl.futurecollars.invoicing.service.invoice;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Service
public class InvoiceService {

  private final Database<Invoice> database;

  public InvoiceService(Database<Invoice> database) {
    this.database = database;
  }

  public int save(Invoice invoice) {
    return database.save(invoice);
  }

  public Optional<Invoice> getById(int id) {
    return database.getById(id);
  }

  public List<Invoice> getAll() {
    return database.getAll();
  }

  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    updatedInvoice.setId(id); // just in case it was not set
    return database.update(id, updatedInvoice);
  }

  public Optional<Invoice> delete(int id) {
    return database.delete(id);
  }
}
