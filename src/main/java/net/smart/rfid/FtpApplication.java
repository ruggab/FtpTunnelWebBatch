package net.smart.rfid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class FtpApplication {

	public static void main(String[] args) {
		SpringApplication.run(FtpApplication.class, args);
	}

}
 