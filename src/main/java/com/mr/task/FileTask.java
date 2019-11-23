package com.mr.task;


import com.mr.common.Cache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileTask {

    @Scheduled(cron = "* 0/30 * * * *")
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
        deleteNo.forEach(Cache.getCacheFileMap()::remove);
    }
}
