import { Location } from "./post.model";

export interface Registration{
    name: string;
    surname:string;
    username:string;
    password:string;
    email:string;
    adress:Location;
}