package rs.ac.uns.ftn.informatika.rest.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.rest.domain.Following;
import rs.ac.uns.ftn.informatika.rest.dto.FollowingDTO;

@Component
public class FollowingDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public FollowingDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Following fromDTOtoFollowing(FollowingDTO followingDTO) {
        return modelMapper.map(followingDTO, Following.class);
    }

    public FollowingDTO fromFollowingToDTO(Following following) {
        return modelMapper.map(following, FollowingDTO.class);
    }
}
