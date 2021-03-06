package ntua.softeng28.evcharge.user;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
class AddUser {

  private static final Logger log = LoggerFactory.getLogger(AddUser.class);
  
  @Autowired
  BCryptPasswordEncoder passwordEncoder; 

  @Bean
  CommandLineRunner initDatabase(UserRepository repository) {

    return args -> {
      try{
        log.info("Preloading {}", repository.save(new User("admin", passwordEncoder.encode("petrol4ever"), false, "ROLE_ADMIN", new HashSet<>())));
      }
      catch(RuntimeException e){
        log.error("Failed to add users to DB:", e.getMessage());
      }
    };
  }
}