import HttpRepository from "@/repository/HttpRepository";
import type Login from "@/entity/user/Login";
import {inject, singleton} from "tsyringe";
import UserProfile from "@/entity/user/UserProfile";
import Token from "@/entity/data/Token";
import type Register from "@/entity/user/Register";
import type CartMessage from "@/entity/data/CartMessage";
import ShoppingCartItem from "@/entity/item/ShoppingCartItem";

@singleton()
export default class UserRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository : HttpRepository) {
    }

    public register(request: Register) {
        return this.httpRepository.post({
            path:"/api/user-service/register",
            body: request
        })
    }
    public login(request: Login)  {
        return  this.httpRepository.post<Token>({
            path: "/api/user-service/login",
            body: request
        },Token)
    }

    public getProfile() {
        return this.httpRepository.get<UserProfile>({
            path: "/api/user-service/info"
        },UserProfile)
    }

    public getCartItems() {
        return this.httpRepository.get<ShoppingCartItem>({
            path: "/api/order-service/cart/items"
        },ShoppingCartItem)
    }

    public cartMessage(request: CartMessage) {
        return this.httpRepository.post({
            path: "/api/user-service/cart/message",
            body: request
        })
    }

}