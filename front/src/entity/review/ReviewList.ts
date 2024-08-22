import {Transform} from "class-transformer";
import {DateTimeFormatter, LocalDateTime} from "@js-joda/core";

export default class ReviewList {

    public content= ''

    public score= 0

    public userUUID= ''

    public reviewUUID= ''

    @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    public regDate = LocalDateTime.now()

    public loginId=""

    public getDisplaySimpleRegDate() {
        return this.regDate.format(DateTimeFormatter.ofPattern('yyyy-MM-dd'))
    }
}