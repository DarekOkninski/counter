package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.Role;
import pl.stadler.counter.repositories.RoleRepo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {

    private final RoleRepo roleRepo;

    @Autowired
    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Role> findAllByOrderByName() {
        return roleRepo.findAllByOrderByName();
    }

    public Role findByName(String name) {
        return roleRepo.findByName(name).orElse(null);
    }
}
