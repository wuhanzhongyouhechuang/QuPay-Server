package com.ntnikka.common.Enum;

public enum PayTypeEnum {

    WAP(0, "Wap"), QRCODE(1, "QrCode");

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    PayTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
