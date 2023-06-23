Feature: Verify file contents

  Scenario Outline: Verify file contents match
    Given a list of target file paths:
      | <targetPaths> |
    And a list of expected file paths:
      | <expectedPaths> |
    Then I compare the file contents

    Examples:
      | targetPaths                                                                                                                                                                           | expectedPaths                                                                                                                                                                             |
      | /Users/kuanghaochuan/Projects/ZipComparator/src/test/resources/cucumber/target-a-empty.zip, /Users/kuanghaochuan/Projects/ZipComparator/src/test/resources/cucumber/target-a-same.zip | /Users/kuanghaochuan/Projects/ZipComparator/src/test/resources/cucumber/expected-a-empty.zip, /Users/kuanghaochuan/Projects/ZipComparator/src/test/resources/cucumber/expected-a-same.zip |
