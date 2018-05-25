
package com.example.mysto.rickmortybuddyapp.Fragments.Characters.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Characters implements Serializable {

    @SerializedName("info")
    @Expose
    private Info info;

    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public Characters() {

    }

    public Characters(Info info, List<Result> results) {
        this.info = info;
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
