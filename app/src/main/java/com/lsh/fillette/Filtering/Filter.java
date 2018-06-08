package com.lsh.fillette.Filtering;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lsh on 2018. 3. 16..
 * Filter's metadata.
 */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Filter implements Serializable {

    private String creator;
    private String uid;
    private String filterName;
    private String profileUrl;
    private String filterData;
    private List<String> imageList;
    private long date;
    private boolean like;
    private int downCount;
    private int film;

    public String getFilterName() {
        return filterName;
    }

    public String getUid() {
        return uid;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getCreator() {
        return creator;
    }

    public long getDate() {
        return date;
    }


    public int getDownCount() {
        return downCount;
    }

    public int getFilm() {
        return film;
    }

    public String getFilterData() {
        return filterData;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public void setDownCount(int downCount) {
        this.downCount = downCount;
    }

    public void setFilm(int film) {
        this.film = film;
    }

    public void setFilterData(String filterData) {
        this.filterData = filterData;
    }

}

