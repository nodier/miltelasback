package com.softlond.base.model;

import java.sql.Date;

public class DateRange {
    Date from;
    Date to;

    public DateRange() {
    }

    public DateRange(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
