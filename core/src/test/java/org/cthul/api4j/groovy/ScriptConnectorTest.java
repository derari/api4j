package org.cthul.api4j.groovy;

import groovy.util.ResourceException;
import java.net.URLConnection;
import org.cthul.resolve.ClassResourceResolver;
import org.cthul.resolve.ResourceResolver;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class ScriptConnectorTest {
    
    @Test
    public void test_getResourceConnection() throws Exception {
        ResourceResolver rs = new ClassResourceResolver(ScriptConnectorTest.class)
                .addDomain("test:");
        ScriptConnector sc = new ScriptConnector(rs);
        URLConnection uc = sc.getResourceConnection("test:emptyScript.api.groovy");
        assertThat(uc, notNullValue());
    }
    
    @Test
    public void test_exception() {
        try {
            ResourceResolver rs = new ClassResourceResolver(ScriptConnectorTest.class)
                    .addDomain("test:");
            ScriptConnector sc = new ScriptConnector(rs);
            sc.getResourceConnection("lololol");
            assertThat("Exception expected", false);
        } catch (ResourceException e) {
            assertThat(e.getMessage(), containsString("lololol"));
        }
    }
}
