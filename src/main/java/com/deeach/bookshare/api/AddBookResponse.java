package com.deeach.bookshare.api;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AddBookResponse {
    private String result;

    public AddBookResponse() {
    }

    public AddBookResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("result", result)
                .toString();
    }
}
