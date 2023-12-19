package com.smartcampus.test;

import java.util.UUID;

public class AsaduTest {
    public static void main(String []args){
        System.out.println(String.valueOf(UUID.randomUUID().getMostSignificantBits()).replace("-","").substring(0,7));
    }
}
