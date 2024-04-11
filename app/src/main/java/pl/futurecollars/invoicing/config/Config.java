package pl.futurecollars.invoicing.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.db.file.IdService;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@Configuration
public class Config {

  public static final String DATABASE_LOCATION = "db";
  public static final String ID_FILE_NAME = "id.txt";
  public static final String INVOICES_FILE_NAME = "invoices.txt";

  @Bean
  public IdService idService(FileService fileService) throws IOException {
    Path idFilePath = Files.createTempFile(DATABASE_LOCATION, ID_FILE_NAME);
    return new IdService(fileService, idFilePath);
  }

  @Bean
  public Database fileBasedDatabase(JsonService jsonService, FileService fileService, IdService idService) throws IOException {
    Path databaseFilePath = Files.createTempFile(DATABASE_LOCATION, INVOICES_FILE_NAME);
    return new FileBasedDatabase(jsonService, fileService, idService, databaseFilePath);
  }
}
