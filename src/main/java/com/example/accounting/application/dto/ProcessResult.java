package com.example.accounting.application.dto;

import lombok.Builder;

@Builder
public record ProcessResult(int total, int classified, int unclassified) {

}
