package com.mr.controller;


import com.mr.common.Cache;
import com.mr.pojo.User;
import com.mr.websocket.GlobalWebSocket;
import com.mr.websocket.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("messageExchange")
public class MessageExchangeController {

    private Logger logger = LoggerFactory.getLogger(MessageExchangeController.class);

    @RequestMapping("sendMessage")
    public void sendStringMessage(HttpServletRequest request, String receiverName, String sendMessageText, @RequestBody MultipartFile file) throws IOException {
        String filePath = null;
        if (null != file && !file.getOriginalFilename().equals("") && file.getBytes().length > 0) {
            String parentFilePath = new File("").getAbsolutePath()
                    + File.separator + ((User) request.getSession().getAttribute("userName")).getUserName();
            File parentFile = new File(parentFilePath);
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            String fileName = file.getOriginalFilename();
            filePath = parentFilePath + File.separator + fileName.substring(0, fileName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
            file.transferTo(new File(filePath));
            logger.info("保存文件至" + filePath);
            filePath = filePath.replaceAll("\\\\", "/");
        }

        String senderName = ((User) request.getSession().getAttribute("userName")).getUserName();
        WebSocketMessage webSocketMessage;
        if (Cache.getExistUserNameList().contains(receiverName)) {
            webSocketMessage = new WebSocketMessage((byte) 1, sendMessageText, filePath, senderName, receiverName);
            GlobalWebSocket.sendMessageToUser(webSocketMessage);
        } else {
            webSocketMessage = new WebSocketMessage((byte) 0, "用户不存在", null, senderName, null);
            GlobalWebSocket.sendMessageToUser(webSocketMessage);
        }
        if (null != webSocketMessage && null != webSocketMessage.getFileMsg() && !"".equals(webSocketMessage.getFileMsg().trim())) {
            Cache.addCacheFile(webSocketMessage);
        }
    }

    @RequestMapping("getFile")
    public void getFile(String filePath, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(filePath.substring(filePath.lastIndexOf("/") + 1),"UTF-8"));
        File file = new File(filePath);

        byte[] bytes = new byte[1024 * 1024];
        FileInputStream fileInputStream = new FileInputStream(file);
        int readLength;
        while (-1 != (readLength = fileInputStream.read(bytes))) {
            response.getOutputStream().write(bytes, 0, readLength);
        }
        fileInputStream.close();
    }
}
