import HttpRepository from "@/repository/HttpRepository";
import type Login from "@/entity/user/Login";
import {inject, singleton} from "tsyringe";
import UserProfile from "@/entity/user/UserProfile";
import Token from "@/entity/data/Token";

@singleton()
export default class UserRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository : HttpRepository) {
    }

    public login(request: Login) :Promise<Token> {
        return  this.httpRepository.post({
            path: "api/user-service/login",
            body: request
        },Token)
    }

    public getProfile() :Promise<UserProfile>{
        return this.httpRepository.get({
            path: "api/user-service/info"
        },UserProfile)
    }

}