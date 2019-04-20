package com.mightywidgets.error;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WidgetErrorAdviceTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(WidgetErrorAdvice.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void handleNotFoundException() {
    }

    @Test
    public void handleRuntimeException() {
    }

    @Test
    public void constraintViolationException() {
    }

    @Test
    public void handleMethodArgumentNotValid() {
    }

    @Test
    public void handleHttpMessageNotReadable() {
    }
}
