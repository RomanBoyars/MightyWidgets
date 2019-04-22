package com.mightywidgets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mightywidgets.controller.WidgetController;
import com.mightywidgets.error.WidgetErrorAdvice;
import com.mightywidgets.repository.WidgetRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class AbstractTest {

    protected ObjectMapper mapper;
    @Autowired
    protected MockMvc mockMvc;
    protected WidgetRepository widgetRepository;
    protected WidgetController widgetController;

    @Before
    public void setup() {
        this.mapper = new ObjectMapper();
        WidgetRepository widgetRepository = new WidgetRepository();
        widgetRepository.save(new Widget(null, 0, 0, 10, 10, null));
        widgetRepository.save(new Widget(null, 1, 1, 20, 20, null));
        widgetRepository.save(new Widget(null, 2, 2, 30, 30, null));
        this.widgetRepository = widgetRepository;

        this.widgetController = new WidgetController(widgetRepository);

        mockMvc = MockMvcBuilders.standaloneSetup(widgetController)
                .setControllerAdvice(new WidgetErrorAdvice())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((ViewResolver) (viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

}
