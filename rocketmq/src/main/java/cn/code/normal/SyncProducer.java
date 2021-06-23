package cn.code.normal;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 *
 * <p>Title: SyncProducer</p>
 * <p>
 *     Description: 同步发送
 * </p>
 *
 * @author weixing.yang
 * @version 1.0.0
 * @date 2021/6/22 16:34
 */
public class SyncProducer {

    public static void main(String[] args) throws Exception {
        //生产者实例化
        DefaultMQProducer producer = new DefaultMQProducer("sync");
        //指定rocket服务地址
        producer.setNamesrvAddr("192.168.112.140:9876");
        //启动实例
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message msg = new Message("TopicTest" , "TagB" , ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n%n%n", sendResult.getSendStatus()+":(MsgId):"
                    +sendResult.getMsgId()+":(queueId):"
                    +sendResult.getMessageQueue().getQueueId()
                    +"(value):"+ new String(msg.getBody()));
        }
        producer.shutdown();
    }
}
