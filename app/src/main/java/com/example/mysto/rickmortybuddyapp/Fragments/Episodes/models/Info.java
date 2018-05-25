
package com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Info implements Serializable {

    @SerializedName("prev")
    @Expose
    private String prev;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("count")
    @Expose
    private Integer count;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Info() {
    }

    /**
     * 
     * @param count
     * @param pages
     * @param next
     * @param prev
     */
    public Info(String prev, String next, Integer pages, Integer count) {
        super();
        this.prev = prev;
        this.next = next;
        this.pages = pages;
        this.count = count;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
