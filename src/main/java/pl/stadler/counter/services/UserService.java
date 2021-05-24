package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.MyUserDetails;
import pl.stadler.counter.models.Role;
import pl.stadler.counter.models.User;
import pl.stadler.counter.repositories.RoleRepo;
import pl.stadler.counter.repositories.UserRepo;
import pl.stadler.counter.security.PasswordEncode;

import java.util.*;


@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncode passwordEncode;
    private final RoleRepo roleRepo;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncode passwordEncode, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncode = passwordEncode;
        this.roleRepo = roleRepo;
    }

    public User findUserByUsername(String username) {
        return userRepo.findUserByUsername(username).orElse(null);
    }


    public User save(User user) {
        System.out.println("Wywolana metoda user - save");
        User userTmp = userRepo.findUserByUsername(user.getUsername()).orElse(null);

        if (userTmp == null || (user.getId() != null && user.getId().equals(userTmp.getId()))) {
            user.setPassword(passwordEncode.passwordEncoder().encode(user.getPassword()));

            // ustawienie domyślnej roli dla każdego użytkownika

            Set<String> rolesTmp = new HashSet<>();
            user.getRoles().forEach(x -> {
                rolesTmp.add(x.getName());
            });

//            Role role = roleRepo.findByName("USER").orElse(null);

            Set<Role> roleSet = new HashSet<Role>();
            rolesTmp.forEach(x -> {
                Role role = roleRepo.findByName(x).orElse(null);
                if (role != null)
                    roleSet.add(role);
            });
            user.setRoles(roleSet);

            User usertmp = userRepo.save(user);
            // zmiana hasla aby nie zostalo ono zwrócone podczas przkazywania do forntendu
            usertmp.setPassword("");
            return usertmp;
        }
        return null;
    }

    public User changeRole(User user) {
        System.out.println("Wywolana metoda user - changeRole");
        User userTmp = userRepo.findUserByUsername(user.getUsername()).orElse(null);

        if (user.getId().equals(userTmp.getId())) {
            user.setPassword(userTmp.getPassword());

            // wyszukanie tych samych uprawnień i usunięcie duplikatów z listy
            Set<String> rolesTmp = new HashSet<>();
            user.getRoles().forEach(x -> {
                rolesTmp.add(x.getName());
            });

            Set<Role> roleSet = new HashSet<Role>();
            rolesTmp.forEach(x -> {
                Role role = roleRepo.findByName(x).orElse(null);
                if (role != null)
                    roleSet.add(role);
            });
            user.setRoles(roleSet);

            User usertmp = userRepo.save(user);
            // zmiana hasla aby nie zostalo ono zwrócone podczas przkazywania do forntendu
            usertmp.setPassword("");
            return usertmp;
        }
        return null;
    }

    public User findUserByEmail(String email) {
        return userRepo.findUserByEmail(email).orElse(null);
    }

    public List<User> findAllByOrderByUsername() {
        return userRepo.findAllByOrderByUsername();
    }

    public void update(User user) {
        userRepo.save(user);
    }

    public User updateUser(User user) {
        System.out.println("Wywolana metoda user - update");
        User userTmp = userRepo.findUserByUsername(user.getUsername()).orElse(null);

        if (userTmp != null) {
            user.setPassword(passwordEncode.passwordEncoder().encode(user.getPassword()));
            return userRepo.save(user);
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Wywolana metoda - loadUserByUsername");
        User user = findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("No user found with username:" + username);
        }
        return new MyUserDetails(user);
    }


}
