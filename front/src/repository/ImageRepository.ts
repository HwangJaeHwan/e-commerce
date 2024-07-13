import {singleton} from "tsyringe";
import axios from "axios";

@singleton()
export default class ImageRepository {


    public upload(request: FormData) {

        return axios.post('api/image-service/upload',request,{
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        })
    }


}