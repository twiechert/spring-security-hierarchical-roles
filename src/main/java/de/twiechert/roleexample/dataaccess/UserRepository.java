package de.twiechert.roleexample.dataaccess;

import de.twiechert.roleexample.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long>, UserRepositoryCustom {

    @Query("select u from User as u where u.emailAddress = ?#{principal.emailAddress}")
    @PreAuthorize("hasRole('ANY_EMPLOYEE')")
    User findOwnUser();

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Iterable<User> findAll(Iterable<Long> longs);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Iterable<User> findAll();

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Page<User> findAll(Pageable pageable);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Iterable<User> findAll(Sort sort);

    Optional<User> findByEmailAddress(@Param("mail") String emailAddress);

}
