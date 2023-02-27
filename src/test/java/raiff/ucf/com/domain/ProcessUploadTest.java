package raiff.ucf.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import raiff.ucf.com.web.rest.TestUtil;

class ProcessUploadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessUpload.class);
        ProcessUpload processUpload1 = new ProcessUpload();
        processUpload1.setId(1L);
        ProcessUpload processUpload2 = new ProcessUpload();
        processUpload2.setId(processUpload1.getId());
        assertThat(processUpload1).isEqualTo(processUpload2);
        processUpload2.setId(2L);
        assertThat(processUpload1).isNotEqualTo(processUpload2);
        processUpload1.setId(null);
        assertThat(processUpload1).isNotEqualTo(processUpload2);
    }
}
