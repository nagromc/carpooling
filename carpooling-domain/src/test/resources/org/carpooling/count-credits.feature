Feature: Count owed trips
  We want to know the number of owed trips between carpoolers.

  Scenario: One trip is owed
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
    When the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | Alice     | 0.5    |
      | Bob       | -0.5   |

  Scenario: A returned trip between two carpoolers is cancelled
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
      | Bob    | 1         | Alice     |
    When the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | Alice     | 0      |
      | Bob       | 0      |
