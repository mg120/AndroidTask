package com.androidtask.model;

public class CountriesModel {

    private String name;
    private String capital;

    public CountriesModel(String countryName, String capital) {
        this.name = countryName;
        this.capital = capital;
    }

    public String getCountryName() {
        return name;
    }

    public void setCountryName(String countryName) {
        this.name = countryName;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }
}
