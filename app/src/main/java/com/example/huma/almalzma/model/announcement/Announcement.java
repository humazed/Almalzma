package com.example.huma.almalzma.model.announcement;

import com.example.huma.almalzma.model.Link;

public class Announcement {
    private Event mEvent;
    private Link mLink;
    private Quote mQuote;

    public Event getEvent() {
        return mEvent;
    }

    public void setEvent(Event event) {
        mEvent = event;
    }

    public Link getLink() {
        return mLink;
    }

    public void setLink(Link link) {
        mLink = link;
    }

    public Quote getQuote() {
        return mQuote;
    }

    public void setQuote(Quote quote) {
        mQuote = quote;
    }
}

