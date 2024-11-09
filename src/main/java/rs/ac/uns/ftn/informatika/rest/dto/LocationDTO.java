package rs.ac.uns.ftn.informatika.rest.dto;

import rs.ac.uns.ftn.informatika.rest.domain.Location;

public class LocationDTO {
    public double latitude;
    public double longitude;
    public LocationDTO(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public LocationDTO() {}
    public LocationDTO(Location location) {
        this(location.getLatitude(), location.getLongitude());
    }
}
