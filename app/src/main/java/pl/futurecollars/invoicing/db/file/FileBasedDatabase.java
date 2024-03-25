package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

public class FileBasedDatabase implements Database {

  private final FileService filesService;

  private final JsonService jsonService;

  private final IdService idService;

  private final Path path;

  public FileBasedDatabase(JsonService jsonService, FileService fileService, IdService idService, Path path) {
    this.filesService = fileService;
    this.jsonService = jsonService;
    this.idService = idService;
    this.path = path;
  }

  @Override
  public int save(Invoice invoice) {
    try {
      invoice.setId(idService.getNextIdAndIncrement());
      filesService.appendLineToFile(path, jsonService.toJson(invoice));
      return invoice.getId();
    } catch (IOException e) {
      throw new RuntimeException("Problem z zapisaniem Invoice", e);
    }
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.empty();
  }
  // TODO wypisywanie Invoice po ID

  @Override
  public List<Invoice> getAll() {
    /*try {
      // return filesService.readAllLines(path);
    } catch (IOException e) {
      throw new RuntimeException("Problem z odczytaniem wszystkich rekordow z Invoice", e);
    }*/
    return new ArrayList<>();
  }
  // TODO wypisaywanie listy dokonczyc

  @Override
  public void update(int id, Invoice updatedInvoice) {

  }

  @Override
  public void delete(int id) {

  }
}
