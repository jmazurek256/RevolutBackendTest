package com.mazurek.moneytransfer;

import com.mazurek.moneytransfer.model.Account;
import com.mazurek.moneytransfer.model.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MoneyTransferController {
    private AtomicInteger idCounter = new AtomicInteger(0);

    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<Person, Account> personAccountMap = new HashMap<>();

    public String createAccount(String ownerName, String ownerPhoneNumber) {
        Person person = Person.create(ownerName, ownerPhoneNumber);
        if (personAccountMap.containsKey(person)) {
            return null;
        }
        Account account = new Account(person);
        String newId = createNewId();
        accounts.put(newId, account);
        personAccountMap.put(person, account);
        return newId;
    }

    public Account getAccountById(String id) {
        return accounts.get(id);
    }

    private String createNewId() {
        return String.format("acc%d", idCounter.getAndIncrement());
    }
}