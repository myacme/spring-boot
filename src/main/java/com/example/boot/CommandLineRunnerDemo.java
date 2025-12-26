package com.example.boot;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

/**
 * 启动时执行类
 *
 * @author ljx
 * @version 1.0.0
 * @create 2022/1/11 11:20
 */
//@Component
public class CommandLineRunnerDemo implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(CommandLineRunnerDemo.class);


	@Override
	public void run(String... args) {
		logger.info("项目启动时时启动类");
	}
}