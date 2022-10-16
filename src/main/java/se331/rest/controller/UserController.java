package se331.rest.controller;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se331.rest.security.entity.User;
import se331.rest.security.service.JwtUserDetailsServiceImpl;
import se331.rest.util.LabMapper;

@Controller
public class UserController {
    @Autowired
    JwtUserDetailsServiceImpl jwtUserDetailsService;

    @PostMapping("/user")
    public ResponseEntity<?> adduser(@RequestBody User user) {
        User output = jwtUserDetailsService.save(user);
        return ResponseEntity.ok(output);
    }
}
