package com.iitguwahati.prendre.helper;

/**
 * Created by root on 10/2/16.
 */
public class Events {
    private String subject;
    private String date;
    private String time;
    private String venue;
    public Events() {
        // empty default constructor, necessary for Firebase to be able to deserialize blog posts
    }
    public String getSubject() {
        return subject;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getVenue() {
        return venue;
    }


}
