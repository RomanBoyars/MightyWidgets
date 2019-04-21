package com.mightywidgets.controller;

import com.mightywidgets.CanvasArea;
import com.mightywidgets.Widget;
import com.mightywidgets.error.EntityNotFoundException;
import com.mightywidgets.error.WidgetNotFoundException;
import com.mightywidgets.repository.WidgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/test/widgets")
public class WidgetController {

    Logger log = LoggerFactory.getLogger(WidgetController.class);
    private WidgetRepository widgetRepository;

    @Autowired
    public WidgetController(WidgetRepository widgetRepository) {
        this.widgetRepository = widgetRepository;
    }

    @GetMapping
    public ResponseEntity<Object> findAll(CanvasArea canvasArea, Pageable pageRequest) {
        Page<Widget> currentPage;

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        if (validator.validate(canvasArea).size() == 0)
            currentPage = widgetRepository.findAll(canvasArea, pageRequest);
        else
            currentPage = widgetRepository.findAll(pageRequest);

        Map<String, Object> message = new LinkedHashMap<>();

        message.put("page", currentPage.getNumber());
        message.put("pageSize", currentPage.getSize());
        message.put("totalPages", currentPage.getTotalPages());
        message.put("displaying", currentPage.getNumberOfElements());
        message.put("totalElements", currentPage.getTotalElements());
        message.put("widgets", currentPage.getContent());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Widget> findById(@PathVariable Long id) {
        Optional<Widget> widget = widgetRepository.findById(id);

        if (!widget.isPresent()) {
            throw new WidgetNotFoundException(id);
        }
        return new ResponseEntity<>(widget.get(), HttpStatus.OK);
    }


    @PostMapping
    public Widget create(@Valid @RequestBody Widget widget) {
        return widgetRepository.save(widget);
    }


    @PutMapping("/update/{id}")
    public Widget update(@PathVariable Long id, @Valid @RequestBody Widget updatingWidget) {
        Optional<Widget> widgetOptional = widgetRepository.findById(id);

        if (!widgetOptional.isPresent()) {
            throw new WidgetNotFoundException(id);
        }
        Widget widget = widgetOptional.get();

        if (updatingWidget.getX() != null) widget.setX(updatingWidget.getX());
        if (updatingWidget.getY() != null) widget.setY(updatingWidget.getY());
        if (updatingWidget.getHeight() != null) widget.setHeight(updatingWidget.getHeight());
        if (updatingWidget.getWidth() != null) widget.setWidth(updatingWidget.getWidth());
        if (updatingWidget.getZIndex() != null) widget.setzIndex(updatingWidget.getZIndex());

        return widgetRepository.save(widget);
    }


    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            widgetRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new WidgetNotFoundException(id);
        }
        return new ResponseEntity<>("Deleted widget " + id, HttpStatus.OK);
    }


}
