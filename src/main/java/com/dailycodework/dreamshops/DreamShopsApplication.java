package com.dailycodework.dreamshops;

import com.dailycodework.dreamshops.security.jwt.JwtKeyGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootApplication
public class DreamShopsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DreamShopsApplication.class, args);

    }

}
