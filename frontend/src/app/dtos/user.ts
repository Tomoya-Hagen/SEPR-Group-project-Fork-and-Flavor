import {Role} from "./role";

export interface userListDto {
  id: number;
  name: string;
}

export interface userDto {
  id: number;
  name: string;
}

export interface userPasswordChangeDto {
  oldPassword: string;
  newPassword: string;
}
