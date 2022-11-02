Feature: Count owed trips
  We want to know the number of owed trips between carpoolers.

  Scenario: One trip is owed
    Given the following trips:
      | date       | driver | nbOfTrips | passenger |
      | 2015-10-21 | alice  | 1         | bob       |
    When the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 0.5    |
      | bob       | -0.5   |

  Scenario: A returned trip between two carpoolers is cancelled
    Given the following trips:
      | date       | driver | nbOfTrips | passenger |
      | 2015-10-21 | alice  | 1         | bob       |
      | 2015-10-22 | bob    | 1         | alice     |
    When the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 0      |
      | bob       | 0      |
