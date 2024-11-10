package rs.ac.uns.ftn.informatika.rest.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.uns.ftn.informatika.rest.domain.Comment;
import rs.ac.uns.ftn.informatika.rest.dto.CommentDTO;

@Component
public class CommentDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public CommentDTOMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Comment fromDTOtoComment(CommentDTO commentDTO){
        return modelMapper.map(commentDTO, Comment.class);
    }

    public CommentDTO fromCommentToDTO(Comment comment){
        return modelMapper.map(comment, CommentDTO.class);
    }
}
