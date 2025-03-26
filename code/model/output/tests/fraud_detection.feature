Feature: Fraud Detection
  As a financial transaction processing system
  I want to automatically detect potentially fraudulent transactions based on a predefined threshold
  So that I can prevent financial losses due to fraudulent activities

  Background:
    Given the fraud detection threshold is set to $10,000.00

  Scenario: Transaction amount is below the threshold
    When a transaction of $5,000.00 is processed
    Then the system should not flag it as fraudulent
    And the fraud detection result should be false

  Scenario: Transaction amount equals the threshold
    When a transaction of $10,000.00 is processed
    Then the system should flag it as fraudulent
    And the fraud detection result should be true

  Scenario: Transaction amount is above the threshold
    When a transaction of $15,000.00 is processed
    Then the system should flag it as fraudulent
    And the fraud detection result should be true

  Scenario: Transaction amount is zero
    When a transaction of $0.00 is processed
    Then the system should not flag it as fraudulent
    And the fraud detection result should be false

  Scenario: Transaction amount is negative
    When a transaction of -$5,000.00 is processed
    Then the system should not flag it as fraudulent
    And the fraud detection result should be false

  Scenario: Transaction amount is very large
    When a transaction of $1,000,000.00 is processed
    Then the system should flag it as fraudulent
    And the fraud detection result should be true

  Scenario: Transaction amount is at the maximum double value
    When a transaction of Double.MAX_VALUE is processed
    Then the system should flag it as fraudulent
    And the fraud detection result should be true