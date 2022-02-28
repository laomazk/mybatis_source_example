package com.magic.mb.mapper;

import com.magic.mb.config.PageHelp;
import com.magic.mb.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author laoma
 * @create 2022-02-26 0:09
 */
@Mapper
public interface UserMapper {
    // @PageHelp
    List<User> selectList();

    int updateById(@Param("user") User user);
}
