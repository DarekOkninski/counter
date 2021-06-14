package pl.stadler.counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import pl.stadler.counter.controler.KabelListController;
import pl.stadler.counter.models.ProjectSettings;

import java.io.IOException;

@CrossOrigin("*")
@SpringBootApplication
public class CounterApplication {

	public static void main(String[] args) throws IOException {

		SpringApplication.run(CounterApplication.class, args);

	}

}
