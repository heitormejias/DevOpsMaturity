package br.com.mejias.devopsmaturity.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mejias.devopsmaturity.web.rest.TestUtil;

public class InfraTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Infra.class);
        Infra infra1 = new Infra();
        infra1.setId(1L);
        Infra infra2 = new Infra();
        infra2.setId(infra1.getId());
        assertThat(infra1).isEqualTo(infra2);
        infra2.setId(2L);
        assertThat(infra1).isNotEqualTo(infra2);
        infra1.setId(null);
        assertThat(infra1).isNotEqualTo(infra2);
    }
}
