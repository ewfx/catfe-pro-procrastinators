package com.bank.steps;

import com.bank.models.Account;
import com.bank.repositories.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = BankingApiApplication.class)
public class AccountSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    private String responseContent;

    @Given("an account with number {string} exists")
    public void anAccountWithNumberExists(String accountNumber) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setName("Test User");
        account.setBalance(1000.00);
        account.setCurrency("USD");
        accountRepository.save(account);
    }

    @Given("the following accounts exist:")
    public void theFollowingAccountsExist(List<Account> accounts) {
        accountRepository.saveAll(accounts);
    }

    @When("I send a POST request to create a new account with:")
    public void iSendAPostRequestToCreateANewAccountWith(Map<String, Object> accountDetails) {
        try {
            String requestJson = objectMapper.writeValueAsString(new Account(
                    (String) accountDetails.get("accountNumber"),
                    (String) accountDetails.get("name"),
                    (Double) accountDetails.get("balance"),
                    (String) accountDetails.get("currency")));
            
            responseContent = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestJson))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("I send a GET request to {string}")
    public void iSendAGetRequestToString(String endpoint) {
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.get(endpoint))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @When("I send a DELETE request to {string}")
    public void iSendAdeleterRequestToString(String endpoint) {
        try {
            responseContent = mockMvc.perform(MockMvcRequestBuilders.delete(endpoint))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        try {
            int actualStatus = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts"))
                    .andReturn()
                    .getResponse()
                    .getStatus();
            Assert.assertEquals(expectedStatus, actualStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the response should include:")
    public void theResponseShouldInclude(Map<String, Object> expectedResponse) {
        try {
            Map<String, Object> actualResponse = objectMapper.readValue(responseContent, Map.class);
            Assert.assertEquals(expectedResponse, actualResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the response should include the account details:")
    public void theResponseShouldIncludeTheAccountDetails(Map<String, Object> expectedAccount) {
        try {
            Account actualAccount = objectMapper.readValue(responseContent, Account.class);
            Assert.assertEquals(expectedAccount.get("accountNumber"), actualAccount.getAccountNumber());
            Assert.assertEquals(expectedAccount.get("name"), actualAccount.getName());
            Assert.assertEquals(expectedAccount.get("balance"), actualAccount.getBalance());
            Assert.assertEquals(expectedAccount.get("currency"), actualAccount.getCurrency());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("the response should be empty")
    public void theResponseShouldBeEmpty() {
        Assert.assertTrue(responseContent.isEmpty());
    }
}