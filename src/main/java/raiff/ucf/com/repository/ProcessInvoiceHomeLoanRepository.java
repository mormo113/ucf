package raiff.ucf.com.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import raiff.ucf.com.domain.ProcessInvoiceHomeLoan;

/**
 * Spring Data JPA repository for the ProcessInvoiceHomeLoan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessInvoiceHomeLoanRepository extends JpaRepository<ProcessInvoiceHomeLoan, Long> {}
