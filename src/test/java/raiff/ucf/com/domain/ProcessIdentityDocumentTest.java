package raiff.ucf.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import raiff.ucf.com.web.rest.TestUtil;

class ProcessIdentityDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessIdentityDocument.class);
        ProcessIdentityDocument processIdentityDocument1 = new ProcessIdentityDocument();
        processIdentityDocument1.setId(1L);
        ProcessIdentityDocument processIdentityDocument2 = new ProcessIdentityDocument();
        processIdentityDocument2.setId(processIdentityDocument1.getId());
        assertThat(processIdentityDocument1).isEqualTo(processIdentityDocument2);
        processIdentityDocument2.setId(2L);
        assertThat(processIdentityDocument1).isNotEqualTo(processIdentityDocument2);
        processIdentityDocument1.setId(null);
        assertThat(processIdentityDocument1).isNotEqualTo(processIdentityDocument2);
    }
}
