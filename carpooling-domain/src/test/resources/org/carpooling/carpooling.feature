Feature: Carpooling
  Several people should be able to share a ride.

  Scenario: A driver driving alone does not owe a trip
    Given "Alice" drove 1 time with "Bob"
    When "Alice" drives with "nobody"
    Then "Bob" should owe 1 trip to "Alice"

  Scenario: A passenger who shares a ride owes a trip to the driver
    Given "Bob" drove 1 time with "Alice"
    When "Bob" drives with "Alice"
    Then "Alice" should owe 2 trips to "Bob"

  Scenario: A former passenger who rides back with a former driver doesn't change the number of owed trips
    Given "Alice" drove 1 time with "Bob"
    When "Alice" drives with "Bob"
    And "Bob" drives with "Alice"
    Then "Bob" should owe 1 trip to "Alice"

  Scenario: Two passengers who share a ride owes a trip each to the driver
    Given "Alice" drove 1 time with "Bob"
    And "Alice" drove 2 times with "Charlie"
    When "Alice" drives with:
      | Bob     |
      | Charlie |
    Then "Bob" should owe 2 trips to "Alice"
    And "Charlie" should owe 3 trips to "Alice"

  Scenario: A passenger doesn't owe a trip if not sharing a trip
    Given "Alice" drove 1 time with "Bob"
    And "Alice" drove 0 time with "Charlie"
    When "Alice" drives with "Charlie"
    Then "Bob" should owe 1 trip to "Alice"

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
    Then "Bob" should owe 1 trip to "Alice"
    And "Charlie" should owe 2 trips to "Bob"
    And "Alice" should owe 3 trips to "Charlie"
