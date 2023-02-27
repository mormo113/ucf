package raiff.ucf.com.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import raiff.ucf.com.domain.AttachedFile;

/**
 * Spring Data JPA repository for the AttachedFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachedFileRepository extends JpaRepository<AttachedFile, Long> {}
