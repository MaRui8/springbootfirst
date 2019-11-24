package com.mr.task;


import com.mr.common.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileTask {

    private Logger logger = LoggerFactory.getLogger(FileTask.class);

    @Scheduled(cron = "0 0/30 * * * *")
    public void deleteFile() {
        List<Integer> deleteNo = new ArrayList<>();
        Cache.getCacheFileMap().values().forEach(e -> {
            File file = new File(e.getFileMsg());
            if (System.currentTimeMillis() - e.getCreateTime().getTime() > 86400000 || file.length() > 1024 * 1024 * 10) {
                if (file.exists()) {
                    file.delete();
                }
                deleteNo.add(e.getNo());
            }
        });
        StringBuilder sb = new StringBuilder();
        deleteNo.forEach(e -> sb.append(Cache.getCacheFileMap().get(e).getSenderName())
                .append("->").append(Cache.getCacheFileMap().get(e).getReceiverName())
                .append(Cache.getCacheFileMap().get(e).getFileMsg()).append(";"));
        logger.info("delete files [{}]", sb.toString());
        deleteNo.forEach(Cache.getCacheFileMap()::remove);
    }
}
