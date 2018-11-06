package com.rengu.project.board.Service;

import com.rengu.project.board.Entity.RoleEntity;
import com.rengu.project.board.Entity.UserEntity;
import com.rengu.project.board.Repository.UserRepository;
import com.rengu.project.board.Utils.ApplicationConfig;
import com.rengu.project.board.Utils.ApplicationMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return getUserByUsername(s);
    }

    //保存管理员用户
    public UserEntity saveAdminUser(UserEntity userEntity) {
        return saveUser(userEntity, roleService.getRoleByName(ApplicationConfig.DEFAULT_USER_ROLE_NAME), roleService.getRoleByName(ApplicationConfig.DEFAULT_ADMIN_ROLE_NAME));
    }

    // 保存用户
    @CachePut(value = "User_Cache", key = "#userEntity.getId()")
    public UserEntity saveUser(UserEntity userEntity, RoleEntity... roleEntities) {
        if (StringUtils.isEmpty(userEntity.getUsername())) {
            throw new RuntimeException(ApplicationMessages.USER_USERNAME_ARGS_NOT_FOUND);
        }
        if (hasUserByUsername(userEntity.getUsername())) {
            throw new RuntimeException(ApplicationMessages.USER_USERNAME_EXISTED + userEntity.getUsername());
        }
        if (StringUtils.isEmpty(userEntity.getPassword())) {
            throw new RuntimeException(ApplicationMessages.USER_PASSWORD_ARGS_NOT_FOUND);
        }
        userEntity.setPassword(new BCryptPasswordEncoder().encode(userEntity.getPassword()));
        Set<RoleEntity> roleEntitySet = userEntity.getRoleEntities() == null ? new HashSet<>() : userEntity.getRoleEntities();
        roleEntitySet.addAll(Arrays.asList(roleEntities));
        userEntity.setRoleEntities(roleEntitySet);
        return userRepository.save(userEntity);
    }

    // 根据用户名查询用户是否存在
    public boolean hasUserByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        return userRepository.existsAllByUsername(username);
    }

    // 根据用户名查询用户
    public UserEntity getUserByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException(ApplicationMessages.USER_USERNAME_ARGS_NOT_FOUND + username);
        }
        Optional<UserEntity> userEntityOptional = userRepository.findAllByUsername(username);
        if (!userEntityOptional.isPresent()) {
            throw new UsernameNotFoundException(ApplicationMessages.USER_USERNAME_NOT_FOUND + username);
        }
        return userEntityOptional.get();
    }
}
