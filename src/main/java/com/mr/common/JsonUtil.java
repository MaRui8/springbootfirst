package com.mr.common;

import com.fasterxml.jackson.databind.json.JsonMapper;

import java.text.SimpleDateFormat;

public class JsonUtil {

    public static final JsonMapper mapper = JsonMapper.builder().defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).build();
}
