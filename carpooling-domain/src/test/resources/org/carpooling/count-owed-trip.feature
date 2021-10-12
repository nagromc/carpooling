Feature: Count owed trips
  We want to know the number of owed trips between carpoolers.

  Scenario: One trip is owed
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
    When the owed trips are counted
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Bob    | 1             | Alice    |

  Scenario: A returned trip between two carpoolers is cancelled
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
      | Bob    | 1         | Alice     |
    When the owed trips are counted
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Alice  | 0             | Bob      |
      | Bob    | 0             | Alice    |
