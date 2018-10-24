package com.deeach.bookshare.api.book;

import org.apache.commons.lang.builder.ToStringBuilder;

public class GetBooksRequest {
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .toString();
    }
}
