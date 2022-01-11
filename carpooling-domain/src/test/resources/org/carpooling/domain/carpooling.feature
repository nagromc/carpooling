Feature: Carpooling
  Several people should be able to share a ride.

  Scenario: A driver driving alone does not owe a trip
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | alice  | 1         | bob       |
    When "alice" drives alone
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 0.5    |
      | bob       | -0.5   |

  Scenario: A passenger who shares a ride owes a trip to the driver
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | bob    | 1         | alice     |
    When "bob" drives with:
      | alice |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | -1     |
      | bob       | 1      |

  Scenario: A former passenger who rides back with a former driver doesn't change the number of owed trips
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | alice  | 1         | bob       |
    When "alice" drives with:
      | bob |
    And "bob" drives with:
      | alice |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 0.5    |
      | bob       | -0.5   |

  Scenario: Two passengers who share a ride owes a trip each to the driver
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | alice  | 1         | bob       |
      | alice  | 2         | charlie   |
    When "alice" drives with:
      | bob     |
      | charlie |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit       |
      | alice     | 2.166666667  |
      | bob       | -0.833333333 |
      | charlie   | -1.333333333 |

  Scenario: A passenger doesn't owe a trip if not sharing a trip
    Given the following trips:
      | driver | nbOfTrips | passenger |
      | alice  | 1         | bob       |
      | alice  | 0         | charlie   |
    When "alice" drives with:
      | charlie |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 1      |
      | bob       | -0.5   |
      | charlie   | -0.5   |

  Scenario: Three persons who drives together successively doesn't change the number of owed trips
    Given the following trips:
      | driver  | nbOfTrips | passenger |
      | alice   | 1         | bob       |
      | bob     | 2         | charlie   |
      | charlie | 3         | alice     |
    When "alice" drives with:
      | bob     |
      | charlie |
    And "bob" drives with:
      | charlie |
      | alice   |
    And "charlie" drives with:
      | alice |
      | bob   |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | -1     |
      | bob       | 0.5    |
      | charlie   | 0.5    |
