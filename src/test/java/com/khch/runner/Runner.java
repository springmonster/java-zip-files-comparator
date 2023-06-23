package com.khch.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {
        "src/test/resources/list.feature",
},
        glue = "com.khch.steps")
public class Runner extends AbstractTestNGCucumberTests {
}
