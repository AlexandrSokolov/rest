package com.savdev.rest.commons.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.io.TempDir;


public abstract class BaseTest {

  @TempDir
  Path tempTestFolder;

  @BeforeAll
  public static void initAllTests() {
    System.out.println("BaseTest init");
  }

  public InputStream testInputStream(String filePath) {
    try {
      String absolutePath = testFileAbsolutePath(filePath);
      return new FileInputStream(absolutePath);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public String testFileAbsolutePath(String filePath) {
    try {
      String withoutFirstSlash = filePath.startsWith(File.separator) ?
        filePath.substring(File.separator.length()) : filePath;

      URL pathResource = getClass()
        .getClassLoader()
        .getResource(withoutFirstSlash);
      if (pathResource == null) {
        throw new IllegalStateException(
          "Could not find resource for the path in test resources: [" + filePath + "]");
      }
      return new File(pathResource.toURI()).getAbsolutePath();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  public File tempTestFile(final String fileName){
    return new File(tempTestFolder.toFile(), fileName);
  }
}