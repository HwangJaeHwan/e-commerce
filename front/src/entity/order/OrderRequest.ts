import type OrderItem from "@/entity/order/OrderItem";

export default class OrderRequest {
    public userUUID = ''
    private items : OrderItem[] = []
    public address =''
    public name = ''
    public impUid = ''

    public detailAddress =''
    public zipcode =''
    public  phoneNumber =''


    public addItem(item: OrderItem): void {
        this.items.push(item);
    }
}