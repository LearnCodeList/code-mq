package cn.code.service.busi;

import cn.code.dao.OrderExpDao;
import cn.code.model.OrderExp;
import cn.code.service.delay.IDelayOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 类说明：订单相关的服务
 */
@Service
public class SaveOrder {
    private Logger logger = LoggerFactory.getLogger(SaveOrder.class);

    public final static short UNPAY = 0;
    public final static short PAYED = 1;
    public final static short EXPIRED = -1;

    @Autowired
    private OrderExpDao orderExpDao;

    @Autowired
    @Qualifier("rocketmq")
    private IDelayOrder delayOrder;

    /**
     * 接收前端页面参数，生成订单
     *
     * @param orderNumber 订单个数
     */
    public void insertOrders(int orderNumber) throws Exception{
        OrderExp orderExp;
        for (int i = 0; i < orderNumber; i++) {
            //这个是设置延时消息的属性
            //"1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"  18个等级
            long expire_duration = 30;
            long expireTime = 8;
			Thread.sleep(5000);
			orderExp = new OrderExp();
			String orderNo = "DD00_30S_"+i;
			orderExp.setOrderNo(orderNo);
			orderExp.setOrderNote("延迟订单——" + orderNo);
			orderExp.setOrderStatus(UNPAY);
			orderExpDao.insertDelayOrder(orderExp, expire_duration);
			logger.info("保存订单到DB:" + orderNo);
			//TODO 这里需要把订单信息存入RocketMQ
            delayOrder.orderDelay(orderExp, expireTime);
        }
    }

    @PostConstruct
    public void initDelayOrder() {
        logger.info("系统启动，扫描表中过期未支付的订单并处理.........");

        int counts = orderExpDao.updateExpireOrders();
        logger.info("系统启动，处理了表中[" + counts + "]个过期未支付的订单！");

        List<OrderExp> orderList = orderExpDao.selectUnPayOrders();
        logger.info("系统启动，发现了表中还有[" + orderList.size() + "]个未到期未支付的订单！推入检查队列准备到期检查....");
        for (OrderExp order : orderList) {
            long expireTime = order.getExpireTime().getTime() - (System.currentTimeMillis());
            delayOrder.orderDelay(order, expireTime);
        }
    }


}
