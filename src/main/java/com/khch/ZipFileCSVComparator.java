package com.khch;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileCSVComparator {

    public boolean compareCSVFilesInZip(String targetZipPath, String expectedZipPath,
                                        String targetFileName, String expectedFileName) {
        try (ZipFile targetZipFile = new ZipFile(targetZipPath);
             ZipFile expectedZipFile = new ZipFile(expectedZipPath)) {

            ZipEntry targetZipFileEntry = targetZipFile.getEntry(targetFileName);
            ZipEntry expectedZipFileEntry = expectedZipFile.getEntry(expectedFileName);

            if (targetZipFileEntry == null || expectedZipFileEntry == null) {
                throw new FileNotFoundException("Files are not found in zip file.");
            }

            if (checkMD5IsSame(targetZipFile, expectedZipFile, targetZipFileEntry, expectedZipFileEntry)) {
                return true;
            }

            if (compareEachLine(targetZipFile, expectedZipFile, targetZipFileEntry, expectedZipFileEntry)) {
                return true;
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkMD5IsSame(ZipFile targetZipFile, ZipFile expectedZipFile, ZipEntry targetZipFileEntry, ZipEntry expectedZipFileEntry) throws IOException, NoSuchAlgorithmException {
        InputStream targetZipFileInputStream = targetZipFile.getInputStream(targetZipFileEntry);
        InputStream expectedZipFileInputStream = expectedZipFile.getInputStream(expectedZipFileEntry);

        byte[] targetMd5 = MessageDigest.getInstance("MD5").digest(readAllBytes(targetZipFileInputStream));
        byte[] expectedMd5 = MessageDigest.getInstance("MD5").digest(readAllBytes(expectedZipFileInputStream));
        targetZipFileInputStream.close();
        expectedZipFileInputStream.close();

        if (MessageDigest.isEqual(targetMd5, expectedMd5)) {
            return true;
        }
        return false;
    }

    private byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        return baos.toByteArray();
    }

    public boolean compareEachLine(ZipFile targetZipFile, ZipFile expectedZipFile,
                                   ZipEntry targetZipFileEntry, ZipEntry expectedZipFileEntry) {
        InputStream targetZipFileInputStream1 = null;
        InputStream expectedZipFileInputStream1 = null;
        BufferedReader targetBufferedReader = null;
        BufferedReader expectedBufferedREader = null;
        boolean isAllSame = true;

        try {
            targetZipFileInputStream1 = targetZipFile.getInputStream(targetZipFileEntry);
            expectedZipFileInputStream1 = expectedZipFile.getInputStream(expectedZipFileEntry);

            targetBufferedReader = new BufferedReader(new InputStreamReader(targetZipFileInputStream1));
            expectedBufferedREader = new BufferedReader(new InputStreamReader(expectedZipFileInputStream1));

            List<String> targetList = new ArrayList<>();
            List<String> expectedList = new ArrayList<>();

            String line1, line2;
            while ((line1 = targetBufferedReader.readLine()) != null) {
                targetList.add(line1);
            }
            while ((line2 = expectedBufferedREader.readLine()) != null) {
                expectedList.add(line2);
            }

            if (targetList.size() != expectedList.size()) {
                System.out.println("Target file row count: " + targetList.size() + " and expected file row count: " + expectedList.size());
                return false;
            }

            for (int i = 0; i < targetList.size(); i++) {
                String targetLine = targetList.get(i);
                boolean isSame = false;
                for (int j = 0; j < expectedList.size(); j++) {
                    String expectedLine = expectedList.get(j);
                    if (targetLine.equals(expectedLine)) {
                        isSame = true;
                        break;
                    }
                }
                if (!isSame) {
                    isAllSame = false;
                    System.out.println("Diff target file row " + (i + 1) + ": " + targetLine);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (targetBufferedReader != null) {
                    targetBufferedReader.close();
                }
                if (expectedBufferedREader != null) {
                    expectedBufferedREader.close();
                }

                if (targetZipFileInputStream1 != null) {
                    targetZipFileInputStream1.close();
                }
                if (expectedZipFileInputStream1 != null) {
                    expectedZipFileInputStream1.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return isAllSame;
    }
}
