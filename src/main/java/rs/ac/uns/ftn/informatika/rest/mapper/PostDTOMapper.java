package rs.ac.uns.ftn.informatika.rest.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.rest.domain.Post;
import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;

@Component
public class PostDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public PostDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Post fromDTOtoPost(PostDTO postDTO){
        return modelMapper.map(postDTO, Post.class);
    }

    public PostDTO fromPostToDTO(Post post){
        return modelMapper.map(post,PostDTO.class);
    }

}
