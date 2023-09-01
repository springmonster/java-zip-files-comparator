package com.khch.apache.csv;

import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

// https://www.baeldung.com/apache-commons-csv
public class ApacheCommonsCSVTest {

  final Map<String, String> AUTHOR_BOOK_MAP = new HashMap<String, String>() {
    {
      put("Dan Simmons", "Hyperion");
      put("Douglas Adams", "The Hitchhiker's Guide to the Galaxy");
    }
  };

  final String[] HEADERS = {"author", "title"};

  @Test
  public void givenCSVFile_whenRead_thenContentsAsExpected() throws IOException {
    final Reader in = new FileReader("src/test/resources/apache/csv/book.csv");

    final CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
        .setHeader(HEADERS)
        .setSkipHeaderRecord(true)
        .build();

    final Iterable<CSVRecord> records = csvFormat.parse(in);

    for (final CSVRecord record : records) {
      final String author = record.get("author");
      final String title = record.get("title");
      assertEquals(AUTHOR_BOOK_MAP.get(author), title);
    }
  }

  @Test
  public void givenAuthorBookMap_whenWrittenToStream_thenOutputStreamAsExpected()
      throws IOException {
    final StringWriter sw = new StringWriter();

    final CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
        .setHeader(HEADERS)
        .build();

    try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
      AUTHOR_BOOK_MAP.forEach((author, title) -> {
        try {
          printer.printRecord(author, title);
        } catch (final IOException e) {
          e.printStackTrace();
        }
      });
    }
    System.out.println(sw.toString().trim());
  }
}
