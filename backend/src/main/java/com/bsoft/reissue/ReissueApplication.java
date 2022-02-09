package com.bsoft.reissue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bsoft.reissue.dao")
public class ReissueApplication {

    public static void main(String[] args) {


        SpringApplication.run(ReissueApplication.class, args);
    }

}
