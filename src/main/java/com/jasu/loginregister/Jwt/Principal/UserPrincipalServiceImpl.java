package com.jasu.loginregister.Jwt.Principal;

import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPrincipalServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username);
    if (user==null){
      throw new UsernameNotFoundException("User Not Found with username: " + username);
    }
    UserPrincipal userPrincipal = UserMapper.toUserPrincipal(user);
    userPrincipal.setUsername(user.getEmail());
    return userPrincipal;
  }

}
