package rs.ac.uns.ftn.informatika.rest.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;
import rs.ac.uns.ftn.informatika.rest.domain.Like;
import rs.ac.uns.ftn.informatika.rest.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.rest.dto.LikeDTO;

@Component
public class LikeDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public LikeDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Like fromDTOtoLike(LikeDTO likeDTO){
        return modelMapper.map(likeDTO, Like.class);
    }

    public LikeDTO fromLikeToDTO(Like like){
        return modelMapper.map(like, LikeDTO.class);
    }
}
