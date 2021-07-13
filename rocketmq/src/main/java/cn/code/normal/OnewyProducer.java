package cn.code.normal;

import cn.code.constant.ConfigConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 *
 * <p>
 *     Description: 单向发送
 * </p>
 *
 * @author weixing.yang
 * @version 1.0.0
 * @date  2021/6/22 16:00
 */
public class OnewyProducer {

    public static void main(String[] args) throws Exception{
        //生产者实例化
        DefaultMQProducer producer = new DefaultMQProducer("oneway");
        //指定rocket服务地址
        producer.setNamesrvAddr(ConfigConstant.RQ_NAMESRV_ADDR);
        //启动实例
        producer.start();
        for (int i = 0; i < 10; i++) {
            //创建一个消息实例，指定 topic 、tag 和消息体
            Message msg = new Message("TopicTest", "TagA", ("Hello RocketMQ" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            //发送消息
            producer.sendOneway(msg);
            System.out.printf("%s%n", new String(msg.getBody()));
        }
        //生产者实例不再使用时关闭
        producer.shutdown();
    }
}
