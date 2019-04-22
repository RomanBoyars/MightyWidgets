package com.mightywidgets.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.JsonPath;
import com.mightywidgets.AbstractTest;
import com.mightywidgets.Widget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(WidgetController.class)
public class WidgetControllerTest extends AbstractTest {

    private List<Widget> getWidgetsFromJson(String jsonString) throws IOException {
        return mapper.readValue(jsonString, new TypeReference<List<Widget>>() {
        });
    }

    private Widget getWidgetFromJson(String jsonString) throws IOException {
        return mapper.readValue(jsonString, Widget.class);
    }

    @Test
    public void checkFindAll() throws Exception {
        String uri = "/api/test/widgets";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        int page = JsonPath.parse(content).read("$.page");
        int displaying = JsonPath.parse(content).read("$.displaying");
        int totalElements = JsonPath.parse(content).read("$.totalElements");
        List<Widget> widgets = getWidgetsFromJson(
                JsonPath.read(content, "$.widgets").toString());

        assertEquals(3, widgets.size());
        assertEquals(widgets.size(), totalElements);
        assertEquals(0, page);
        assertEquals(totalElements, displaying);

        assertEquals(0, widgets.get(0).getId().intValue());
        assertEquals(1, widgets.get(1).getId().intValue());
        assertEquals(2, widgets.get(2).getId().intValue());

        assertEquals(0, widgets.get(0).getX().intValue());
        assertEquals(1, widgets.get(1).getX().intValue());
        assertEquals(2, widgets.get(2).getX().intValue());

        assertEquals(10, widgets.get(0).getHeight().intValue());
        assertEquals(20, widgets.get(1).getHeight().intValue());
        assertEquals(30, widgets.get(2).getHeight().intValue());

        assertEquals(10, widgets.get(0).getWidth().intValue());
        assertEquals(20, widgets.get(1).getWidth().intValue());
        assertEquals(30, widgets.get(2).getWidth().intValue());

        assertEquals(1, widgets.get(0).getZIndex().intValue());
        assertEquals(2, widgets.get(1).getZIndex().intValue());
        assertEquals(3, widgets.get(2).getZIndex().intValue());

        assertEquals(200, status);
    }

    @Test
    public void checkGetById() throws Exception {
        int id = 1;
        String uri = "/api/test/widgets/" + id;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Widget returnedWidget = getWidgetFromJson(content);
        Widget repositoryWidget = widgetRepository.findById(returnedWidget.getId()).get();

        assertEquals(id, returnedWidget.getId().intValue());
        assertEquals(1, returnedWidget.getX().intValue());
        assertEquals(1, returnedWidget.getY().intValue());
        assertEquals(20, returnedWidget.getHeight().intValue());
        assertEquals(20, returnedWidget.getWidth().intValue());
        assertEquals(2, returnedWidget.getZIndex().intValue());
        assertEquals(200, status);
    }

    @Test
    public void checkAddNew() throws Exception {
        String uri = "/api/test/widgets/";
        Widget inputWidget = new Widget(null, 3, 3, 40, 40, null);
        String inputJson = mapper.writeValueAsString(inputWidget);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Widget returnedWidget = getWidgetFromJson(content);
        Widget repositoryWidget = widgetRepository.findById(returnedWidget.getId()).get();

        assertEquals(4, widgetRepository.count());
        assertTrue(returnedWidget.getId().intValue() == 3
                && repositoryWidget.getId() == 3);
        assertTrue(returnedWidget.getX().equals(inputWidget.getX())
                && repositoryWidget.getX().equals(inputWidget.getX()));
        assertTrue(returnedWidget.getY().equals(inputWidget.getY())
                && repositoryWidget.getY().equals(inputWidget.getY()));
        assertTrue(returnedWidget.getHeight().equals(inputWidget.getHeight())
                && repositoryWidget.getHeight().equals(inputWidget.getHeight()));
        assertTrue(returnedWidget.getWidth().equals(inputWidget.getHeight())
                && repositoryWidget.getWidth().equals(inputWidget.getHeight()));
        assertTrue(returnedWidget.getZIndex() == 4
                && repositoryWidget.getZIndex() == 4);
        assertEquals(200, status);
    }

    @Test
    public void checkEdit() throws Exception {
        Long id = 2L;
        String uri = "/api/test/widgets/update/" + id;
        Widget inputWidget = new Widget(5L, 5, 5, 50, 50, 5);
        String inputJson = mapper.writeValueAsString(inputWidget);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();
        Widget returnedWidget = getWidgetFromJson(content);
        Widget repositoryWidget = widgetRepository.findById(returnedWidget.getId()).get();

        assertTrue(returnedWidget.getId().equals(id)
                && repositoryWidget.getId().equals(id));
        assertTrue(returnedWidget.getX().equals(inputWidget.getX())
                && repositoryWidget.getX().equals(inputWidget.getX()));
        assertTrue(returnedWidget.getY().equals(inputWidget.getY())
                && repositoryWidget.getY().equals(inputWidget.getY()));
        assertTrue(returnedWidget.getHeight().equals(inputWidget.getHeight())
                && repositoryWidget.getHeight().equals(inputWidget.getHeight()));
        assertTrue(returnedWidget.getWidth().equals(inputWidget.getHeight())
                && repositoryWidget.getWidth().equals(inputWidget.getHeight()));
        assertTrue(returnedWidget.getZIndex() == 5
                && repositoryWidget.getZIndex() == 5);
        assertEquals(200, status);
    }

    @Test
    public void checkDelete() throws Exception {
        Long id = 2L;
        String uri = "/api/test/widgets/" + id;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();
        Optional<Widget> repositoryWidget = widgetRepository.findById(id);

        assertFalse(repositoryWidget.isPresent());
        assertEquals("Deleted widget " + id, content);
        assertEquals(200, status);
    }


}
