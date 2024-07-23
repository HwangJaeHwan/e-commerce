export default class CartMessage {

    public itemUUID = ''
    public quantity = 0


    constructor(itemUUID: string, quantity: number) {
        this.itemUUID = itemUUID;
        this.quantity = quantity;
    }
}