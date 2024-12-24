export interface RegisterUser{
    id?: number;
    name: string;
    surname: string;
    username: string;
    password: string;
    email: string;
    adress: string;
    loginStatus: boolean;
    isAdmin: boolean;
    postCount: number;
    commentNumber: number;
}