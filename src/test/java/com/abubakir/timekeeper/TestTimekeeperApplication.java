package com.abubakir.timekeeper;

import org.springframework.boot.SpringApplication;

public class TestTimekeeperApplication {

    public static void main(String[] args) {
        SpringApplication.from(TimekeeperApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
