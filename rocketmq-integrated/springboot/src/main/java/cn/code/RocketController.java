package cn.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rocket")
public class RocketController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MQProducer mqProducer;

    @RequestMapping(value = "/send")
    public String sendrocket(@RequestParam(required = false) String data,
                            @RequestParam(required = false) String tag) {
        try {
            logger.info("rocket的消息={}", data);
            mqProducer.sendMessage(data,"TopicTest", tag, null);
            return "发送rocket成功";
        } catch (Exception e) {
            logger.error("发送rocket异常：", e);
            return "发送rocket失败";
        }
    }

}
