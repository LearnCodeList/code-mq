package cn.code.transaction;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.concurrent.*;

/**
 *
 * <p>Title: TransactionProducer</p>
 * <p>
 *     Description: 事务消息-生产者
 * </p>
 *
 * @author weixing.yang
 * @version 1.0.0
 * @date 2021/6/23 16:29
 */
public class TransactionProducer {

    public static void main(String[] args) throws Exception{
        TransactionListener transactionListener = new TransactionProducerImpl();
        TransactionMQProducer producer = new TransactionMQProducer("transaction");
        //指定rocket服务地址
        producer.setNamesrvAddr("192.168.112.140:9876");
        ExecutorService executorService = new ThreadPoolExecutor(2,5,
                100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2000),
                new ThreadFactory(){
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });
        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.start();

        String[] tags = new String[]{"TagA","TagB","TagC","TagD","TagE"};
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("TopicTest", tags[i%tags.length],
                    "KEY"+i,("Hello RocketMQ"+i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.sendMessageInTransaction(msg, null);
            System.out.printf("%s%n", sendResult);
            Thread.sleep(10);
        }
        for (int i = 0; i < 100000; i++) {
            Thread.sleep(1000);
        }
        producer.shutdown();
    }
}
