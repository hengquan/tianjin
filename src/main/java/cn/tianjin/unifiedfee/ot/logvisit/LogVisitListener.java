package cn.tianjin.unifiedfee.ot.logvisit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.spiritdata.framework.ext.spring.SpringGetBean;
import cn.tianjin.unifiedfee.ot.entity.LogVisit;
import cn.tianjin.unifiedfee.ot.logvisit.service.LogVisitService;

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
        lvl.setName("“访问日志”数据收集监听");
        lvl.start();
        logger.info("“访问日志”数据收集监听启动成功");
    }

    /**
     * 启动任务服务的处理主进程
     */
    @Override
    public void run() {
        LogVisitMemory agm=LogVisitMemory.getInstance();
        int i=0;
        while (this.lvService==null&&i++<5) {
            try {
                this.lvService=SpringGetBean.getBean("logVisitService");
            } catch(Exception e) { }
            try { sleep(500); } catch(Exception e) { }
        }
        if (this.lvService!=null) {
            logger.info("获得访问日志服务成功");
            startSave2DB(agm);
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