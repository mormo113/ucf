package raiff.ucf.com.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import raiff.ucf.com.domain.ProcessPersonalLoan;

/**
 * Spring Data JPA repository for the ProcessPersonalLoan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessPersonalLoanRepository extends JpaRepository<ProcessPersonalLoan, Long> {}
