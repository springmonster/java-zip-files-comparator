package com.khch;

import java.io.*;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileCSVComparator {

    public static boolean compareCSVFilesInZip(String targetZipPath, String expectedZipPath,
                                               String targetFileName, String expectedFileName) {
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
            StringBuilder diffSB = new StringBuilder();

            String line1, line2;
            int row = 0;
            while ((line1 = targetBufferedReader.readLine()) != null && (line2 = expectedBufferedREader.readLine()) != null) {
                row++;
                if (!line1.equals(line2)) {
                    String[] values1 = line1.split(",");
                    String[] values2 = line2.split(",");
                    for (int i = 0; i < Math.min(values1.length, values2.length); i++) {
                        if (!values1[i].equals(values2[i])) {
                            diffSB.append(String.format("Row %d, Column %d: %s != %s\n", row, i + 1, values1[i], values2[i]));
                        }
                    }
                }
            }

            while ((line1 = targetBufferedReader.readLine()) != null) {
                row++;
                diffSB.append(String.format("Row %d: %s\n", row, line1));
            }

            while ((line2 = expectedBufferedREader.readLine()) != null) {
                row++;
                diffSB.append(String.format("Row %d: %s\n", row, line2));
            }

            System.out.println(diffSB);

            targetBufferedReader.close();
            expectedBufferedREader.close();

            return false;
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
