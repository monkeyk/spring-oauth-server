package com.monkeyk.sos.domain.user;

import com.monkeyk.sos.domain.shared.Repository;

import java.util.List;

/**
 * @author Shengzhao Li
 */

public interface UserRepository extends Repository {

    User findByGuid(String guid);

    void saveUser(User user);

    void updateUser(User user);

    User findByUsername(String username);

    /**
     * 查询 User 的 各类 profile 基础数据
     * 包括 phone, email, address, nickname, updated_at
     *
     * @param username username
     * @return User  only have profile fields
     * @since 3.0.0
     */
    User findProfileByUsername(String username);

    /**
     * 注意：产品化的设计此处应该有分页会更好
     */
    List<User> findUsersByUsername(String username);
}