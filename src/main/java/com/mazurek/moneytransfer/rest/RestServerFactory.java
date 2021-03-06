package com.mazurek.moneytransfer.rest;

import com.mazurek.moneytransfer.MoneyTransferController;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;

public class RestServerFactory {
    private final MoneyTransferController controller;

    public RestServerFactory(MoneyTransferController controller) {
        this.controller = controller;
    }

    public Undertow createServer() {
        RoutingHandler routingHandler = Handlers.routing()
                .add("GET", "/", ex -> ex.getResponseSender().send("OK"))
                .add("POST", "/account", new CreateAccountHandler(controller))
                .add("GET", "/account/{id}", new GetAccountInfoHandler(controller))
                .add("POST", "/deposit", new DepositHandler(controller))
                .add("POST", "/withdraw", new WithdrawHandler(controller))
                .add("POST", "/transfer", new TransferHandler(controller));
        return Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(routingHandler).build();
    }

}
