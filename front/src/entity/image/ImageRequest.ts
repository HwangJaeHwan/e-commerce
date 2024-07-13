export default class ImageRequest {


    private UUID =''
    private userUUID =''
    private imageType = ''

    constructor(UUID: string, userUUID: string, imageType: string) {
        this.UUID = UUID;
        this.userUUID = userUUID;
        this.imageType = imageType;
    }
}