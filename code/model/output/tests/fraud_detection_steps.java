package com.bank.steps;

import com.bank.services.FraudService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class FraudDetectionSteps {

    private FraudService fraudService;
    private boolean isFraudulentResult;

    @Given("the fraud detection threshold is set to $10,000.00")
    public void theFraudDetectionThresholdIsSetTo$10_000() {
        // The threshold is already defined in FraudService as 10000.0
        fraudService = new FraudService();
    }

    @When("a transaction of {double} is processed")
    public void aTransactionOfIsProcessed(double amount) {
        isFraudulentResult = fraudService.isFraudulent(amount);
    }

    @Then("the system should not flag it as fraudulent")
    public void theSystemShouldNotFlagItAsFraudulent() {
        Assert.assertFalse(isFraudulentResult);
    }

    @Then("the fraud detection result should be false")
    public void theFraudDetectionResultShouldBeFalse() {
        Assert.assertFalse(isFraudulentResult);
    }

    @Then("the system should flag it as fraudulent")
    public void theSystemShouldFlagItAsFraudulent() {
        Assert.assertTrue(isFraudulentResult);
    }

    @Then("the fraud detection result should be true")
    public void theFraudDetectionResultShouldBeTrue() {
        Assert.assertTrue(isFraudulentResult);
    }
}