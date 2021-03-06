package com.mazurek.moneytransfer;

import com.google.common.base.Preconditions;
import com.mazurek.moneytransfer.model.Account;
import com.mazurek.moneytransfer.model.Person;
import com.mazurek.moneytransfer.rest.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class MoneyTransferController {
    private AtomicInteger idCounter;

    protected final Map<String, Account> accounts;

    protected final Map<Person, Account> personAccountMap;

    public MoneyTransferController() {
        this.idCounter = new AtomicInteger(0);
        this.accounts = initializeMap();
        this.personAccountMap = initializeMap();
    }

    protected <K, V> Map<K, V> initializeMap() {
        return new HashMap<>();
    }

    public String createAccount(String ownerName, String ownerPhoneNumber) {
        Person person = Person.create(ownerName, ownerPhoneNumber);
        if (personAccountMap.containsKey(person)) {
            throw new IllegalArgumentException("Account already exist for provided person");
        }
        Account account = new Account(person);
        String newId = createNewId();
        accounts.put(newId, account);
        personAccountMap.put(person, account);
        return newId;
    }


    public void deposit(String id, BigDecimal amount) {
        validateAmount(amount);
        Account account = getAccount(id);
        account.setBalance(account.getBalance().add(amount));
    }

    public void withdraw(String id, BigDecimal amount) {
        validateAmount(amount);
        Account account = getAccount(id);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(String.format("Balance is too low to withdraw: %s", amount));
        }
        account.setBalance(account.getBalance().subtract(amount));
    }

    public Account getAccount(String id) {
        return Optional.ofNullable(accounts.get(id)).orElseThrow(() -> new ResourceNotFoundException(String.format("Account with id %s doesn't exist", id)));
    }

    public void transfer(String sourceId, String targetId, BigDecimal amount) {
        validateAmount(amount);
        Account sourceAccount = getAccount(sourceId);
        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(String.format("Balance is too low to transfer: %s", amount));
        }

        Account targetAccount = getAccount(targetId);
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

    }

    private String createNewId() {
        return String.format("acc%d", idCounter.getAndIncrement());
    }

    private void validateAmount(BigDecimal amount) {
        Preconditions.checkArgument(amount.compareTo(BigDecimal.ZERO) > 0, String.format("Amount (%s) must be positive", amount));
    }
}
