package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import pl.futurecollars.invoicing.utils.FileService;

public class IdService {

  private final FileService filesService;
  private final Path path;

  private int id;

  public IdService(FileService filesService, Path path) {
    this.filesService = filesService;
    this.path = path;
    this.id = readNextIdFromFile();
  }

  public int getNextIdAndIncrement() {
    try {
      id +=1;
      filesService.writeToFile(path, String.valueOf(id));
      return id;
    } catch (IOException e) {
      throw new RuntimeException("Problem z generwoaniem ID", e);
    }
  }

  private int readNextIdFromFile() {
    try {
      if (!Files.exists(path)) {
        Files.createFile(path);
        filesService.writeToFile(path, "1");
      }
      List<String> idString = filesService.readAllLines(path);
      return Integer.parseInt(idString.get(0));
    } catch (Exception e) {
      throw new RuntimeException("Znaleziono blad z plikiem przechowujacym aktualne ID", e);
    }
  }

}
