package com.jasu.loginregister.Jwt.Principal;

import com.jasu.loginregister.BruteForce.LoginAttemptService;
import com.jasu.loginregister.BruteForce.RequestUtils;
import com.jasu.loginregister.Entity.User;
import com.jasu.loginregister.Exception.NotFoundException;
import com.jasu.loginregister.Model.Mapper.UserMapper;
import com.jasu.loginregister.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.jasu.loginregister.Entity.DefinitionEntity.DEStateMessage.MAX_FAILED_LOGIN;

@Service
public class UserPrincipalServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LoginAttemptService loginAttemptService;

  @Autowired
  private HttpServletRequest request;

  @Override
  @Transactional
  public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username);

    UserPrincipal userPrincipal = UserMapper.toUserPrincipal(user);
        return userPrincipal;
  }

  @Transactional
  public UserPrincipal loadUserById(long userId) {

    String ip = RequestUtils.getClientIP(request);
    if (loginAttemptService.isBlocked(ip)) {
      throw new RuntimeException("block_ip");
    }

    Optional<User> user = userRepository.findById(userId);

    if (user==null){
      throw new NotFoundException("User Not Found ");
    }
    UserPrincipal userPrincipal = UserMapper.toUserPrincipal(user.get());
    return userPrincipal;
  }

}
