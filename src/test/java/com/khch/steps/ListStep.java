package com.khch.steps;

import com.khch.ZipFileCSVComparator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.File;

public class ListStep {
    private String targetPath;
    private String expectedPath;

    private boolean isSame;

    private ZipFileCSVComparator zipFileCSVComparator = new ZipFileCSVComparator();

    @Given("a list of target file paths")
    public void a_list_of_target_file_paths(io.cucumber.datatable.DataTable dataTable) {
        this.targetPath = dataTable.asList().get(0);
    }

    @Given("a list of expected file paths")
    public void a_list_of_expected_file_paths(io.cucumber.datatable.DataTable dataTable) {
        this.expectedPath = dataTable.asList().get(0);
    }

    @When("I compare the file contents")
    public void iCompareTheFileContents() {
        File targetFile = new File(targetPath);
        File expectedFile = new File(expectedPath);

        String targetZipFileName = targetFile.getName().replace("target-", "");
        String expectedZipFileName = expectedFile.getName().replace("expected-", "");

        String targetFileName = targetZipFileName.replace(".zip", ".xlsx");
        String expectedFileName = expectedZipFileName.replace(".zip", ".xlsx");

        isSame = zipFileCSVComparator.compareCSVFilesInZip(targetPath, expectedPath, targetFileName, expectedFileName);
    }

    @Then("the file contents should match")
    public void the_file_contents_should_match(io.cucumber.datatable.DataTable dataTable) {
        System.out.println("isSame = " + isSame);
        System.out.println(isSame == Boolean.parseBoolean(dataTable.asList().get(0)));
    }
}
