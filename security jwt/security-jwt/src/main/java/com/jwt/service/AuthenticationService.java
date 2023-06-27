package com.jwt.service;

import com.jwt.auth.AuthenticationRequest;
import com.jwt.auth.AuthenticationResponse;
import com.jwt.auth.RegisterRequest;
import com.jwt.model.Role;
import com.jwt.model.User;
import com.jwt.repository.RoleCustomRepo;
import com.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleCustomRepo roleCustomRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest){
        try {
            User user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(()->new NoSuchElementException("User not found"));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getPassword(),authenticationRequest.getPassword()));

            List<Role> role = null;
            if (user != null) {
                role = roleCustomRepo.getRole(user);
            }

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Set<Role> set = new HashSet<>();
            role.stream().forEach(c->{
                set.add(new Role(c.getName()));
                authorities.add(new SimpleGrantedAuthority(c.getName()));
            });
            user.setRoles(set);
            set.stream().forEach(i->authorities.add(new SimpleGrantedAuthority(i.getName())));
            var jwtAccessToken = jwtService.generateToken(user, authorities);
            var jwtRefreshToken = jwtService.generateRefreshToken(user,authorities);
            return ResponseEntity.ok(AuthenticationResponse.builder().access_token(jwtAccessToken).refresh_token(jwtRefreshToken).email(user.getEmail()).user_name(user.getUser_name()).build());
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (BadCredentialsException e){
            return ResponseEntity.badRequest().body("Invalid Credentials");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        try {
            if (userRepository.existsById(registerRequest.getEmail().toString())){
                throw new IllegalArgumentException("User with "+ registerRequest.getEmail() +"email already exist");
            }
            userService.saveUser(new User(registerRequest.getMobile_number(),registerRequest.getUser_name(),registerRequest.getEmail(), registerRequest.getPassword(),new HashSet<>()));
            userService.addUser(registerRequest.getEmail(),"ROLE_USER"); // default role
            User user = userRepository.findByEmail(registerRequest.getEmail()).orElseThrow();
            return ResponseEntity.ok(user);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
