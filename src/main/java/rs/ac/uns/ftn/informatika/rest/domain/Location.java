package rs.ac.uns.ftn.informatika.rest.domain;

import rs.ac.uns.ftn.informatika.rest.dto.LocationDTO;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
    private double latitude;
    private double longitude;
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Location(LocationDTO location) {
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    public Location() {}
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

//    @Override
//    public String toString() {
//        return "Location{latitude=" + latitude + ", longitude=" + longitude + "}";
//    }
}
