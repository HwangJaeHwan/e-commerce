import HttpRepository from "@/repository/HttpRepository";
import type Login from "@/entity/user/Login";
import {inject} from "tsyringe";

export default class UserRepository{

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {

    }

    public login(request: Login) {
        this.httpRepository.post({
            path: "api/user-service/login",
            body: request
        })
    }

}