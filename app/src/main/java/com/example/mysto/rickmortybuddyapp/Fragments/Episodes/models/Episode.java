package com.example.mysto.rickmortybuddyapp.Fragments.Episodes.models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Episode implements Serializable {

    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("characters")
    @Expose
    private List<String> characters = null;
    @SerializedName("episode")
    @Expose
    private String episode;
    @SerializedName("air_date")
    @Expose
    private String airDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;

    /**
     * No args constructor for use in serialization
     *
     */
    public Episode() {
    }

    /**
     *
     * @param id
     * @param airDate
     * @param episode
     * @param created
     * @param name
     * @param image
     * @param characters
     * @param url
     */
    public Episode(String created, String image, String url, List<String> characters, String episode, String airDate, String name, Integer id) {
        super();
        this.created = created;
        this.image = image;
        this.url = url;
        this.characters = characters;
        this.episode = episode;
        this.airDate = airDate;
        this.name = name;
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}

