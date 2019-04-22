package com.mightywidgets.error;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.JsonPath;
import com.mightywidgets.AbstractTest;
import com.mightywidgets.Widget;
import com.mightywidgets.controller.WidgetController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(WidgetController.class)
public class ErrorsTest extends AbstractTest {

    @Test
    public void checkWidgetNotFound() throws Exception {
        int id = 100;
        String uri = "/api/test/widgets/" + id;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        String textStatus = JsonPath.parse(content).read("$.status");
        String message = JsonPath.parse(content).read("$.message");

        assertEquals("NOT_FOUND", textStatus);
        assertEquals("Could not find widget " + id, message);
        assertEquals(404, status);
    }

    @Test
    public void checkMethodArgumentMissmatch() throws Exception {
        String id = "WRONG_ID";
        String uri = "/api/test/widgets/" + id;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        String textStatus = JsonPath.parse(content).read("$.status");
        String message = JsonPath.parse(content).read("$.message");

        assertEquals("BAD_REQUEST", textStatus);
        assertEquals("id should be of type java.lang.Long", message);
        assertEquals(400, status);
    }

    @Test
    public void checkMessageNotReadable() throws Exception {
        String malformedJson = "{ aaaaa }";
        String uri = "/api/test/widgets/";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(malformedJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        String textStatus = JsonPath.parse(content).read("$.status");
        String message = JsonPath.parse(content).read("$.message");

        assertEquals("BAD_REQUEST", textStatus);
        assertEquals("Malformed JSON. Values does not match their types or they are bigger than JSON format allows.", message);
        assertEquals(400, status);
    }

    @Test
    public void checkMethodArgumentNotValid() throws Exception {
        String uri = "/api/test/widgets/";
        Widget inputWidget = new Widget(null, 4444444, 5555555, 40, 40, null);
        String inputJson = mapper.writeValueAsString(inputWidget);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        String textStatus = JsonPath.parse(content).read("$.status");
        String message = JsonPath.parse(content).read("$.message");
        List<FieldValidationError> errorList = mapper.readValue(JsonPath.read(content, "$.innerErrors").toString(),
                new TypeReference<List<FieldValidationError>>() {});

        assertEquals("BAD_REQUEST", textStatus);
        assertEquals("Validation error", message);
        assertEquals(2, errorList.size());
        assertEquals(400, status);

    }

}
