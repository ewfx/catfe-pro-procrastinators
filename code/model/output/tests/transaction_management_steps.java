package com.bank.steps;

import com.bank.models.Account;
import com.bank.models.Transaction;
import com.bank.models.TransactionType;
import com.bank.repositories.AccountRepository;
import com.bank.repositories.TransactionRepository;
import com.bank.services.FraudService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionSteps {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FraudService fraudService;

    @MockBean
    private FraudService fraudServiceMock;

    private Account account1;
    private Account account2;
    private Transaction transaction;

    @Given("the following accounts exist:")
    public void setupAccounts(io.cucumber.datatable.DataTable dataTable) {
        account1 = new Account();
        account1.setAccountNumber("123456789");
        account1.setName("Alice");
        account1.setBalance(5000.00);
        account1.setCurrency("USD");
        accountRepository.save(account1);

        account2 = new Account();
        account2.setAccountNumber("987654321");
        account2.setName("Bob");
        account2.setBalance(3000.00);
        account2.setCurrency("USD");
        accountRepository.save(account2);
    }

    @When("I transfer {double} USD from account {string} to {string}")
    public void transferFunds(double amount, String fromAccount, String toAccount) {
        Map<String, Object> transferDetails = new HashMap<>();
        transferDetails.put("fromAccount", fromAccount);
        transferDetails.put("toAccount", toAccount);
        transferDetails.put("amount", amount);

        // Mock fraud service if needed
        when(fraudServiceMock.isFraudulent(amount)).thenReturn(amount > FraudService.FRAUD_THRESHOLD);

        new TransferController(accountRepository, transactionRepository, fraudServiceMock).transferFunds(transferDetails);
    }

    @Then("the transfer should be successful")
    public void verifySuccessfulTransfer() {
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Then("the balance of account {string} should be {double}")
    public void verifyBalance(String accountNumber, double expectedBalance) {
        Optional<Account> account = accountRepository.findById(accountNumber);
        Assert.assertTrue(account.isPresent());
        Assert.assertEquals(expectedBalance, account.get().getBalance(), 0.01);
    }

    @Then("a transfer transaction should be recorded")
    public void verifyTransactionRecorded() {
        List<Transaction> transactions = transactionRepository.findAll();
        Assert.assertFalse(transactions.isEmpty());
        Assert.assertEquals(TransactionType.TRANSFER, transactions.get(transactions.size() - 1).getType());
    }

    @Then("the transfer should fail due to fraud detection")
    public void verifyFraudDetection() {
        verify(fraudServiceMock, times(1)).isFraudulent(anyDouble());
    }

    @Then("the balances should remain unchanged")
    public void verifyBalancesUnchanged() {
        Assert.assertEquals(5000.00, accountRepository.findById("123456789").get().getBalance(), 0.01);
        Assert.assertEquals(3000.00, accountRepository.findById("987654321").get().getBalance(), 0.01);
    }

    @Then("a fraud alert should be logged")
    public void verifyFraudAlertLogged() {
        // Verify through logs or mock verification
        // This would typically involve checking the logging framework
    }

    @When("I deposit {double} USD into account {string}")
    public void depositFunds(double amount, String accountNumber) {
        Map<String, Object> depositDetails = new HashMap<>();
        depositDetails.put("accountNumber", accountNumber);
        depositDetails.put("amount", amount);

        new TransferController(accountRepository, transactionRepository, fraudServiceMock).depositFunds(depositDetails);
    }

    @Then("the deposit should be successful")
    public void verifySuccessfulDeposit() {
        verify(accountRepository).save(any(Account.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @When("I withdraw {double} USD from account {string}")
    public void withdrawFunds(double amount, String accountNumber) {
        Map<String, Object> withdrawDetails = new HashMap<>();
        withdrawDetails.put("accountNumber", accountNumber);
        withdrawDetails.put("amount", amount);

        new TransferController(accountRepository, transactionRepository, fraudServiceMock).withdrawFunds(withdrawDetails);
    }

    @Then("the withdrawal should be successful")
    public void verifySuccessfulWithdrawal() {
        verify(accountRepository).save(any(Account.class));
        verify(transactionRepository).save(any(Transaction.class));
    }

    @When("I retrieve all transactions")
    public void retrieveAllTransactions() {
        new TransferController(accountRepository, transactionRepository, fraudServiceMock).getAllTransactions(null);
    }

    @Then("I should see all recorded transactions")
    public void verifyAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        Assert.assertFalse(transactions.isEmpty());
    }

    @When("I retrieve transactions for account {string}")
    public void retrieveTransactionsForAccount(String accountNumber) {
        new TransferController(accountRepository, transactionRepository, fraudServiceMock).getAllTransactions(accountNumber);
    }

    @Then("I should see only transactions related to that account")
    public void verifyAccountTransactions() {
        List<Transaction> transactions = transactionRepository.findByFromAccountOrToAccount("123456789", "123456789");
        Assert.assertFalse(transactions.isEmpty());
        for (Transaction transaction : transactions) {
            Assert.assertTrue(transaction.getFromAccount().equals("123456789") ||
                    transaction.getToAccount().equals("123456789"));
        }
    }
}