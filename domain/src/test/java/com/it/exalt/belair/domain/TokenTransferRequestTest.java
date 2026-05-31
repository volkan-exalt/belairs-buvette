package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenTransferRequestTest {

    @Test
    void shouldConfirmTokenTransfer() {
        FestivalGoer sender = new FestivalGoer("Sender", new TokenBalance(3, 3));
        FestivalGoer recipient = new FestivalGoer("Recipient", new TokenBalance(1, 1));

        TokenTransferRequest request = sender.requestTokenTransfer(recipient, 2, 1);
        request.confirm();

        assertSame(sender, request.sender());
        assertSame(recipient, request.recipient());
        assertEquals(new TokenBalance(2, 1), request.transferAmount());
        assertEquals(TokenTransferRequest.Status.CONFIRMED, request.status());
        assertEquals(new TokenBalance(1, 2), sender.tokenBalance());
        assertEquals(new TokenBalance(3, 2), recipient.tokenBalance());
    }

    @Test
    void shouldRejectOrValidateTokenTransfers() {
        FestivalGoer sender = new FestivalGoer("Sender", new TokenBalance(3, 3));
        FestivalGoer recipient = new FestivalGoer("Recipient", new TokenBalance(1, 1));

        assertThrows(DomainValidationException.class, () -> sender.requestTokenTransfer(recipient, 4, 0));
        assertThrows(DomainValidationException.class, () -> sender.requestTokenTransfer(recipient, 0, 4));
        assertThrows(DomainValidationException.class, () -> sender.requestTokenTransfer(recipient, 3, 4));

        TokenTransferRequest request = sender.requestTokenTransfer(recipient, 1, 1);
        request.reject();
        assertEquals(TokenTransferRequest.Status.REJECTED, request.status());
        assertThrows(DomainValidationException.class, request::confirm);
    }
}
