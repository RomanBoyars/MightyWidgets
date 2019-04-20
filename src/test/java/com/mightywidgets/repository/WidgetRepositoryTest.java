package com.mightywidgets.repository;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WidgetRepositoryTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(InMemoryRepository.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void exists() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findAllById() {
    }

    @Test
    public void count() {
    }

    @Test
    public void deleteById() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void deleteAll() {
    }

    @Test
    public void deleteAll1() {
    }

    @Test
    public void save() {
    }

    @Test
    public void saveAll() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void existsById() {
    }

    @Test
    public void findAll1() {
    }

    @Test
    public void findAll2() {
    }
}
