package raiff.ucf.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import raiff.ucf.com.web.rest.TestUtil;

class AttachedFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachedFile.class);
        AttachedFile attachedFile1 = new AttachedFile();
        attachedFile1.setId(1L);
        AttachedFile attachedFile2 = new AttachedFile();
        attachedFile2.setId(attachedFile1.getId());
        assertThat(attachedFile1).isEqualTo(attachedFile2);
        attachedFile2.setId(2L);
        assertThat(attachedFile1).isNotEqualTo(attachedFile2);
        attachedFile1.setId(null);
        assertThat(attachedFile1).isNotEqualTo(attachedFile2);
    }
}
