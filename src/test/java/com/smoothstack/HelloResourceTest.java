package com.smoothstack;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloResourceTest extends JerseyTest {

    @Override
    public ResourceConfig configure() {
        return new ResourceConfig(HelloResource.class);
    }

    @Test
    public void testGetMessage() {
        String responseMsg = target("hello").request().get(String.class);
        assertThat(responseMsg).isEqualTo("Hello, Smoothstack!");
    }
}
