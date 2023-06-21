package com.khch;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ZipFileCSVComparatorTest {

    @Test
    public void testEmptySame() {
        String targetZipPath = getClass().getClassLoader().getResource("empty/a-empty.zip").getPath();
        String expectedZipPath = getClass().getClassLoader().getResource("empty/b-empty.zip").getPath();
        String targetFileName = "a-empty.xlsx";
        String expectedFileName = "b-empty.xlsx";
        try {
            boolean isSame = ZipFileCSVComparator.compareCSVFilesInZip(targetZipPath, expectedZipPath, targetFileName, expectedFileName);
            assertTrue("empty files are same", isSame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNotEmptySame() {
        String targetZipPath = getClass().getClassLoader().getResource("same/a-same.zip").getPath();
        String expectedZipPath = getClass().getClassLoader().getResource("same/b-same.zip").getPath();
        String targetFileName = "a-same.xlsx";
        String expectedFileName = "b-same.xlsx";

        try {
            boolean isSame = ZipFileCSVComparator.compareCSVFilesInZip(targetZipPath, expectedZipPath, targetFileName, expectedFileName);
            assertTrue("Not empty files are same", isSame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNotEmptyNotSame() {
        String targetZipPath = getClass().getClassLoader().getResource("diff/a-diff.zip").getPath();
        String expectedZipPath = getClass().getClassLoader().getResource("diff/b-diff.zip").getPath();
        String targetFileName = "a-diff1.xlsx";
        String expectedFileName = "b-diff2.xlsx";

        try {
            boolean isSame = ZipFileCSVComparator.compareCSVFilesInZip(targetZipPath, expectedZipPath, targetFileName, expectedFileName);
            assertFalse("Not empty files are not same", isSame);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
