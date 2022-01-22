package com.vlc3k.piasocialnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class PiaSocialnetworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(PiaSocialnetworkApplication.class, args);
    }
}