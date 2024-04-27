package io.teamchallenge.repository;

import io.teamchallenge.dto.user.UserVO;
import io.teamchallenge.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves an {@link Optional} of {@link UserVO} by the user's email.
     *
     * @param email the email of the user.
     * @return an Optional containing the UserVO object, if found.
     */
    @Query("select new io.teamchallenge.dto.user.UserVO(u.id, u.email, u.role) from User u where u.email=:email")
    Optional<UserVO> findUserVOByEmail(String email);

    /**
     * Retrieves an {@link Optional} of {@link User} by the user's email.
     *
     * @param email the email of the user.
     * @return an Optional containing the User object, if found.
     */
    Optional<User> findUserByEmail(String email);
}