package cn.tianjin.unifiedfee.ot.quartzobj;

import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spiritdata.framework.util.JsonUtils;

import cn.tianjin.unifiedfee.ot.util.HttpFuncs;

@Service
public class DoQuartz {

    // 每十秒执行一次
    @Scheduled(cron = "*/10 * * * * ?")
    // 每天23点59执行
    // @Scheduled(cron = "0 59 23 * * ?")
    public void work() {
        System.out.println("----------------------------------");
        System.out.println("每五秒执行一次");
        System.out.println("----------------------------------");

        String postUrl = "http://1.202.219.107:8088/system-server/api/support/company-info/list";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNum", "1");
        map.put("pageSize", "10000");
        String sendPost = HttpFuncs.sendPost(postUrl, JsonUtils.objToJson(map), false);
        
    }
}
