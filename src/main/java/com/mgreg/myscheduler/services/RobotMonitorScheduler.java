package com.mgreg.myscheduler.services;

import com.mgreg.myscheduler.domain.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RobotMonitorScheduler implements MonitorInterface {


    private static final Logger log = LoggerFactory.getLogger(RobotMonitorScheduler.class);
    private int countErrors;

    @Autowired
    private ConfigurableApplicationContext context;
    @Value("${bot.scheduler.maxerrors}")
    private int MAX_ERROR_NUM;

    @Autowired
     private RestTemplate resttemplate;
   @Autowired
     private NotifyService notifyService;

   @Value("${bot.scheduler.from}")
    private String fromMail;


    @Override
    @Scheduled(fixedRateString = "${bot.scheduler.interval}")
    public void monitor() {
        try {
            log.info("The time is now {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            Quote quote = resttemplate.getForObject("https://quoters.apps.pcfone.io/api/random", Quote.class);
            if (quote==null)
            {
                throw new IllegalStateException("Quote is empty");
            }
             log.debug("quote is {} ", quote);

            MessageInfo messageInfo = new EmailMessageInfo(fromMail, "gregory.moldavsky@gmail.com",
                    "test",
                    quote.getValue().getQuote());
            log.info("Quote was send by email ");

            notifyService.sendNotification(messageInfo);
        } catch (Exception e) {
            countErrors++;
            log.error("Error occurred for {} time: {} ", countErrors, e.getMessage());
            if(countErrors ==MAX_ERROR_NUM) {
                log.info("Reached max number of errors - {}, so exiting ", MAX_ERROR_NUM);
                //System.exit(1);
                SpringApplication.exit(context);
            }
        }


    }
}