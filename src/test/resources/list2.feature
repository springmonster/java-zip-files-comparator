Feature: Verify files content in folder

  Scenario Outline: Verify files in <targetFolder> & <expectedFolder> content match
    Given target folder
      | <targetFolder> |
    And expected folder
      | <expectedFolder> |
    Then compare

    Examples:
      | targetFolder                                                                   | expectedFolder                                                                   |
      | /Users/kuanghaochuan/Projects/ZipComparator/src/test/resources/cucumber/target | /Users/kuanghaochuan/Projects/ZipComparator/src/test/resources/cucumber/expected |
