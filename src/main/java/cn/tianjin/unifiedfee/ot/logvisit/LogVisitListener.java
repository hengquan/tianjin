package cn.tianjin.unifiedfee.ot.logvisit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.tianjin.unifiedfee.ot.entity.LogVisit;
import cn.tianjin.unifiedfee.ot.logvisit.service.LogVisitService;
import cn.tianjin.unifiedfee.ot.util.SpringUtils;

public class LogVisitListener extends Thread {
    private Logger logger=LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LogVisitService lvService;

    /**
     * 启动“访问日志”数据收集监听线程
     */
    public static void begin() {
        Logger logger=LoggerFactory.getLogger(LogVisitListener.class);
        LogVisitListener lvl=new LogVisitListener();
        logger.info("“访问日志”数据收集监听开始启动......");
        logger.info("");
        lvl.start();
    }

    /**
     * 启动任务服务的处理主进程
     */
    @Override
    public void run() {
        //等待spring加载完成
//        try {
//            //sleep(10000);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
        LogVisitMemory agm=LogVisitMemory.getInstance();
        int i=0;
        while (this.lvService==null&&i++<5) {
            try {
                this.lvService=(LogVisitService) SpringUtils.getBean("logVisitService");
            } catch(Exception e) { }
            try { sleep(500); } catch(Exception e) { }
        }
        if (this.lvService!=null) {
            logger.info("“访问日志”数据收集监听启动成功");
            startSave2DB(agm);
        } else {
            logger.info("“访问日志”数据收集监听启动失败");
        }
    }
    /*
     * 写入数据库监控
     */
    private void startSave2DB(LogVisitMemory agm) {
        while (true) {
            try {
                LogVisit alp=agm.takeQueue();
                if (alp!=null) {
                    lvService.Save2DB(alp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}