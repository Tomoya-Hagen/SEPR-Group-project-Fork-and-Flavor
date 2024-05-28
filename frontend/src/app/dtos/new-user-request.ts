export class NewUserRequest {
  constructor(
    public username: string,
    public email: string,
    public password: string
  ) {}
}
