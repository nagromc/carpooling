Feature: Carpooling
  Several people should be able to share a ride.

  Scenario: A driver driving alone does not owe a trip
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
    When "Alice" drives alone
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | Alice     | 0.5    |
      | Bob       | -0.5   |

  Scenario: A passenger who shares a ride owes a trip to the driver
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Bob    | 1         | Alice     |
    When "Bob" drives with:
      | Alice |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | Alice     | -1     |
      | Bob       | 1      |

  Scenario: A former passenger who rides back with a former driver doesn't change the number of owed trips
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
    When "Alice" drives with:
      | Bob |
    And "Bob" drives with:
      | Alice |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | Alice     | 0.5    |
      | Bob       | -0.5   |

  Scenario: Two passengers who share a ride owes a trip each to the driver
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
      | Alice  | 2         | Charlie   |
    When "Alice" drives with:
      | Bob     |
      | Charlie |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit       |
      | Alice     | 2.166666667  |
      | Bob       | -0.833333333 |
      | Charlie   | -1.333333333 |

  Scenario: A passenger doesn't owe a trip if not sharing a trip
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | Alice  | 1         | Bob       |
      | Alice  | 0         | Charlie   |
    When "Alice" drives with:
      | Charlie |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | Alice     | 1      |
      | Bob       | -0.5   |
      | Charlie   | -0.5   |

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
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | Alice     | -1     |
      | Bob       | 0.5    |
      | Charlie   | 0.5    |
