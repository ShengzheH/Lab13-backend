package se331.rest.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se331.rest.security.entity.Authority;
import se331.rest.security.entity.AuthorityName;
import se331.rest.security.entity.User;
import se331.rest.security.repository.AuthorityRepository;
import se331.rest.security.repository.UserRepository;
import se331.rest.security.util.JwtUserFactory;

import java.util.List;

import static se331.rest.security.entity.AuthorityName.ROLE_USER;


/**
 * Created by stephan on 20.03.16.
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }
    public User save(User user){
        PasswordEncoder encoder = new BCryptPasswordEncoder( );
        User temp = new User();
        temp.setUsername(user.getUsername());
        temp.setEmail(user.getEmail());
        temp.setPassword( encoder.encode(user.getPassword()));
        temp.setEnabled(true);
        Authority authority = authorityRepository.findByName(ROLE_USER);
        List<User> userList = authorityRepository.findByName(ROLE_USER).getUsers();
        userList.add(temp);
        authorityRepository.findByName(ROLE_USER).setUsers(userList);
        authority.setUsers(null);
        temp.getAuthorities().add(authority);
        return userRepository.save(temp);
    }
}
