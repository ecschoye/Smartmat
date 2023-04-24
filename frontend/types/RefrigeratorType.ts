import { Member } from "./MemberType"; 

export interface Refrigerator{
  id : number, 
  name : String, 
  address : String,
  members : Member[]
}