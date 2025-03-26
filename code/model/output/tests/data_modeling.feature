Feature: Data Modeling Functionality

  As a developer
  I want to model accounts and transactions accurately
  So that the application can handle financial data correctly

  Background:
    Given the database is initialized
    And the application context is loaded

  @Account
  Scenario: Create an Account with valid data
    When I create an account with:
      | accountNumber | name      | balance | currency |
      | ACC12345      | Test User | 1000.0  | USD      |
    Then the response status should be 201
    And the account should be persisted in the database

  Scenario: Retrieve an existing Account
    Given an account with accountNumber "ACC12345" exists
    When I retrieve the account by accountNumber "ACC12345"
    Then the response status should be 200
    And the account details should match the given data

  Scenario: Update an existing Account
    Given an account with accountNumber "ACC12345" exists
    When I update the account's balance to 1500.0
    Then the response status should be 200
    And the updated account balance should be 1500.0 in the database

  Scenario: Create an Account with invalid data
    When I create an account with:
      | accountNumber | name      | balance | currency |
      | ACC12345      |           | -100    | USD      |
    Then the response status should be 400
    And the account should not be persisted in the database

  @Transaction
  Scenario: Create a Transaction with valid data
    Given an account with accountNumber "ACC12345" exists with balance 1000.0
    And an account with accountNumber "ACC67890" exists with balance 500.0
    When I create a transaction from "ACC12345" to "ACC67890" of amount 200.0
    Then the response status should be 201
    And the transaction should be recorded in the database
    And the balance of "ACC12345" should be 800.0
    And the balance of "ACC67890" should be 700.0

  Scenario: Retrieve a Transaction details
    Given a transaction with id "TRAN123" exists
    When I retrieve the transaction by id "TRAN123"
    Then the response status should be 200
    And the transaction details should match the given data

  Scenario: Attempt Transaction with Insufficient funds
    Given an account with accountNumber "ACC12345" exists with balance 100.0
    And an account with accountNumber "ACC67890" exists with balance 500.0
    When I create a transaction from "ACC12345" to "ACC67890" of amount 150.0
    Then the response status should be 400
    And the transaction should not be recorded
    And the account balances should remain unchanged

  Scenario: Create a Transaction with invalid amount
    Given an account with accountNumber "ACC12345" exists
    When I create a transaction from "ACC12345" to "ACC67890" with amount 0.0
    Then the response status should be 400
    And the transaction should not be recorded