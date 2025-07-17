package com.authguard.authguard.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ResourceExist extends Exception {
    public ResourceExist(String msg){
        super(msg);
    }
}
