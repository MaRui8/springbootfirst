package com.mr.websocket;

import com.mr.common.GlobalUniqueNo;

import java.util.Date;

public class WebSocketMessage {

    private Integer no;
    private byte messageType;
    private String stringMsg;
    private String fileMsg;
    private String senderName;
    private String receiverName;
    private Date createTime;


    public WebSocketMessage() {
        this.no = GlobalUniqueNo.get();
        this.createTime = new Date();
    }

    public WebSocketMessage(byte messageType,String stringMsg, String fileMsg, String senderName, String receiverName) {
        this();
        this.messageType = messageType;
        this.stringMsg = stringMsg;
        this.fileMsg = fileMsg;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public String getStringMsg() {
        return stringMsg;
    }

    public void setStringMsg(String stringMsg) {
        this.stringMsg = stringMsg;
    }

    public String getFileMsg() {
        return fileMsg;
    }

    public void setFileMsg(String fileMsg) {
        this.fileMsg = fileMsg;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
