//package com.springboot.blogSecurity;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import com.springboot.blog.entity.User;
//import com.springboot.blog.repository.UserRepository;
//@Component
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//	private UserRepository userRepository;
//
//	public CustomUserDetailsService(UserRepository userRepository) {
//		this.userRepository = userRepository;
//	}
//
//
//	@Override
//	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//		
//		User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).
//				orElseThrow(() -> new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));
//
//		Set<GrantedAuthority> authorities = user.getRoles().stream().
//				map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
//				
//		
//		return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
//	}
//		//org.springframework.security.core.userdetails.UserDetailsService.
//}


package com.springboot.blog.security;

import com.springboot.blog.entity.User;
import com.springboot.blog.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
//
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
          User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                 .orElseThrow(() ->
                         new UsernameNotFoundException("User not found with username or email: "+ usernameOrEmail));

        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                authorities);
    }
}

