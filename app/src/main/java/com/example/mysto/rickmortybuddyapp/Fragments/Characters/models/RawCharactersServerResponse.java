
package com.example.mysto.rickmortybuddyapp.Fragments.Characters.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RawCharactersServerResponse implements Serializable {

    @SerializedName("info")
    @Expose
    private Info info;

    @SerializedName("results")
    @Expose
    private List<Character> results = null;

    public RawCharactersServerResponse(Info info, List<Character> results) {
        this.info = info;
        this.results = results;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Character> getResults() {
        return results;
    }

    public void setResults(List<Character> results) {
        this.results = results;
    }

}
