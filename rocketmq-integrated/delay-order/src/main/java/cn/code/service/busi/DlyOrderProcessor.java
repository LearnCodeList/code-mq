package cn.code.service.busi;

import cn.code.dao.OrderExpDao;
import cn.code.model.OrderExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 类说明：处理过期订单的服务
 */
@Service
public class DlyOrderProcessor {
    private Logger logger = LoggerFactory.getLogger(DlyOrderProcessor.class);

    @Autowired
    public OrderExpDao orderExpDao;

    /**
     * 检查数据库中指定id的订单的状态,如果为未支付，则修改为已过期
     */
    public void checkDelayOrder(OrderExp record) {
        OrderExp dbOrder = orderExpDao.selectByPrimaryKey(record.getId());
        if (dbOrder.getOrderStatus() == SaveOrder.UNPAY) {
            logger.info("订单【" + record + "】未支付已过期，需要更改为过期订单！");
            orderExpDao.updateExpireOrder(record.getId());
        } else {
            logger.info("已支付订单【" + record + "】，无需修改！");
        }

    }

}
