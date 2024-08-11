import {Transform} from "class-transformer";
import {DateTimeFormatter, LocalDateTime} from "@js-joda/core";
import OrderItem from "@/entity/order/OrderItem";

export default class Order {

    public orderId = 0
    public orderUUID = ''
    @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    public orderDate = LocalDateTime.now()
    public name = ''
    public address = ''
    public detailAddress = ''
    public zipcode = ''
    public phoneNumber = ''
    public orderStatus = ''
    public items : OrderItem[] = []
    public totalPrice = 0

    public getDisplaySimpleRegDate() {
        return this.orderDate.format(DateTimeFormatter.ofPattern('yyyy-MM-dd'))
    }
}