package com.kodnito.it.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodnito.restapi.entity.Todo;
import java.io.File;
import java.net.URL;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author hayricicek
 */
@RunWith(Arquillian.class)
public class TodoEndpointTest {

    @ArquillianResource
    private URL webappUrl;

    WebTarget target;

    ObjectMapper om = new ObjectMapper();

    @Before
    public void before() throws Exception {
        target = ClientBuilder.newClient().target(webappUrl.toURI()).path("api");
    }

    @Deployment
    public static WebArchive createDeployment() {
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addAsLibraries(files)
                .addPackages(true, "com.kodnito.restapi")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml");

        System.out.println(war.toString(true));

        return war;
    }

    @Test
    @InSequence(1)
    @RunAsClient
    public void shouldCreateTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTask("this is task");
        todo.setDescription("this is description");

        String todoJsonString = om.writeValueAsString(todo);

        final Response response = target
                .path("todos")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(todoJsonString));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    @InSequence(2)
    @RunAsClient
    public void shouldGetAllTodos() throws Exception {
        Response response = target.path("todos")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        JsonNode json = om.readTree(response.readEntity(String.class));

        Assert.assertTrue(json.size() > 0);
    }

    @Test
    @InSequence(3)
    @RunAsClient
    public void shouldGetByTodoId() throws Exception {
        Response response = target.path("todos")
                .path("1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        JsonNode json = om.readTree(response.readEntity(String.class));

        assertNotNull("Response contains id ", json.get("id").asLong());
        assertEquals(1L, json.get("id").asLong());
    }

    @Test
    @InSequence(4)
    @RunAsClient
    public void shouldPut() throws Exception {

        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTask("this is updated task");
        todo.setDescription("this is updated description");

        String todoUpdateString = om.writeValueAsString(todo);

        Response response = target.path("todos")
                .path("1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(todoUpdateString));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    @InSequence(5)
    @RunAsClient
    public void shouldDelete() throws Exception {
        Response response = target.path("todos")
                .path("1")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

}
