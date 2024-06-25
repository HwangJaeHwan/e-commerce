package com.example.imageservice.exception;

public class ImageNotFoundException extends ImageException{

    private static final String MESSAGE = "이미지를 찾을 수 없습니다.";

    public ImageNotFoundException() {
        super(MESSAGE);
    }

    public ImageNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "404";
    }
}
