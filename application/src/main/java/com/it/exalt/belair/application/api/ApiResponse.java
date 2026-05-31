package com.it.exalt.belair.application.api;

public record ApiResponse<T>(int statusCode, T body) {
}
