package com.khch.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {
        "src/test/resources/list.feature",
        "src/test/resources/list2.feature"
},
        glue = "com.khch.steps")
public class Runner extends AbstractTestNGCucumberTests {
}
