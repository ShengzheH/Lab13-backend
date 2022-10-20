package se331.rest.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se331.rest.entity.Organizer;
import se331.rest.repository.OrganizerRepository;
import se331.rest.security.entity.Authority;
import se331.rest.security.entity.AuthorityName;
import se331.rest.security.entity.User;
import se331.rest.security.repository.AuthorityRepository;
import se331.rest.security.repository.UserRepository;
import se331.rest.security.util.JwtUserFactory;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
    @Autowired
    OrganizerRepository organizerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }
    @Transactional
    public User save(User user){
        Organizer organizer;
        organizer = organizerRepository.save(Organizer.builder()
                .name("NEW").build());
        PasswordEncoder encoder = new BCryptPasswordEncoder( );
        User user1 = User.builder()
                .username ( user.getUsername())
                .password ( encoder.encode ( user.getPassword() ) )
                .firstname ( "NEW" )
                .lastname ( "NEW")
                .email ( user.getEmail() ).enabled (true)
                .lastPasswordResetDate(Date.from(LocalDate.of (2021,01,
                        01).atStartOfDay(ZoneId.systemDefault ()).toInstant()))
                .build();


        Authority authority = authorityRepository.findByName(ROLE_USER);
        List<User> userList = authorityRepository.findByName(ROLE_USER).getUsers();

        userList.add(user1);
        authorityRepository.findByName(ROLE_USER).setUsers(userList);
        authority.setUsers(null);

        user1.getAuthorities().add(authority);

        System.out.println(1);
        userRepository.save(user1);
        organizer.setUser(user1);
        Organizer organizer1 = new Organizer();
        organizer1.setUser(null);
        organizer1.setId(organizer.getId());
        organizer1.setName(organizer.getName());
        user1.setOrganizer(organizer1);
        return user1;
//        return temp;
    }
}
