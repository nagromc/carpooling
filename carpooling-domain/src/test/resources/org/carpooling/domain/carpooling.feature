Feature: Carpooling
  Several people should be able to share a ride.

  Scenario: A driver driving alone does not owe a trip
    Given the following trips:
      | date       | driver | nbOfTrips | passenger |
      | 2015-10-21 | alice  | 1         | bob       |
    When "alice" drives on "2015-10-22" alone
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 0.5    |
      | bob       | -0.5   |

  Scenario: A passenger who shares a ride owes a trip to the driver
    Given the following trips:
      | date       | driver | nbOfTrips | passenger |
      | 2015-10-21 | bob    | 1         | alice     |
    When "bob" drives on "2015-10-22" with:
      | alice |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | -1     |
      | bob       | 1      |

  Scenario: A former passenger who rides back with a former driver doesn't change the number of owed trips
    Given the following trips:
      | date       | driver | nbOfTrips | passenger |
      | 2015-10-21 | alice  | 1         | bob       |
    When "alice" drives on "2015-10-22" with:
      | bob |
    And "bob" drives on "2015-10-23" with:
      | alice |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 0.5    |
      | bob       | -0.5   |

  Scenario: Two passengers who share a ride owes a trip each to the driver
    Given the following trips:
      | date       | driver | nbOfTrips | passenger |
      | 2015-10-21 | alice  | 1         | bob       |
      | 2015-10-22 | alice  | 2         | charlie   |
    When "alice" drives on "2015-10-23" with:
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
      | date       | driver | nbOfTrips | passenger |
      | 2015-10-21 | alice  | 1         | bob       |
      | 2015-10-22 | alice  | 0         | charlie   |
    When "alice" drives on "2015-10-23" with:
      | charlie |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 1      |
      | bob       | -0.5   |
      | charlie   | -0.5   |

  Scenario: Three persons who drives together successively doesn't change the number of owed trips
    Given the following trips:
      | date       | driver  | nbOfTrips | passenger |
      | 2015-10-21 | alice   | 1         | bob       |
      | 2015-10-22 | bob     | 2         | charlie   |
      | 2015-10-23 | charlie | 3         | alice     |
    When "alice" drives on "2015-10-24" with:
      | bob     |
      | charlie |
    And "bob" drives on "2015-10-25" with:
      | charlie |
      | alice   |
    And "charlie" drives on "2015-10-26" with:
      | alice |
      | bob   |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | -1     |
      | bob       | 0.5    |
      | charlie   | 0.5    |

  Scenario: Two drivers driving alone do not owe a trip
    Given the following trips:
      | date       | driver | nbOfTrips | passenger |
      | 2015-10-21 | alice  | 1         | bob       |
    When "alice" and "bob" drive on "2015-10-22" alone
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 0.5    |
      | bob       | -0.5   |

  Scenario: Two drivers share the earnings
    Given the following trips:
      | date       | driver | nbOfTrips | passenger |
      | 2015-10-21 | alice  | 1         | bob       |
      | 2015-10-22 | dave   | 2         | charlie   |
    When "alice" and "bob" drive on "2015-10-23" with:
      | charlie |
      | dave    |
      | erin    |
    And the credits are counted
    Then the credits should be:
      | carpooler | credit |
      | alice     | 1.1    |
      | bob       | 0.1    |
      | charlie   | -1.4   |
      | dave      | 0.6    |
      | erin      | -0.4   |
