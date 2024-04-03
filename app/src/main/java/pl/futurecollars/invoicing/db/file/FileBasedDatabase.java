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
      List<String> lines = filesService.readAllLines(path);
      return lines.stream()
          .map(json -> jsonService.toObject(json, Invoice.class))
          .filter(invoice -> invoice.getId() == id)
          .findFirst();
    } catch (IOException e) {
      throw new RuntimeException("Blad z odnalezieniem faktury po ID", e);
    }
  }
  // TODO wypisywanie Invoice po ID

  @Override
  public List<Invoice> getAll() {
    try {
      List<String> lines = filesService.readAllLines(path);
      return lines.stream()
          .map(json -> jsonService.toObject(json, Invoice.class))
          .peek(System.out::println)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new RuntimeException("Problem z odczytaniem wszystkich rekordow z Invoice", e);
    }
  }
  // TODO wypisaywanie listy dokonczyc

  @Override
  public void update(int id, Invoice updatedInvoice) {
    try {
      List<String> updatedLines = filesService.readAllLines(path)
          .stream()
          .map(json -> {
            Invoice invoice = jsonService.toObject(json, Invoice.class);
            if (invoice.getId() == id) {
              return jsonService.toJson(updatedInvoice);
            } else {
              return json;
            }
          })
          .collect(Collectors.toList());
      filesService.writeLinesToFile(path, updatedLines);
    } catch (IOException e) {
      throw new RuntimeException("Blad z aktualizacja danych na fakturze", e);
    }

  }
  // TODO, fileservie, jeson service wyjatki,

  @Override
  public void delete(int id) {
    try {
      List<String> updatedLines = filesService.readAllLines(path)
          .stream()
          .filter(json -> {
            Invoice invoice = jsonService.toObject(json, Invoice.class);
            return invoice.getId() != id;
          })
          .collect(Collectors.toList());
      filesService.writeLinesToFile(path, updatedLines);
    } catch (IOException e) {
      throw new RuntimeException("Problem z usunieciem pliku po ID");
    }
  }
}
