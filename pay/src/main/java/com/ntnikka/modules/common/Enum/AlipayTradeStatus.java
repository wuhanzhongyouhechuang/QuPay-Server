package com.ntnikka.modules.common.Enum;

/**
 * @ClassName AlipayTradeStatus
 * @Author Liuq
 * @Description todo
 * @Date 2018/9/20 9:39
 **/
public enum AlipayTradeStatus {
    TRADE_SUCCESS(0,"TRADE_SUCCESS"),TRADE_FINISHED(1 , "TRADE_FINISHED"),TRADE_CLOSED(2,"TRADE_CLOSED");
    private int code;
    private String status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    AlipayTradeStatus(int code , String status){
        this.code = code;
        this.status = status ;
    }
}
