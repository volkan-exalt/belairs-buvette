package com.it.exalt.belair.domain;

import java.util.Objects;

public class TokenTransferRequest {
    public enum Status {
        PENDING,
        CONFIRMED,
        REJECTED
    }

    private final FestivalGoer sender;
    private final FestivalGoer recipient;
    private final TokenBalance transferAmount;
    private Status status;

    public TokenTransferRequest(FestivalGoer sender, FestivalGoer recipient, TokenBalance transferAmount) {
        this.sender = Objects.requireNonNull(sender, "sender must not be null");
        this.recipient = Objects.requireNonNull(recipient, "recipient must not be null");
        this.transferAmount = Objects.requireNonNull(transferAmount, "transferAmount must not be null");
        if (transferAmount.drinkTokens() > 3 || transferAmount.foodTokens() > 3) {
            throw new DomainValidationException("A festival goer can transfer up to three tokens of each type");
        }
        if (!sender.tokenBalance().canCover(transferAmount)) {
            throw new DomainValidationException("Sender does not have enough tokens for transfer");
        }
        this.status = Status.PENDING;
    }

    public FestivalGoer sender() {
        return sender;
    }

    public FestivalGoer recipient() {
        return recipient;
    }

    public TokenBalance transferAmount() {
        return transferAmount;
    }

    public Status status() {
        return status;
    }

    public void confirm() {
        if (status != Status.PENDING) {
            throw new DomainValidationException("Transfer request is already completed");
        }
        sender.deductTokens(transferAmount);
        recipient.addTokens(transferAmount);
        status = Status.CONFIRMED;
    }

    public void reject() {
        if (status != Status.PENDING) {
            throw new DomainValidationException("Transfer request is already completed");
        }
        status = Status.REJECTED;
    }
}
