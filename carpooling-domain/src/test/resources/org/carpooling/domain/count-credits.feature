Feature: Count owed trips
  We want to know the number of owed trips between carpoolers.

  Scenario: One trip is owed
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | alice  | 1         | bob       |
    When the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 0.5    |
      | bob       | -0.5   |

  Scenario: A returned trip between two carpoolers is cancelled
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | alice  | 1         | bob       |
      | bob    | 1         | alice     |
    When the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 0      |
      | bob       | 0      |
