package rs.ac.uns.ftn.informatika.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Embeddable;

public class RecommendedLocation {

    @JsonProperty("latitude")
    private double latitude;
    @JsonProperty("longitude")
    private double longitude;
    @JsonProperty("name")
    private String name;
    public RecommendedLocation(){}
    public RecommendedLocation(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
