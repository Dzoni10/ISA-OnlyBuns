package rs.ac.uns.ftn.informatika.rest.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.rest.domain.Location;
import rs.ac.uns.ftn.informatika.rest.dto.LocationDTO;

@Component
public class LocationDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public LocationDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Location fromDTOtoLocation(LocationDTO locationDTO){
        return modelMapper.map(locationDTO, Location.class);
    }

    public LocationDTO fromLocationToDTO(Location location){
        return modelMapper.map(location,LocationDTO.class);
    }
}
