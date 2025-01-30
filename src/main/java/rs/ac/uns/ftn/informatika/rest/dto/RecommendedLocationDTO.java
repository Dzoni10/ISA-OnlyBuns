package rs.ac.uns.ftn.informatika.rest.dto;

import rs.ac.uns.ftn.informatika.rest.domain.RecommendedLocation;

public class RecommendedLocationDTO {
    public double latitude;
    public double longitude;
    public String name;

    public RecommendedLocationDTO(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
    public RecommendedLocationDTO(RecommendedLocation recommendedLocation) {
        this(recommendedLocation.getLatitude(), recommendedLocation.getLongitude(),recommendedLocation.getName());
    }
}
