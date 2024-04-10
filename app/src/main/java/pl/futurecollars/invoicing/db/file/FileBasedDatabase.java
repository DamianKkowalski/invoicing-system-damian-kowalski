package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@AllArgsConstructor

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
    try {

      return filesService.readAllLines(path)
          .stream()
          .filter(line -> containsId(line, id))
          .map(line -> jsonService.toObject(line, Invoice.class))
          .findFirst();
    } catch (IOException e) {
      throw new RuntimeException("Blad z odnalezieniem faktury po ID", e);
    }
  }

  @Override
  public List<Invoice> getAll() {
    try {
      return filesService.readAllLines(path)
          .stream()
          .map(line -> jsonService.toObject(line, Invoice.class))
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Problem z odczytaniem wszystkich rekordow z Invoice", e);
    }
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    try {
      List<String> updatedLines = filesService.readAllLines(path);
      var filteredLinesWithoutUpdatedId = updatedLines
              .stream()
              .filter(line -> !containsId(line, id))
              .collect(Collectors.toList());
      updatedInvoice.setId(id);
      filteredLinesWithoutUpdatedId.add(jsonService.toJson(updatedInvoice));
      filesService.writeLinesToFile(path, filteredLinesWithoutUpdatedId);
      updatedLines.removeAll(filteredLinesWithoutUpdatedId);
      return updatedLines.isEmpty() ? Optional.empty() : Optional.of(jsonService.toObject(updatedLines.get(0), Invoice.class));
    } catch (IOException e) {
      throw new RuntimeException("Blad z aktualizacja danych na fakturze", e);
    }

  }

  @Override
  public Optional<Invoice> delete(int id) {
    try {
      var updatedLines = filesService.readAllLines(path);
      var invoicesExceptDeleted = updatedLines
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());
      filesService.writeLinesToFile(path, invoicesExceptDeleted);
      updatedLines.removeAll(invoicesExceptDeleted);
      return updatedLines.isEmpty() ? Optional.empty() : Optional.of(jsonService.toObject(updatedLines.get(0), Invoice.class));
    } catch (IOException e) {
      throw new RuntimeException("Problem z usunieciem pliku po ID");
    }
  }

  private boolean containsId(String line, int id) {
    return line.contains("\"id\":" + id + ",");
  }
}
