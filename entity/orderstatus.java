package com.LUXURYCLIQ.entity;


import lombok.*;

import javax.persistence.Entity;


public enum orderstatus {
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    RETURN("Return"),
    CANCELED("Canceled");

    private final String displayName;

    orderstatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
