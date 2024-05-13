package pl.futurecollars.invoicing.db.mongo;

import com.mongodb.client.MongoCollection;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

@RequiredArgsConstructor
public class MongoIdProvider {

  private static final String LAST_VALUE_KEY = "lastValue";
  private static final String ID_VALUE = "invoiceCounter";
  private static final String ID_KEY = "_id";

  private static final Document FILTER_DOCUMENT = new Document(ID_KEY, ID_VALUE);

  private final MongoCollection<Document> collection;
  private int lastValue = 0;

  @PostConstruct
  private void postConstruct() {
    var iterator = collection.find(FILTER_DOCUMENT).iterator();

    if (iterator.hasNext()) {
      lastValue = (int) iterator.next().get(LAST_VALUE_KEY);
    } else {
      collection.insertOne(counterDocument(0));
    }
  }

  public int getNextIdAndIncrement() {
    collection.findOneAndReplace(
        FILTER_DOCUMENT,
        counterDocument(++lastValue)
    );

    return lastValue;
  }

  private Document counterDocument(int value) {
    Document document = new Document();
    document.append(ID_KEY, ID_VALUE);
    document.append(LAST_VALUE_KEY, value);

    return document;
  }

}
