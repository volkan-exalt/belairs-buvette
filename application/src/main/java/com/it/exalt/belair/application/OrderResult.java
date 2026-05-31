package com.it.exalt.belair.application;

import java.util.UUID;

public record OrderResult(String status, UUID orderId) {
}
