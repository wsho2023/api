package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import common.utils.MyMail;

@SpringBootApplication
public class ApiApplication extends SpringBootServletInitializer {
    @Autowired
    private SpringConfig config;

	public static void main(String[] args) {
		//SpringApplication.run(ApiApplication.class, args);
		//https://qiita.com/kazz12211/items/071c65e1f11b3ab7f653
        ConfigurableApplicationContext ctx = SpringApplication.run(ApiApplication.class, args);
        ApiApplication app = ctx.getBean(ApiApplication.class);
        app.execStartup(args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApiApplication.class);
	}
	
	public void execStartup(String[] args) {
		String mailBody;
		mailBody = "Temple path: " + config.getPathTempletePath() + "\n"
				 + "Output path: " + config.getPathOutputPath();
		System.out.println(mailBody);
		
		String toAddr = "test1@gmail.com";
		MyMail mailConf = new MyMail(
				config, toAddr, "", "execStartup", null, mailBody+"\n");	//config.getMailUsername()
        mailConf.sendMailThread();
	}
}
