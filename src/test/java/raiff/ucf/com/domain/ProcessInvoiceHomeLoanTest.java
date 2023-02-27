package raiff.ucf.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import raiff.ucf.com.web.rest.TestUtil;

class ProcessInvoiceHomeLoanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessInvoiceHomeLoan.class);
        ProcessInvoiceHomeLoan processInvoiceHomeLoan1 = new ProcessInvoiceHomeLoan();
        processInvoiceHomeLoan1.setId(1L);
        ProcessInvoiceHomeLoan processInvoiceHomeLoan2 = new ProcessInvoiceHomeLoan();
        processInvoiceHomeLoan2.setId(processInvoiceHomeLoan1.getId());
        assertThat(processInvoiceHomeLoan1).isEqualTo(processInvoiceHomeLoan2);
        processInvoiceHomeLoan2.setId(2L);
        assertThat(processInvoiceHomeLoan1).isNotEqualTo(processInvoiceHomeLoan2);
        processInvoiceHomeLoan1.setId(null);
        assertThat(processInvoiceHomeLoan1).isNotEqualTo(processInvoiceHomeLoan2);
    }
}
