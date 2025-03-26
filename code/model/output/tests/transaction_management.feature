Feature: Transaction Management
  As a bank customer
  I want to perform financial transactions securely and reliably
  So that I can manage my funds effectively

  Background:
    Given the following accounts exist:
      | accountNumber | name   | balance | currency |
      | 123456789     | Alice  | 5000.00 | USD     |
      | 987654321     | Bob    | 3000.00 | USD     |

  Scenario: Successful fund transfer
    When I transfer 1000.00 USD from account 123456789 to 987654321
    Then the transfer should be successful
    And the balance of account 123456789 should be 4000.00
    And the balance of account 987654321 should be 4000.00
    And a transfer transaction should be recorded

  Scenario: Transfer with fraudulent amount
    When I transfer 15000.00 USD from account 123456789 to 987654321
    Then the transfer should fail due to fraud detection
    And the balances should remain unchanged
    And a fraud alert should be logged

  Scenario: Insufficient funds transfer
    When I transfer 6000.00 USD from account 987654321 to 123456789
    Then the transfer should fail due to insufficient funds
    And the balances should remain unchanged

  Scenario: Account not found transfer
    When I transfer 1000.00 USD from account 999999999 to 987654321
    Then the transfer should fail
    And the error message should indicate account not found

  Scenario: Successful deposit
    When I deposit 500.00 USD into account 123456789
    Then the deposit should be successful
    And the balance of account 123456789 should be 5500.00
    And a deposit transaction should be recorded

  Scenario: Successful withdrawal
    When I withdraw 500.00 USD from account 123456789
    Then the withdrawal should be successful
    And the balance of account 123456789 should be 4500.00
    And a withdrawal transaction should be recorded

  Scenario: Insufficient funds withdrawal
    When I withdraw 6000.00 USD from account 987654321
    Then the withdrawal should fail due to insufficient funds
    And the balance should remain unchanged

  Scenario: Get all transactions
    When I retrieve all transactions
    Then I should see all recorded transactions
    And the transactions should include transfers, deposits, and withdrawals

  Scenario: Get transactions for specific account
    When I retrieve transactions for account 123456789
    Then I should see only transactions related to that account