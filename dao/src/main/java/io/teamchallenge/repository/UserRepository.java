package io.teamchallenge.repository;

import io.teamchallenge.dto.user.UserVO;
import io.teamchallenge.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("select new io.teamchallenge.dto.user.UserVO(u.id, u.email, u.role) from User u where u.email=:email")
    Optional<UserVO> findUserVOByEmail(String email);
    Optional<User> findUserByEmail(String email);
}
