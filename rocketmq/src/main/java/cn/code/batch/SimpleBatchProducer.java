package cn.code.batch;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: SimpleBatchProducer</p>
 * <p>
 * Description: 简单批量发送
 * </p>
 *
 * @author weixing.yang
 * @version 1.0.0
 * @date 2021/6/23 16:02
 */
public class SimpleBatchProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("batch");
        //指定rocket服务地址
        producer.setNamesrvAddr("192.168.112.140:9876");
        producer.start();
        String topic = "TopicTest";
        List<Message> messages = new ArrayList<Message>();
        messages.add(new Message(topic, "TagA", "OrderId001","Hello world 1".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderId002","Hello world 2".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderId003","Hello world 3".getBytes()));

        producer.send(messages);
        System.out.printf("batch over");
        producer.shutdown();
    }
}
