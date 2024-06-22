import {Role} from "./role";

export interface userListDto {
  id: number;
  name: string;
}

export interface userDto {
  id: number;
  name: string;
  roles: Role[];
  //verified: boolean; //true = already verified this recipe -> get boolean from recipe detail
}

export interface userPasswordChangeDto {
  oldPassword: string;
  newPassword: string;
}
