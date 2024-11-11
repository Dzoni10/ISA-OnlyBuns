import { Like } from "./like.model";

export interface Post{
    id: number;
    creatorId: number;
    description: string;
    creationTime : Date;
    isEditing: boolean;
    imagePath: string;
    username: string;
    likeCount: number;        // Optional field to store the like count
    likes?: Like[];            // Optional array of likes for each post
    userHasLiked?: boolean;    // Optional field to check if the user liked the post
}