package couchbase.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Couchbase backend database application.
 * Steps to use:
 * 1. Check the console is up and running from C:\Program Files\Couchbase\Server\couchbase_console
 * 2. Login credentials:
 * Cluster: Tutorial
 * Admin: admin
 * Password: 02811a0433
 * 3. Custom bucket: restful-sample
 * 4. For connectivity make sure a new user is created with username same as bucket name and set password under security tab in console
 * 5. Start the server and use Postman api to hit the api's for testing
 */

@SpringBootApplication
@ComponentScan(basePackages = "couchbase.*")
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
