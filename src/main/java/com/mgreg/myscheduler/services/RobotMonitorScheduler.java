package com.mgreg.myscheduler.services;

import com.mgreg.myscheduler.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RobotMonitorScheduler implements MonitorInterface {


    private static final Logger log = LoggerFactory.getLogger(RobotMonitorScheduler.class);
    final private RestTemplate resttemplate;
    final private NotifyService notifyService;

    @Value("${bot.scheduler.from}")
    private String fromMail;

    public RobotMonitorScheduler(RestTemplate resttemplate, NotifyService notifyService){
        this.resttemplate = resttemplate;
        this.notifyService=notifyService;
    }

    @Override
    @Scheduled(fixedRateString = "${bot.scheduler.interval}")
    public void monitor() {
        log.info("The time is now {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        Quote quote = resttemplate.getForObject("https://quoters.apps.pcfone.io/api/random", Quote.class);
        log.info ("quote is {} ", quote.toString());
         MessageInfo messageInfo = new EmailMessageInfo(fromMail,"gregory.moldavsky@gmail.com",
                 "test",
                 quote.getValue().getQuote());

        notifyService.sendNotification(messageInfo);

    }
}