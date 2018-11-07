package com.resource.management.data;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BookOwner {
    private final String userName;
    private final UserId userId;

    public BookOwner(String userName, UserId userId) {
        this.userName = userName;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userName", userName)
                .append("userId", userId)
                .toString();
    }
}
