package com.example.helloapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public HelloResponse hello() {
        String koreaTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString();
        long timestamp = System.currentTimeMillis();
        return new HelloResponse(koreaTime, timestamp, "Hello, World!");
    }
}
