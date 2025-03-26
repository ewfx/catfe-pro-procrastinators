Feature: Account Management
  As a user of the banking API
  I want to manage bank accounts
  So that I can perform basic account operations

  Background:
    Given the application is running

  Scenario: Successful account creation
    When I send a POST request to create a new account with:
      | accountNumber | name | balance | currency |
      | 123456789    | Alice | 5000.00 | USD     |
    Then the response status should be 200
    And the response should include:
      | status  | message                | accountNumber | balance | currency |
      | success | Account created successfully | 123456789    | 5000.00 | USD     |

  Scenario: Attempting to create an existing account
    Given an account with number "123456789" already exists
    When I send a POST request to create an account with:
      | accountNumber | name | balance | currency |
      | 123456789    | Alice | 5000.00 | USD     |
    Then the response status should be 200
    And the response should include:
      | status | message       |
      | failed | Account already exists |

  Scenario: Getting balance for an existing account
    Given an account with number "123456789" exists
    When I send a GET request to "/api/v1/accounts/123456789/balance"
    Then the response status should be 200
    And the response should include:
      | status  | accountNumber | balance | currency |
      | success | 123456789    | 5000.00 | USD     |

  Scenario: Getting balance for a non-existent account
    When I send a GET request to "/api/v1/accounts/987654321/balance"
    Then the response status should be 200
    And the response should include:
      | status | message       |
      | failed | Account not found |

  Scenario: Retrieving all accounts
    Given the following accounts exist:
      | accountNumber | name | balance | currency |
      | 123456789    | Alice | 5000.00 | USD     |
      | 987654321    | Bob   | 1000.00 | EUR     |
    When I send a GET request to "/api/v1/accounts"
    Then the response status should be 200
    And the response should include:
      | status | accounts |
      | success | 2       |

  Scenario: Retrieving a specific account
    Given an account with number "123456789" exists
    When I send a GET request to "/api/v1/accounts/123456789"
    Then the response status should be 200
    And the response should include the account details:
      | accountNumber | name | balance | currency |
      | 123456789    | Alice | 5000.00 | USD     |

  Scenario: Attempting to retrieve a non-existent account
    When I send a GET request to "/api/v1/accounts/987654321"
    Then the response status should be 404
    And the response should be empty

  Scenario: Successful account deletion
    Given an account with number "123456789" exists
    When I send a DELETE request to "/api/v1/accounts/123456789"
    Then the response status should be 200
    And the response should include:
      | message                |
      | Account deleted successfully. |

  Scenario: Attempting to delete a non-existent account
    When I send a DELETE request to "/api/v1/accounts/987654321"
    Then the response status should be 404
    And the response should include:
      | Account not found. |