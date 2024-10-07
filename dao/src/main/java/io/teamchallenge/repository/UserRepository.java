package io.teamchallenge.repository;

import io.teamchallenge.dto.user.UserVO;
import io.teamchallenge.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository interface for managing {@link User} entities.
 * Provides methods to perform CRUD operations and custom queries.
 * @author Niktia Malov
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves an {@link Optional} of {@link UserVO} by the user's email.
     *
     * @param email the email of the user.
     * @return an Optional containing the UserVO object, if found.
     */
    @Query("select new io.teamchallenge.dto.user.UserVO(u.id, u.firstName, u.lastName, u.email, u.role) "
           + "from User u where u.email=:email")
    Optional<UserVO> findUserVOByEmail(String email);

    /**
     * Retrieves an {@link Optional} of {@link User} by the user's email.
     *
     * @param email the email of the user.
     * @return an Optional containing the User object, if found.
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Retrieves boolean true if there is already user with sane email address.
     *
     * @param email the email of the user.
     * @return a boolean true if there is already user with sane email.
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves boolean true if there is already user with sane phone number.
     *
     * @param phoneNumber the phoneNumber of the user.
     * @return a boolean true if there is already user with sane phone number.
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Retrieves boolean true if there is already user with completed order with product.
     *
     * @param userId the phoneNumber of the user.
     * @param productId the phoneNumber of the user.
     * @return a boolean true if there is already user with completed order with product.
     */
    @Query("select count(u)> 0 "
           + "from User u "
           + "left join u.orders o "
           + "left join o.orderItems oi "
           + "where u.id = :userId and o.deliveryStatus = 'COMPLETED' and oi.product.id = :productId")
    boolean existsByIdAndCompletedOrderWithProductId(Long userId, Long productId);

    /**
     * Finds a user view object (UserVO) by the given order ID.
     *
     * @param orderId the ID of the order associated with the user.
     * @return an {@link Optional} containing the {@link UserVO} if a user is found, otherwise empty.
     */
    @Query("select new io.teamchallenge.dto.user.UserVO(u.id, u.firstName, u.lastName, u.email, u.role) "
           + "from User u left join u.orders o where o.id = :orderId")
    Optional<UserVO> findVOByOrdersId(Long orderId);

    /**
     * Checks if a user has an order with the given order ID.
     *
     * @param userId the ID of the user.
     * @param orderId the ID of the order.
     * @return true if the user has the order, false otherwise.
     */
    @Query("select count(u)> 0 "
           + "from User u "
           + "left join u.orders o "
           + "where u.id = :userId and o.id = :orderId")
    boolean userHasOrderWithId(Long userId, Long orderId);
}