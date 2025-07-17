package com.authguard.authguard.model.mapper;

import com.authguard.authguard.model.dto.AppRequest;
import com.authguard.authguard.model.dto.AppResponse;
import com.authguard.authguard.model.entity.AppEntity;

public class AppMapper {

    public static AppEntity toAppEntity(AppRequest appRequest) {
        AppEntity appEntity = AppEntity.builder().appName(appRequest.getName())
                .build();
        return appEntity;
    }

    public static AppResponse toAppResponse(AppEntity appEntity) {
        AppResponse appResponse = AppResponse.builder().appId(appEntity.getAppId()).appName(appEntity.getAppName())
                .status(appEntity.isStatus()).build();
        return appResponse;
    }
}
