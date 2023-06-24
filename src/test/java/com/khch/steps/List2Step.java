package com.khch.steps;

import com.khch.ZipFileCSVComparator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class List2Step {

    private String targetFolder;
    private String expectedFolder;

    private ZipFileCSVComparator zipFileCSVComparator = new ZipFileCSVComparator();

    @Given("target folder")
    public void target_folder(io.cucumber.datatable.DataTable dataTable) {
        this.targetFolder = dataTable.asList().get(0);
    }

    @Given("expected folder")
    public void expected_folder(io.cucumber.datatable.DataTable dataTable) {
        this.expectedFolder = dataTable.asList().get(0);
    }

    @Then("compare")
    public void compare() {
        // 获取target文件夹下的所有文件名
        File target = new File(this.targetFolder);
        File[] targetFiles = target.listFiles();
        List<String> targetNames = Arrays.stream(targetFiles)
                .map(file -> file.getName().replace("target-", ""))
                .collect(Collectors.toList());

        // 获取expected文件夹下的所有文件名
        // 然后过滤掉target文件夹下没有的文件名
        File expected = new File(this.expectedFolder);
        File[] expectedFiles = expected.listFiles();
        List<String> expectedNames = Arrays.stream(expectedFiles)
                .map(file -> file.getName().replace("expected-", ""))
                .filter(targetNames::contains)
                .collect(Collectors.toList());

        // 比较
        for (String expectedName : expectedNames) {
            boolean b = zipFileCSVComparator.compareCSVFilesInZip(this.targetFolder + File.separator + "target-" + expectedName,
                    this.expectedFolder + File.separator + "expected-" + expectedName,
                    expectedName.replace(".zip", ".xlsx"),
                    expectedName.replace(".zip", ".xlsx"));

            StringBuilder sb = new StringBuilder();
            sb.append("target-").append(expectedName).append(" vs ").append("expected-").append(expectedName);
            sb.append(" isSame = ").append(b);
            System.out.println(sb);
        }
    }
}
