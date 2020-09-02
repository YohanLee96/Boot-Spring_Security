package com.study.login.repository.redis;

import com.study.login.model.redis.Login;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRedisRepository extends CrudRepository<Login, String> {

}
