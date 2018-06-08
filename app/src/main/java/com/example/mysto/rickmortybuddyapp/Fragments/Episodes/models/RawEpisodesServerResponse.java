package com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models;


import java.io.Serializable;
import java.util.List;

import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Episode;
import com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models.Info;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RawEpisodesServerResponse implements Serializable {

    @SerializedName("results")
    @Expose
    private List<Episode> results = null;
    @SerializedName("info")
    @Expose
    private Info info;

    /**
     * No args constructor for use in serialization
     *
     */
    public RawEpisodesServerResponse() {
    }

    /**
     *
     * @param results
     * @param info
     */
    public RawEpisodesServerResponse(List<Episode> results, Info info) {
        super();
        this.results = results;
        this.info = info;
    }

    public List<Episode> getResults() {
        return results;
    }

    public void setResults(List<Episode> results) {
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

}

