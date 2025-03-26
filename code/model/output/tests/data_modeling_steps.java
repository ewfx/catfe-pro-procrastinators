package com.bank.step_definitions;

import com.bank.models.Account;
import com.bank.models.Transaction;
import com.bank.repositories.AccountRepository;
import com.bank.repositories.TransactionRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class DataModelingSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private String responseContent;
    private int responseStatus;

    @Given("the database is initialized")
    public void initializeDatabase() {
        // Setup any test data here if needed
    }

    @Given("the application context is loaded")
    public void loadApplicationContext() {
        // No action needed, SpringBootTest handles this
    }

    @When("I create an account with:")
    public void createAccount(io.cucumber.datatable.DataTable dataTable) throws Exception {
        var account = new Account();
        var row = dataTable.rowMap(String.class, String.class);
        account.setAccountNumber(row.get("accountNumber"));
        account.setName(row.get("name"));
        account.setBalance(Double.parseDouble(row.get("balance")));
        account.setCurrency(row.get("currency"));

        var request = MockMvcRequestBuilders.post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(account));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        responseContent = result.getResponse().getContentAsString();
        responseStatus = result.getResponse().getStatus();
    }

    @Then("the response status should be {int}")
    public void checkResponseStatus(int expectedStatus) {
        assertEquals(expectedStatus, responseStatus);
    }

    @Then("the account should be persisted in the database")
    public void verifyAccountPersisted() {
        var accountNumber = accountRepository.findByAccountNumber(responseContent).get();
        assertNotNull(accountNumber);
    }

    @When("I retrieve the account by accountNumber {string}")
    public void retrieveAccount(String accountNumber) throws Exception {
        var request = MockMvcRequestBuilders.get("/api/accounts/" + accountNumber);

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        responseContent = result.getResponse().getContentAsString();
        responseStatus = result.getResponse().getStatus();
    }

    @Then("the account details should match the given data")
    public void verifyAccountDetails(String accountNumber, String name, Double balance, String currency) {
        var account = accountRepository.findByAccountNumber(accountNumber).get();
        assertEquals(accountNumber, account.getAccountNumber());
        assertEquals(name, account.getName());
        assertEquals(balance, account.getBalance());
        assertEquals(currency, account.getCurrency());
    }

    @When("I update the account's balance to {double}")
    public void updateAccountBalance(Double newBalance) throws Exception {
        var account = accountRepository.findByAccountNumber(responseContent).get();
        account.setBalance(newBalance);

        var request = MockMvcRequestBuilders.put("/api/accounts/" + account.getAccountNumber())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(account));

        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        responseStatus = result.getResponse().getStatus();
    }

    @Then("the updated account balance should be {double} in the database")
    public void verifyUpdatedBalance(Double expectedBalance) {
        var account = accountRepository.findByAccountNumber(responseContent).get();
        assertEquals(expectedBalance, account.getBalance());
    }

    @When("I create a transaction from {string} to {string} of amount {double}")
    public void createTransaction(String fromAccountNumber, String toAccountNumber, Double amount) throws Exception {
        var transaction = new Transaction();
        transaction.setFromAccount(fromAccountNumber);
        transaction.setToAccount(toAccountNumber);
        transaction.setAmount(amount);

        var request = MockMvcRequestBuilders.post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(transaction));

        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        responseContent = result.getResponse().getContentAsString();
        responseStatus = result.getResponse().status().value();
    }

    @Then("the transaction should be recorded in the database")
    public void verifyTransactionRecorded() {
        var transactionId = responseContent;
        var transaction = transactionRepository.findById(transactionId).get();
        assertNotNull(transaction);
    }

    @Then("the balance of {string} should be {double}")
    public void verifyAccountBalance(String accountNumber, Double expectedBalance) {
        var account = accountRepository.findByAccountNumber(accountNumber).get();
        assertEquals(expectedBalance, account.getBalance());
    }

    private String asJsonString(final Object object) {
        // Implement JSON conversion using Jackson or similar
        return "{}"; // Placeholder, replace with actual implementation
    }
}