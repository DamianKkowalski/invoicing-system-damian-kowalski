package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  public void update(int id, Invoice updatedInvoice) {
    try {
      List<String> updatedLines = filesService.readAllLines(path);
      var filteredLinesWithoutUpdatedId = updatedLines
              .stream()
              .filter(line -> !containsId(line, id))
              .collect(Collectors.toList());
      if (updatedLines.size() == filteredLinesWithoutUpdatedId.size()) {
        throw new IllegalArgumentException("Id " + id + " does not exist");
      }
      updatedInvoice.setId(id);
      filteredLinesWithoutUpdatedId.add(jsonService.toJson(updatedInvoice));
      filesService.writeLinesToFile(path, filteredLinesWithoutUpdatedId);
    } catch (IOException e) {
      throw new RuntimeException("Blad z aktualizacja danych na fakturze", e);
    }

  }

  @Override
  public void delete(int id) {
    try {
      List<String> updatedLines = filesService.readAllLines(path)
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());
      filesService.writeLinesToFile(path, updatedLines);
    } catch (IOException e) {
      throw new RuntimeException("Problem z usunieciem pliku po ID");
    }
  }

  private boolean containsId(String line, int id) {
    return line.contains("\"id\":" + id + ",");
  }
}
