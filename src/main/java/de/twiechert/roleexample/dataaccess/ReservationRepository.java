package de.twiechert.roleexample.dataaccess;

import de.twiechert.roleexample.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Tayfun Wiechert <tayfun.wiechert@gmail.com>
 */
public interface ReservationRepository extends PagingAndSortingRepository<User, Long> {

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Page<User> findAll(Pageable pageable);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Iterable<User> findAll(Sort sort);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Iterable<User> findAll(Iterable<Long> longs);

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    Iterable<User> findAll();
}
