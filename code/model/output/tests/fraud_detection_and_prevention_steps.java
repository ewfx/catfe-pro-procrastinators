package com.bank.steps;

import com.bank.models.Account;
import com.bank.repositories.AccountRepository;
import com.bank.repositories.TransactionRepository;
import com.bank.services.FraudService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class FraudDetectionSteps {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private FraudService fraudService;

    private Map<String, Object> response;
    private static final double FRAUD_THRESHOLD = 10000.0;

    @Given("the fraud threshold is {double}")
    public void setupFraudThreshold(double threshold) {
        // Fraud threshold is already defined as 10000.0 in FraudService
    }

    @When("a transaction of {double} is attempted")
    public void attemptTransaction(double amount) {
        response = new HashMap<>();
        
        // Mock account repository to return existing accounts
        Account sender = new Account();
        sender.setBalance(20000.00);
        when(accountRepository.findById("12345")).thenReturn(Optional.of(sender));
        when(accountRepository.findById("67890")).thenReturn(Optional.of(new Account()));

        // Process the transfer
        Map<String, Object> transferDetails = new HashMap<>();
        transferDetails.put("fromAccount", "12345");
        transferDetails.put("toAccount", "67890");
        transferDetails.put("amount", amount);

        response = ((TransferController) mock(TransferController.class))
                .transferFunds(transferDetails);
    }

    @Then("the transaction should be blocked")
    public void verifyTransactionBlocked() {
        assertEquals("failed", response.get("status"));
        assertEquals("Fraud detected! Transfer blocked.", response.get("message"));
    }

    @Then("the system should log a fraud warning")
    public void verifyFraudLogged() {
        // Verify through logging or by checking the response
        // In a real implementation, you would verify logs
        // For testing purposes, we can check the response message
        assertTrue(response.get("message").toString().contains("Fraud detected"));
    }

    @Then("the response should indicate fraud detection")
    public void verifyResponseIndicatesFraud() {
        assertEquals("failed", response.get("status"));
        assertEquals("Fraud detected! Transfer blocked.", response.get("message"));
    }

    @Then("the transaction should be processed successfully")
    public void verifyTransactionSuccess() {
        assertEquals("success", response.get("status"));
        assertEquals("Transfer completed successfully.", response.get("message"));
    }

    @Then("the system should not log a fraud warning")
    public void verifyNoFraudLogged() {
        // In a real implementation, verify no fraud warning was logged
        // For testing purposes, check the response doesn't indicate fraud
        assertFalse(response.get("message").toString().contains("Fraud detected"));
    }

    @Then("transactions below or equal to {double} should be processed")
    public void verifyLegitimateTransactionsProcessed(double threshold) {
        // Implement logic to verify transactions below threshold are processed
        // This would involve checking the response for each transaction
    }

    @Then("transactions above {double} should be blocked")
    public void verifyFraudulentTransactionsBlocked(double threshold) {
        // Implement logic to verify transactions above threshold are blocked
        // This would involve checking the response for each transaction
    }

    @Then("appropriate fraud warnings should be logged")
    public void verifyFraudWarningsLogged() {
        // Implement logic to verify appropriate fraud warnings
        // This would involve checking logs or response messages
    }
}