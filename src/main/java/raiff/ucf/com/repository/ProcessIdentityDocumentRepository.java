package raiff.ucf.com.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import raiff.ucf.com.domain.ProcessIdentityDocument;

/**
 * Spring Data JPA repository for the ProcessIdentityDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessIdentityDocumentRepository extends JpaRepository<ProcessIdentityDocument, Long> {}
