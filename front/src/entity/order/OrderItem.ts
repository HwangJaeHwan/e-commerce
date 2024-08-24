import type ShoppingCartItem from "@/entity/item/ShoppingCartItem";

export default class OrderItem {
    public name = ''
    public itemUUID = ''
    public quantity = 0
    public price = 0


    constructor(cartItem: ShoppingCartItem) {
        if (cartItem) {
            this.name = cartItem.name;
            this.itemUUID = cartItem.itemUUID;
            this.quantity = cartItem.quantity;
            this.price = cartItem.itemPrice;
        }
    }


}