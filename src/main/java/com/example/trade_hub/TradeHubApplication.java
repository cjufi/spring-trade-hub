package com.example.trade_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class TradeHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeHubApplication.class, args);
    }


//    @Bean
//    CommandLineRunner runner(UserRepository userRepository, PasswordEncoder encoder){
//        return args -> {
//            userRepository.save(new AppUser("milos@gmail.com",encoder.encode("aleksa111"),"Milos","NS","123",600,null));
//        };
//    }
}
