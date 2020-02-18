package br.com.mejias.devopsmaturity.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import br.com.mejias.devopsmaturity.web.rest.TestUtil;

public class ToolsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tools.class);
        Tools tools1 = new Tools();
        tools1.setId(1L);
        Tools tools2 = new Tools();
        tools2.setId(tools1.getId());
        assertThat(tools1).isEqualTo(tools2);
        tools2.setId(2L);
        assertThat(tools1).isNotEqualTo(tools2);
        tools1.setId(null);
        assertThat(tools1).isNotEqualTo(tools2);
    }
}
