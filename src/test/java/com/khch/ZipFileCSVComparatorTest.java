package com.khch;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ZipFileCSVComparatorTest {

    ZipFileCSVComparator zipFileCSVComparator;

    @Before
    public void setUp() {
        zipFileCSVComparator = new ZipFileCSVComparator();
    }

    @Test
    public void testEmptySame() throws IOException, NoSuchAlgorithmException {
        String targetZipPath = getClass().getClassLoader().getResource("empty/a-empty.zip").getPath();
        String expectedZipPath = getClass().getClassLoader().getResource("empty/b-empty.zip").getPath();
        String targetFileName = new File(targetZipPath).getName().replace(".zip", ".xlsx");
        String expectedFileName = new File(expectedZipPath).getName().replace(".zip", ".xlsx");

        boolean isSame = zipFileCSVComparator.compareCSVFilesInZip(targetZipPath, expectedZipPath, targetFileName, expectedFileName);
        assertTrue("Empty files are same", isSame);
    }

    @Test
    public void testNotEmptySame() throws IOException, NoSuchAlgorithmException {
        String targetZipPath = getClass().getClassLoader().getResource("same/a-same.zip").getPath();
        String expectedZipPath = getClass().getClassLoader().getResource("same/b-same.zip").getPath();
        String targetFileName = new File(targetZipPath).getName().replace(".zip", ".xlsx");
        String expectedFileName = new File(expectedZipPath).getName().replace(".zip", ".xlsx");

        boolean isSame = zipFileCSVComparator.compareCSVFilesInZip(targetZipPath, expectedZipPath, targetFileName, expectedFileName);
        assertTrue("Not empty files are same", isSame);
    }

    @Test
    public void testNotEmptyNotSame() throws IOException, NoSuchAlgorithmException {
        String targetZipPath = getClass().getClassLoader().getResource("diff/a-diff1.zip").getPath();
        String expectedZipPath = getClass().getClassLoader().getResource("diff/b-diff2.zip").getPath();
        String targetFileName = new File(targetZipPath).getName().replace(".zip", ".xlsx");
        String expectedFileName = new File(expectedZipPath).getName().replace(".zip", ".xlsx");

        boolean isSame = zipFileCSVComparator.compareCSVFilesInZip(targetZipPath, expectedZipPath, targetFileName, expectedFileName);
        assertFalse("Not empty files are not same", isSame);
    }

    @Test
    public void testNotEmptyNotSameCSVFiles() throws IOException, NoSuchAlgorithmException {
        String targetZipPath = getClass().getClassLoader().getResource("diff/a-diff1_csv.zip").getPath();
        String expectedZipPath = getClass().getClassLoader().getResource("diff/b-diff2_csv.zip").getPath();
        String targetFileName = new File(targetZipPath).getName().replace(".zip", ".csv");
        String expectedFileName = new File(expectedZipPath).getName().replace(".zip", ".csv");

        boolean isSame = zipFileCSVComparator.compareCSVFilesInZip(targetZipPath, expectedZipPath, targetFileName, expectedFileName);
        assertFalse("Not empty files are not same", isSame);
    }
}
