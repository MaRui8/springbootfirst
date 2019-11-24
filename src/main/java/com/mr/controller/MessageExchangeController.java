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
import java.net.URLDecoder;
import java.net.URLEncoder;

@RestController
@RequestMapping("messageExchange")
public class MessageExchangeController {

    private Logger logger = LoggerFactory.getLogger(MessageExchangeController.class);

    private static final String PARENT_FILE_PATH = new File("").getAbsolutePath();

    @RequestMapping("sendMessage")
    public void sendStringMessage(HttpServletRequest request, String receiverName, String sendMessageText, @RequestBody MultipartFile file) throws IOException {
        String fileName = null;
        String senderName = ((User) request.getSession().getAttribute("userName")).getUserName();
        if (null != file && !file.getOriginalFilename().equals("") && file.getBytes().length > 0) {
            String parentFilePath = PARENT_FILE_PATH
                    + File.separator + senderName;
            File parentFile = new File(parentFilePath);
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("."));
            String filePath = parentFilePath + File.separator + fileName;
            file.transferTo(new File(filePath));
            logger.info("保存文件至" + filePath);
        }

        WebSocketMessage webSocketMessage;
        String fileAttr = (senderName + File.separator + fileName).replaceAll("\\\\", "/");
        if (Cache.getExistUserNameList().contains(receiverName)) {
            webSocketMessage = new WebSocketMessage((byte) 1, sendMessageText, fileAttr, senderName, receiverName);
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
    public void getFile(String filePath, HttpServletResponse response, HttpServletRequest request) throws IOException {

        response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(filePath.substring(filePath.lastIndexOf("/") + 1), "UTF-8"));
        File file = new File(PARENT_FILE_PATH + File.separator + filePath);

        byte[] bytes = new byte[1024 * 1024];
        FileInputStream fileInputStream = new FileInputStream(file);
        int readLength;
        while (-1 != (readLength = fileInputStream.read(bytes))) {
            response.getOutputStream().write(bytes, 0, readLength);
        }
        fileInputStream.close();
    }
}
