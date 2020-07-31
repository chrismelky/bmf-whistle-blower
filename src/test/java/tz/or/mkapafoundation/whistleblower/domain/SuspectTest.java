package tz.or.mkapafoundation.whistleblower.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import tz.or.mkapafoundation.whistleblower.web.rest.TestUtil;

public class SuspectTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Suspect.class);
        Suspect suspect1 = new Suspect();
        suspect1.setId(1L);
        Suspect suspect2 = new Suspect();
        suspect2.setId(suspect1.getId());
        assertThat(suspect1).isEqualTo(suspect2);
        suspect2.setId(2L);
        assertThat(suspect1).isNotEqualTo(suspect2);
        suspect1.setId(null);
        assertThat(suspect1).isNotEqualTo(suspect2);
    }
}
