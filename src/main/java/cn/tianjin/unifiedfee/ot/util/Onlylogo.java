package cn.tianjin.unifiedfee.ot.util;

import java.util.UUID;

public class Onlylogo {

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }
}
