package com.mazurek.moneytransfer.rest;

import com.google.gson.Gson;
import com.mazurek.moneytransfer.MoneyTransferController;
import com.mazurek.moneytransfer.model.AccountView;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathTemplateMatch;
import io.undertow.util.StatusCodes;

class GetAccountInfoHandler implements HttpHandler {
    private final MoneyTransferController controller;

    public GetAccountInfoHandler(MoneyTransferController controller) {
        this.controller = controller;
    }

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        PathTemplateMatch pathMatch = httpServerExchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);
        String id = pathMatch.getParameters().get("id");
        Gson gson = new Gson();
        try {
            AccountView body = controller.getAccountViewById(id);
            httpServerExchange.setStatusCode(StatusCodes.OK);
            httpServerExchange.getResponseSender().send(gson.toJson(body));
        } catch (Exception ex) {
            httpServerExchange.setStatusCode(StatusCodes.NOT_FOUND);
            httpServerExchange.getResponseSender().send(String.format("Couldn't find account for id: %s (%s)", id, ex.getMessage()));
        }
    }
}
