package tz.or.mkapafoundation.whistleblower.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import tz.or.mkapafoundation.whistleblower.web.rest.TestUtil;

public class WitnessTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Witness.class);
        Witness witness1 = new Witness();
        witness1.setId(1L);
        Witness witness2 = new Witness();
        witness2.setId(witness1.getId());
        assertThat(witness1).isEqualTo(witness2);
        witness2.setId(2L);
        assertThat(witness1).isNotEqualTo(witness2);
        witness1.setId(null);
        assertThat(witness1).isNotEqualTo(witness2);
    }
}
