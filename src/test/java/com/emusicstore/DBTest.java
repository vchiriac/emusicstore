package com.emusicstore;

import com.emusicstore.configuration.HibernateConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {HibernateConfiguration.class},loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")

public class DBTest {
    @Autowired
    DataSource dataSource;
    @Autowired


    @Test
    public void testDummy() throws SQLException {
        String schema = dataSource.getConnection().getCatalog();
        assertTrue("emusicstore".equalsIgnoreCase(schema));
    }

}