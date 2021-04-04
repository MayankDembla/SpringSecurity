package com.dembla.security.nimbusjwt.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomSpringUserDetailsImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails employeeDetails;
        for (User u : getEmployeesList()) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                if (u.isAccountNonExpired() && u.isAccountNonLocked()) {
                    if (u.getUsername().equalsIgnoreCase(username)) {
                        employeeDetails = new User(u.getUsername(), u.getPassword(),
                                new ArrayList<>());
                        return employeeDetails;
                    } else {
                        throw new UsernameNotFoundException("Employee not found with username: " + u);
                    }
                }
            }
        }
        return null;
    }

    //Some dummy company with dummy employees stored at some dummy database.
    List<User> getEmployeesList() {

        List<User> userList = new ArrayList<>();

        User user1 = new User("Mira", "$2a$10$e59rGaFvpijWXLh03j0aZOzBYQNrIRIjlB8sGwLvBL35fecblsW1m", true, true,
                true, true, new ArrayList<>());
        User user2 = new User("Sam", "$2a$10$e59rGaFvpijWXLh03j0aZOzBYQNrIRIjlB8sGwLvBL35fecblsW1m", false, false,
                false, false, new ArrayList<>());

        User user3 = new User("John", "$2a$10$e59rGaFvpijWXLh03j0aZOzBYQNrIRIjlB8sGwLvBL35fecblsW1m", new ArrayList<>());

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        return userList;
    }
}
