Feature: Fraud Detection and Prevention
  As a banking system
  I want to detect and prevent fraudulent transactions
  So that I can protect users' accounts from unauthorized activities

  Background:
    Given the fraud threshold is $10,000.00

  Scenario: Fraudulent transaction is detected and blocked
    When a transaction of $15,000.00 is attempted
    Then the transaction should be blocked
    And the system should log a fraud warning
    And the response should indicate fraud detection

  Scenario: Legitimate transaction is processed successfully
    When a transaction of $5,000.00 is attempted
    Then the transaction should be processed successfully
    And the system should not log a fraud warning
    And the response should indicate success

  Scenario: Transaction exactly at the fraud threshold
    When a transaction of $10,000.00 is attempted
    Then the transaction should be blocked
    And the system should log a fraud warning
    And the response should indicate fraud detection

  Scenario: Multiple transactions with mixed amounts
    Given there are multiple transactions with amounts:
      | amount   |
      | 5000.00  |
      | 12000.00 |
      | 9999.99  |
    When processing these transactions
    Then transactions below or equal to $10,000.00 should be processed
    And transactions above $10,000.00 should be blocked
    And appropriate fraud warnings should be logged