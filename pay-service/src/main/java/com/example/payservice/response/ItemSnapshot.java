package com.example.payservice.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemSnapshot(String itemUUID, String name, Integer itemPrice) {}
