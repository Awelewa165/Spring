package com.kemal.spring.service;

import com.kemal.spring.domain.RoleRepository;
import com.kemal.spring.domain.User;
import com.kemal.spring.domain.UserRepository;
import com.kemal.spring.web.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Keno&Kemo on 18.10.2017..
 */

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    //find methods
    public List<User> findAll(){
        return userRepository.findAll();
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findById(Long id){
        return userRepository.findById(id);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public void saveUser(User user) {
        userRepository.save(user);
    }
    public User createNewAccount(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        return user;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
        if (user==null){
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(user);
    }



}
