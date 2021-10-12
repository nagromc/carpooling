Feature: Carpooling
  Several people should be able to share a ride.

  Scenario: One trip is owed
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
    When the owed trips are counted
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Bob    | 1             | Alice    |

  Scenario: A driver driving alone does not owe a trip
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
    When "Alice" drives alone
    And the owed trips are counted
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Bob    | 1             | Alice    |

  Scenario: A passenger who shares a ride owes a trip to the driver
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Bob    | 1         | Alice     |
    When "Bob" drives with:
      | Alice |
    And the owed trips are counted
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Alice  | 2             | Bob      |

  Scenario: A former passenger who rides back with a former driver doesn't change the number of owed trips
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
    When "Alice" drives with:
      | Bob |
    And "Bob" drives with:
      | Alice |
    And the owed trips are counted
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Bob    | 1             | Alice    |

  Scenario: Two passengers who share a ride owes a trip each to the driver
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
      | Alice  | 2         | Charlie   |
    When "Alice" drives with:
      | Bob     |
      | Charlie |
    And the owed trips are counted
    Then the owed trips should be:
      | debtor  | nbOfOwedTrips | creditor |
      | Bob     | 2             | Alice    |
      | Charlie | 3             | Alice    |

  Scenario: A passenger doesn't owe a trip if not sharing a trip
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
      | Alice  | 0         | Charlie   |
    When "Alice" drives with:
      | Charlie |
    And the owed trips are counted
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Bob    | 1             | Alice    |

  Scenario: Three persons who drives together successively doesn't change the number of owed trips
    Given the following trips:
      | driver  | nbOfTrips | passenger |
      | Alice   | 1         | Bob       |
      | Bob     | 2         | Charlie   |
      | Charlie | 3         | Alice     |
    When "Alice" drives with:
      | Bob     |
      | Charlie |
    And "Bob" drives with:
      | Charlie |
      | Alice   |
    And "Charlie" drives with:
      | Alice |
      | Bob   |
    And the owed trips are counted
    Then the owed trips should be:
      | debtor  | nbOfOwedTrips | creditor |
      | Bob     | 1             | Alice    |
      | Charlie | 2             | Bob      |
      | Alice   | 3             | Charlie  |
