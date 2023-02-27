package raiff.ucf.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import raiff.ucf.com.web.rest.TestUtil;

class ProcessMessagingRnetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessMessagingRnet.class);
        ProcessMessagingRnet processMessagingRnet1 = new ProcessMessagingRnet();
        processMessagingRnet1.setId(1L);
        ProcessMessagingRnet processMessagingRnet2 = new ProcessMessagingRnet();
        processMessagingRnet2.setId(processMessagingRnet1.getId());
        assertThat(processMessagingRnet1).isEqualTo(processMessagingRnet2);
        processMessagingRnet2.setId(2L);
        assertThat(processMessagingRnet1).isNotEqualTo(processMessagingRnet2);
        processMessagingRnet1.setId(null);
        assertThat(processMessagingRnet1).isNotEqualTo(processMessagingRnet2);
    }
}
