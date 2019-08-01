package com.example.films;


import java.util.HashSet;

public class Films  implements Comparable<Films>{
    private int id;
    private String localized_name;
    private String name;
    private int year;
    private double rating;
    private String image_url;
    private String description;
    private HashSet<String> genres;


    public HashSet<String> getGenres() {
        return genres;
    }

    public void setGenres(HashSet<String> genres) {
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalized_name() {
        return localized_name;
    }

    public void setLocalized_name(String localized_name) {
        this.localized_name = localized_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
//Нужен для сортировка по первоночальному назваию
    @Override
    public int compareTo(Films other) {
        return localized_name.compareTo(other.localized_name);
    }
}

