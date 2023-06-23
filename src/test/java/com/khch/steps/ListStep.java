package com.khch.steps;

import com.khch.ZipFileCSVComparator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListStep {

    private List<String> targetFilePaths;
    private List<String> expectedFilePaths;

    private final ZipFileCSVComparator zipFileCSVComparator = new ZipFileCSVComparator();

    @Given("a list of target file paths:")
    public void a_list_of_target_file_paths(io.cucumber.datatable.DataTable dataTable) {
        this.targetFilePaths = new ArrayList<>();
        List<List<String>> data = dataTable.asLists(String.class);
        for (List<String> row : data) {
            for (String path : row) {
                Collections.addAll(targetFilePaths, path.split(",\\s*"));
            }
        }
    }

    @And("a list of expected file paths:")
    public void a_list_of_expected_file_paths(io.cucumber.datatable.DataTable dataTable) {
        this.expectedFilePaths = new ArrayList<>();
        List<List<String>> data = dataTable.asLists(String.class);
        for (List<String> row : data) {
            for (String path : row) {
                Collections.addAll(expectedFilePaths, path.split(",\\s*"));
            }
        }
    }

    @Then("I compare the file contents")
    public void i_compare_the_file_contents() {
        for (int i = 0; i < targetFilePaths.size(); i++) {
            String targetFilePath = targetFilePaths.get(i);
            File targetFile = new File(targetFilePath);

            String transformedTargetFileName = targetFile.getName().replace("target", "expected");

            String expectedFilePath = expectedFilePaths.get(i);
            File expectedFile = new File(expectedFilePath);
            String expectedFileName = expectedFile.getName();

            assert transformedTargetFileName.equals(expectedFileName);

            String targetExcelName = targetFile.getName().replace(".zip", ".xlsx").replace("target-", "");
            String expectedExcelName = expectedFile.getName().replace(".zip", ".xlsx").replace("expected-", "");

            assert targetExcelName.equals(expectedExcelName);

            if (!zipFileCSVComparator.compareCSVFilesInZip(targetFilePath, expectedFilePath, targetExcelName, expectedExcelName)) {
                throw new RuntimeException("File contents are not same.");
            }
        }
        System.out.println("File contents are same.");
    }
}
