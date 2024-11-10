export interface Post{
    id: number;
    creatorId: number;
    description: string;
    creationTime : Date;
    isEditing: boolean;
    imagePath: string;
}