package cn.tianjin.unifiedfee.ot.quartzobj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.spiritdata.framework.util.JsonUtils;

import cn.tianjin.unifiedfee.ot.service.AllCompanyService;
import cn.tianjin.unifiedfee.ot.util.HttpFuncs;

@Service
public class DoQuartz {

    @Autowired
    private AllCompanyService allCompanyService;

    // 每十秒执行一次
    //@Scheduled(cron = "*/10 * * * * ?")
    // 每天23点59执行
    @Scheduled(cron = "0 59 23 * * ?")
    public void work() throws Exception {
        System.out.println("----------------------------------");
        System.out.println("每五秒执行一次");
        System.out.println("----------------------------------");

        String postUrl = "http://1.202.219.107:8088/system-server/api/support/company-info/list";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageNum", "1");
        map.put("pageSize", "10000");
        String sendPost = HttpFuncs.sendPost(postUrl, JsonUtils.objToJson(map), false);
        if (StringUtils.isNotEmpty(sendPost)) {
            Map<String, Object> result = new HashMap<String, Object>();
            Gson gson = new Gson();
            result = gson.fromJson(sendPost, result.getClass());
            if (result != null) {
                String success = result.get("success") == null ? "" : result.get("success").toString();
                if (StringUtils.isNotEmpty(success) && success.equals("true")) {
                    Map<String, Object> object = (Map<String, Object>) result.get("data");
                    if (object != null) {
                        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                        dataList = (List<Map<String, Object>>) object.get("rows");
                        if (dataList != null && dataList.size() > 0) {
                            allCompanyService.save(dataList);
                        }
                    }
                }
            }
        }
    }
}
