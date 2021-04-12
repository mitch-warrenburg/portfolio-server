package com.mw.portfolio.util;

import static java.util.Objects.isNull;
import static org.apache.commons.io.FileUtils.touch;
import static org.apache.commons.io.FileUtils.writeStringToFile;

import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.util.Base64;

public class KeyFileUtil {

  @SneakyThrows
  public static void writeGcpKeyFile() {

    val encodedFileContents = System.getenv("ENCODED_GOOGLE_APPLICATION_CREDENTIALS");

    if (isNull(encodedFileContents)) {
      throw new RuntimeException("GOOGLE_APPLICATION_CREDENTIALS not found.");
    }

    val fileContents = new String(Base64.getDecoder().decode(encodedFileContents));
    File keyFile = new File("/tmp/key.json");

    touch(keyFile);
    writeStringToFile(keyFile, fileContents, "UTF-8");
  }
}
