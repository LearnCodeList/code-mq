package cn.code.controller;

import cn.code.producer.RocketMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rocket")
public class RocketController {

    @Autowired
    @Qualifier("rocketMQProducer")
    private RocketMQProducer producer;

    /**
     * @param message
     * @return String
     */
    @ResponseBody
    @RequestMapping("spring")
    public String queueSender(@RequestParam("message") String message) {
        String opt = "";
        try {
            Message msg = new Message("rocket-spring-topic", "TAG1", message.getBytes());
            SendResult result = producer.getDefaultMQProducer().send(msg);
            if (result.getSendStatus() != null && result.getSendStatus().name().equals("SEND_OK")) {
                opt = "suc";
            } else {
                opt = "err";
            }

        } catch (Exception e) {
            opt = e.getCause().toString();
        }
        return opt;
    }

    /**
     * @param message
     * @return String
     */
    @ResponseBody
    @RequestMapping("springb")
    public String topicSender(@RequestParam("message") String message) {
        String opt = "";
        try {
            Message msg = new Message("rocket-spring-topic-b", "TAG1", message.getBytes());
            SendResult result = producer.getDefaultMQProducer().send(msg);
            if (result.getSendStatus() != null && result.getSendStatus().name().equals("SEND_OK")) {
                opt = "suc";
            } else {
                opt = "err";
            }

        } catch (Exception e) {
            opt = e.getCause().toString();
        }
        return opt;
    }
}
