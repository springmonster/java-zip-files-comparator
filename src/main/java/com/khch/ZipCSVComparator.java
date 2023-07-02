package com.khch;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipCSVComparator {

    /**
     * 每个zip文件中只有一个csv文件
     *
     * @param targetZipPath   目标zip文件路径
     * @param baseLineZipPath zip文件路径
     * @return true: 两个zip文件中的csv文件内容一致; false: 两个zip文件中的csv文件内容不一致
     */
    public boolean compareCSVFilesInZip(String targetZipPath, String baseLineZipPath,
                                        String targetFileName, String baseLineFileName) {
        try (ZipFile targetZipFile = new ZipFile(targetZipPath);
             ZipFile baseLineZipFile = new ZipFile(baseLineZipPath)) {

            ZipEntry targetZipFileEntry = targetZipFile.getEntry(targetFileName);
            ZipEntry baselineZipFileEntry = baseLineZipFile.getEntry(baseLineFileName);

            if (targetZipFileEntry == null || baselineZipFileEntry == null) {
                throw new FileNotFoundException("Files are not found in zip file.");
            }

            return readAndCompareEachLine(targetZipFile, baseLineZipFile, targetZipFileEntry, baselineZipFileEntry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean readAndCompareEachLine(ZipFile targetZipFile, ZipFile baselineZipFile,
                                          ZipEntry targetZipFileEntry, ZipEntry baselineZipFileEntry) {
        InputStream targetZipFileInputStream1 = null;
        InputStream baselineZipFileInputStream1 = null;
        BufferedReader targetBufferedReader = null;
        BufferedReader baselineBufferedReader = null;
        boolean isAllSame;

        try {
            // 读取baseline文件
            baselineZipFileInputStream1 = baselineZipFile.getInputStream(baselineZipFileEntry);
            baselineBufferedReader = new BufferedReader(new InputStreamReader(baselineZipFileInputStream1));

            Map<String, Integer> baselineMap = new HashMap<>();
            String baselineLine;
            while ((baselineLine = baselineBufferedReader.readLine()) != null) {
                baselineMap.put(baselineLine, baselineMap.getOrDefault(baselineLine, 0) + 1);
            }

            // 读取target文件
            targetZipFileInputStream1 = targetZipFile.getInputStream(targetZipFileEntry);
            targetBufferedReader = new BufferedReader(new InputStreamReader(targetZipFileInputStream1));

            List<String> targetList = new ArrayList<>();
            String targetLineLine;
            while ((targetLineLine = targetBufferedReader.readLine()) != null) {
                targetList.add(targetLineLine);
            }

            // 比较
            isAllSame = compare(baselineMap, targetList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (targetBufferedReader != null) {
                    targetBufferedReader.close();
                }
                if (baselineBufferedReader != null) {
                    baselineBufferedReader.close();
                }

                if (targetZipFileInputStream1 != null) {
                    targetZipFileInputStream1.close();
                }
                if (baselineZipFileInputStream1 != null) {
                    baselineZipFileInputStream1.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return isAllSame;
    }

    /**
     * 比较两个文件内容是否一致
     * <p>
     * 1. baselineMap中的key为每行的内容，value为每行的数量
     * 2. targetList为每行的内容
     * 3. 比较的过程中，如果targetList中的内容在baselineMap中不存在，则返回false
     * 4. 如果targetList中的内容在baselineMap中存在，则将baselineMap中的value减1，如果减1后的value为0，则将该key从baselineMap中删除
     * 5. 比较结束后，如果baselineMap为空，则返回true，否则返回false
     *
     * @param baselineMap key为每行的内容，value为每行的数量
     * @param targetList  每行的内容
     * @return true: 两个文件内容一致; false: 两个文件内容不一致
     */
    public boolean compare(Map<String, Integer> baselineMap, List<String> targetList) {
        int baselineSize = 0;
        for (Integer value : baselineMap.values()) {
            baselineSize += value;
        }

        if (baselineSize != targetList.size()) {
            return false;
        }

        for (String targetLine : targetList) {
            if (!baselineMap.containsKey(targetLine)) {
                return false;
            } else {
                baselineMap.put(targetLine, baselineMap.get(targetLine) - 1);
                if (baselineMap.get(targetLine) == 0) {
                    baselineMap.remove(targetLine);
                }
            }
        }

        return baselineMap.isEmpty();
    }
}
