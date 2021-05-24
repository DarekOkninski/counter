package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.stadler.counter.models.User;
import pl.stadler.counter.services.UserService;


import java.util.List;


@RestController
@RequestMapping(path="/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/find-user-by-username/{username}")
    public User findUserByUsername(@PathVariable(value = "username") String username) {
        return userService.findUserByUsername(username);
    }

    @GetMapping(path = "/find-all-by-order-by-username")
    public List<User> findAllByOrderByUsername() {
        return userService.findAllByOrderByUsername();
    }

    @PostMapping (path = "/save")
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping (path = "/change-role")
    public User changeRole(@RequestBody User user) {
        return userService.changeRole(user);
    }

}
