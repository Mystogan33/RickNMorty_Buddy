package com.example.mysto.rickmortybuddyapp.Fragments.Locations.models;


import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RawLocationsServerResponse implements Serializable {

    @SerializedName("info")
    @Expose
    private Info info;
    @SerializedName("results")
    @Expose
    private List<Location> results = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public RawLocationsServerResponse() {
    }

    /**
     *
     * @param results
     * @param info
     */
    public RawLocationsServerResponse(Info info, List<Location> results) {
        super();
        this.info = info;
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Location> getResults() {
        return results;
    }

    public void setResults(List<Location> results) {
        this.results = results;
    }

}

