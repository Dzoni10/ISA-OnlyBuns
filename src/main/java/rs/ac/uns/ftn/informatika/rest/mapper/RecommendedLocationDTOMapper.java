package rs.ac.uns.ftn.informatika.rest.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.rest.domain.RecommendedLocation;
import rs.ac.uns.ftn.informatika.rest.dto.RecommendedLocationDTO;

@Component
public class RecommendedLocationDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public RecommendedLocationDTOMapper(ModelMapper modelMapper){this.modelMapper = modelMapper;}

    public RecommendedLocation fromDTOtoRecommendedLocation(RecommendedLocationDTO recommendedLocationDTO){return modelMapper.map(recommendedLocationDTO, RecommendedLocation.class);}
    public RecommendedLocationDTO fromRecommendedLocationtoDTO(RecommendedLocation recommendedLocation){return modelMapper.map(recommendedLocation, RecommendedLocationDTO.class);}


}
