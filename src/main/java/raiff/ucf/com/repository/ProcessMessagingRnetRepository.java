package raiff.ucf.com.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import raiff.ucf.com.domain.ProcessMessagingRnet;

/**
 * Spring Data JPA repository for the ProcessMessagingRnet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessMessagingRnetRepository extends JpaRepository<ProcessMessagingRnet, Long> {}
