Feature: Carpooling
  Several people should be able to share a ride.

  Scenario: A driver driving alone does not owe a trip
    Given "Alice" drove 1 time with "Bob"
    When "Alice" drives alone
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Bob    | 1             | Alice    |

  Scenario: A passenger who shares a ride owes a trip to the driver
    Given "Bob" drove 1 time with "Alice"
    When "Bob" drives with:
      | Alice |
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Alice  | 2             | Bob      |

  Scenario: A former passenger who rides back with a former driver doesn't change the number of owed trips
    Given "Alice" drove 1 time with "Bob"
    When "Alice" drives with:
      | Bob |
    And "Bob" drives with:
      | Alice |
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Bob    | 1             | Alice    |

  Scenario: Two passengers who share a ride owes a trip each to the driver
    Given "Alice" drove 1 time with "Bob"
    And "Alice" drove 2 times with "Charlie"
    When "Alice" drives with:
      | Bob     |
      | Charlie |
    Then the owed trips should be:
      | debtor  | nbOfOwedTrips | creditor |
      | Bob     | 2             | Alice    |
      | Charlie | 3             | Alice    |

  Scenario: A passenger doesn't owe a trip if not sharing a trip
    Given "Alice" drove 1 time with "Bob"
    And "Alice" drove 0 time with "Charlie"
    When "Alice" drives with:
      | Charlie |
    Then the owed trips should be:
      | debtor | nbOfOwedTrips | creditor |
      | Bob    | 1             | Alice    |

  Scenario: Three persons who drives together successively doesn't change the number of owed trips
    Given "Alice" drove 1 time with "Bob"
    And "Bob" drove 2 times with "Charlie"
    And "Charlie" drove 3 times with "Alice"
    When "Alice" drives with:
      | Bob     |
      | Charlie |
    And "Bob" drives with:
      | Charlie |
      | Alice   |
    And "Charlie" drives with:
      | Alice |
      | Bob   |
    Then the owed trips should be:
      | debtor  | nbOfOwedTrips | creditor |
      | Bob     | 1             | Alice    |
      | Charlie | 2             | Bob      |
      | Alice   | 3             | Charlie  |
