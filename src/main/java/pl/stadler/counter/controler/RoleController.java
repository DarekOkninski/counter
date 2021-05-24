package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.stadler.counter.models.Role;
import pl.stadler.counter.services.RoleService;

import java.util.List;

@RestController
@RequestMapping(path= "/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping(path = "/find-all")
    public List<Role> findAllByOrderByName() {
        return roleService.findAllByOrderByName();
    }

    @GetMapping(path = "/find-by-name")
    public Role findByName(@PathVariable(value = "name") String name) {
        return roleService.findByName(name);
    }
}
