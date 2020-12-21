package com.antel.picker;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.antel.picker.dao")
@SpringBootApplication
public class PickerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PickerApplication.class, args);
    }

}
