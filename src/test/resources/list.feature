Feature: Verify file contents

  Scenario Outline: Verify file <targetPath> & <expectedPath> content match
    Given a list of target file paths
      | <targetPath> |
    Given a list of expected file paths
      | <expectedPath> |
    When I compare the file contents
    Then the file contents should match
      | <result> |

    Examples:
      | targetPath                                                                                 | expectedPath                                                                                 | result |
      | /Users/kuanghaochuan/Projects/ZipComparator/src/test/resources/cucumber/target-a-empty.zip | /Users/kuanghaochuan/Projects/ZipComparator/src/test/resources/cucumber/expected-a-empty.zip | true   |
      | /Users/kuanghaochuan/Projects/ZipComparator/src/test/resources/cucumber/target-a-same.zip  | /Users/kuanghaochuan/Projects/ZipComparator/src/test/resources/cucumber/expected-a-same.zip  | true   |
