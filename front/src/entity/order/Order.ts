import {Transform} from "class-transformer";
import {DateTimeFormatter, LocalDateTime} from "@js-joda/core";
import OrderItem from "@/entity/order/OrderItem";

export default class Order {

    public id = 0
    public orderUUID = ''
    @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    public orderDate = LocalDateTime.now()
    public city = ''
    public street = ''
    public zipcode = ''
    public orderStatus = ''
    public OrderItems : OrderItem[] = []
}