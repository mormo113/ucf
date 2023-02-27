package raiff.ucf.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import raiff.ucf.com.web.rest.TestUtil;

class ProcessPersonalLoanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessPersonalLoan.class);
        ProcessPersonalLoan processPersonalLoan1 = new ProcessPersonalLoan();
        processPersonalLoan1.setId(1L);
        ProcessPersonalLoan processPersonalLoan2 = new ProcessPersonalLoan();
        processPersonalLoan2.setId(processPersonalLoan1.getId());
        assertThat(processPersonalLoan1).isEqualTo(processPersonalLoan2);
        processPersonalLoan2.setId(2L);
        assertThat(processPersonalLoan1).isNotEqualTo(processPersonalLoan2);
        processPersonalLoan1.setId(null);
        assertThat(processPersonalLoan1).isNotEqualTo(processPersonalLoan2);
    }
}
