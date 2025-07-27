package com.authguard.authguard.services;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.authguard.authguard.model.dto.AuthCodePayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void saveAuthCode(String code, AuthCodePayload authCodePayload) throws JsonProcessingException {
        String payLoadJson = new ObjectMapper().writeValueAsString(authCodePayload);
        redisTemplate.opsForValue().set(code, payLoadJson, Duration.ofMinutes(5));
    }

    public AuthCodePayload getAuthCodePayLoad(String code) throws JsonProcessingException {
        System.out.println("code is " + code);
        String payloadJson = redisTemplate.opsForValue().get(code);
        if (payloadJson != null) {
            System.out.println("payload is " + payloadJson);
            redisTemplate.delete(code); // One-time use
            return new ObjectMapper().readValue(payloadJson, AuthCodePayload.class);
        }
        return null;
    }

}
