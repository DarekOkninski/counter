package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.Role;


import java.util.Optional;
import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    List<Role> findAllByOrderByName();

    Optional<Role> findByName(String name);
}

