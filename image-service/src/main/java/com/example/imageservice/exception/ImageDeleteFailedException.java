package com.example.imageservice.exception;

public class ImageDeleteFailedException extends ImageException {
    private static final String MESSAGE = "이미지 삭제에 실패했습니다.";

    public ImageDeleteFailedException() {
        super(MESSAGE);
    }

    public ImageDeleteFailedException(Throwable cause) {
        super(MESSAGE, cause);
    }


    @Override
    public String getStatusCode() {
        return "500";
    }
}
