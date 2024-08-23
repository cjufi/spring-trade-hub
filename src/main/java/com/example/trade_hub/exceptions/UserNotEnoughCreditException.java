package com.example.trade_hub.exceptions;

public class UserNotEnoughCreditException extends Exception{

    public UserNotEnoughCreditException(String message) {
        super(message);
    }
}
