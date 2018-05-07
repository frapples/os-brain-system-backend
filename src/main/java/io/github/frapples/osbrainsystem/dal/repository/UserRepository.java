package io.github.frapples.osbrainsystem.dal.repository;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import io.github.frapples.osbrainsystem.biz.converter.ModelConverter;
import io.github.frapples.osbrainsystem.biz.model.User;
import io.github.frapples.osbrainsystem.dal.dao.UserDO;
import io.github.frapples.osbrainsystem.dal.mapper.UserMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    @Autowired
    private UserMapper userMapper;

    public boolean addUser(User user) {
        UserDO userDO = ModelConverter.convert(user, UserDO.class);
        if (userDO == null) {
            return false;
        } else {
            return userMapper.insert(userDO) >= 1;
        }
    }

    public Page<User> getUsers(Page<User> page) {
        // List<UserDO> dos = userMapper.selectList(new EntityWrapper<>());
        List<UserDO> dos = userMapper.selectWithClass(page, new EntityWrapper<>());
        List<User> users = ModelConverter.convert(dos, User.class);
        page.setRecords(users);
        // FIXME: Total of page wrong. This is a temp patch
        page.setTotal(userMapper.selectCount(new EntityWrapper<>()));
        return page;
    }

    public Optional<User> getUser(String studentId) {
        if (studentId == null) {
            return Optional.empty();
        }
        List<UserDO> resultDO = userMapper.selectList(
            new EntityWrapper<UserDO>()
                .eq("studentId", studentId));
        List<User> result = ModelConverter.convert(resultDO, User.class);
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(result.get(0));
        }
    }

}
