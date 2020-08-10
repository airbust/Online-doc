package com.example.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "MAIL")
//监听收到消息时，交给 @RabbitHandler 方法处理，具体使用哪个方法根据 MessageConverter 转换后的参数类型
public class MailListener {

    @Autowired
    private JavaMailSender javaMailSender;

    @RabbitHandler
    public void sendSimpleMail(Map<String, String> map){
        String mail = map.get("mail");
        String code = map.get("code");
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("Yveltals@qq.com");
            simpleMailMessage.setTo(mail);
            simpleMailMessage.setSubject("邮箱验证码");
            simpleMailMessage.setText("邮箱验证码: "+code+"，" + "n分钟内有效");
            javaMailSender.send(simpleMailMessage);//发送
        }catch(Exception e){
            System.out.println("发送失败: "+ e.getMessage());
        }
    }
}
