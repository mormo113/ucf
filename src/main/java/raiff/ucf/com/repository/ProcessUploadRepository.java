package raiff.ucf.com.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import raiff.ucf.com.domain.ProcessUpload;

/**
 * Spring Data JPA repository for the ProcessUpload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessUploadRepository extends JpaRepository<ProcessUpload, Long> {}
