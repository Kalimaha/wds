package org.fao.fenix.wds.core.bean;

import java.util.Date;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class LogEntry {

    private Date date;

    private String rest;

    private String payload;

    public LogEntry(Date date, String rest, String payload) {
        this.setDate(date);
        this.setRest(rest);
        this.setPayload(payload);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRest() {
        return rest;
    }

    public void setRest(String rest) {
        this.rest = rest;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

}