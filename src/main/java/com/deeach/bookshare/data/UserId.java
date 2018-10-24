package com.deeach.bookshare.data;

import java.util.UUID;

public class UserId {
    private final UUID identifier;

    public UserId(UUID identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }
}
