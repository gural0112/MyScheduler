package com.mgreg.myscheduler.services;

public class EmailMessageInfoImpl implements MessageInfo{
    public final String from ;
    public final String to ;
    public final String subject ;
    public final String text ;

    public EmailMessageInfoImpl(String from, String to, String subject, String text) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    @Override
    public String getFrom() {
        return this.from;
    }

    @Override
    public String getTo() {
        return this.to;
    }

    @Override
    public String getSubject() {
        return this.subject;
    }

    @Override
    public String getText() {
        return this.text;
    }
}
