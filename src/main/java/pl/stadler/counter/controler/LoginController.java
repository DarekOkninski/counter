package pl.stadler.counter.controler;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.User;

@RestController
public class LoginController {

    @PostMapping(path = "/login")
    public User login(@RequestBody User user) {
        return user;
    }
}
