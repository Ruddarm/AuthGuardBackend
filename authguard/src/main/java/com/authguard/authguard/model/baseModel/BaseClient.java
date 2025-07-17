package com.authguard.authguard.model.baseModel;

import java.util.List;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

import com.authguard.authguard.model.entity.AppEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseClient {
    private String name;
    private String email;
    private String contactNumber;
    private String country;
}
