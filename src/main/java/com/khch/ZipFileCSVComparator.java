package com.khch;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileCSVComparator {

    public static boolean compareCSVFilesInZip(String targetZipPath, String expectedZipPath,
                                               String targetFileName, String expectedFileName) throws IOException, NoSuchAlgorithmException {
        try (ZipFile targetZipFile = new ZipFile(targetZipPath);
             ZipFile expectedZipFile = new ZipFile(expectedZipPath)) {
            ZipEntry targetZipFileEntry = targetZipFile.getEntry(targetFileName);
            ZipEntry expectedZipFileEntry = expectedZipFile.getEntry(expectedFileName);

            if (targetZipFileEntry == null || expectedZipFileEntry == null) {
                throw new Exception("Files are not found in zip file.");
            }

            InputStream targetZipFileInputStream = targetZipFile.getInputStream(targetZipFileEntry);
            InputStream expectedZipFileInputStream = expectedZipFile.getInputStream(expectedZipFileEntry);

            byte[] targetMd5 = MessageDigest.getInstance("MD5").digest(readAllBytes(targetZipFileInputStream));
            byte[] expectedMd5 = MessageDigest.getInstance("MD5").digest(readAllBytes(expectedZipFileInputStream));
            targetZipFileInputStream.close();
            expectedZipFileInputStream.close();

            if (MessageDigest.isEqual(targetMd5, expectedMd5)) {
                return true;
            }

            InputStream targetZipFileInputStream1 = targetZipFile.getInputStream(targetZipFileEntry);
            InputStream expectedZipFileInputStream1 = expectedZipFile.getInputStream(expectedZipFileEntry);

            BufferedReader targetBufferedReader = new BufferedReader(new InputStreamReader(targetZipFileInputStream1));
            BufferedReader expectedBufferedREader = new BufferedReader(new InputStreamReader(expectedZipFileInputStream1));
            StringBuilder targetSB = new StringBuilder();
            StringBuilder expectedSB = new StringBuilder();

            String line1, line2;
            while ((line1 = targetBufferedReader.readLine()) != null
                    && (line2 = expectedBufferedREader.readLine()) != null) {
                targetSB.append(line1);
                expectedSB.append(line2);
            }
            targetBufferedReader.close();
            expectedBufferedREader.close();

            return targetSB.toString().contentEquals(expectedSB.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        return baos.toByteArray();
    }
}
