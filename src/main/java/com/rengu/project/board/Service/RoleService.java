package com.rengu.project.board.Service;

import com.rengu.project.board.Entity.RoleEntity;
import com.rengu.project.board.Repository.RoleRepository;
import com.rengu.project.board.Utils.ApplicationMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // 保存角色
    public RoleEntity saveRole(RoleEntity roleEntity) {
        if (StringUtils.isEmpty(roleEntity.getName())) {
            throw new RuntimeException(ApplicationMessages.ROLE_NAME_NOT_FOUND);
        }
        if (hasRoleByName(roleEntity.getName())) {
            throw new RuntimeException(ApplicationMessages.ROLE_NAME_EXISTED + roleEntity.getName());
        }
        return roleRepository.save(roleEntity);
    }

    // 根据名称检查角色是否存在
    public boolean hasRoleByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        return roleRepository.existsAllByName(name);
    }

    // 根据名称查询角色
    public RoleEntity getRoleByName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new RuntimeException(ApplicationMessages.ROLE_NAME_ARGS_NOT_FOUND + name);
        }
        Optional<RoleEntity> roleEntityOptional = roleRepository.findAllByName(name);
        if (!roleEntityOptional.isPresent()) {
            throw new RuntimeException(ApplicationMessages.ROLE_NAME_NOT_FOUND + name);
        }
        return roleEntityOptional.get();
    }
}
