/*
  Widget class
  <p>
  Created 16.04.2019
 */

package com.mightywidgets;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Objects;

/**
 * Represents a whiteboard widget.
 */
public class Widget extends CanvasArea {
    @Id
    private Long id;
    private Integer zIndex;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy hh:mm:ss")
    private Date modified;

    /**
     * Constructor with parameters for creating a Widget instance
     *
     * @param id     id of the widget
     * @param x      x position of the widget on canvas. Must not be null or empty.
     * @param y      y position of the widget on canvas. Must not be null or empty.
     * @param height Widget height in pixels. Must not be null or empty, or lesser than 0.
     * @param width  Widget width in pixels. Must not be null or empty, or lesser than 0.
     * @param zIndex Z-Index of widget (the higher the value, the higher the widget lies)
     */
    public Widget(@JsonProperty("id") Long id,
                  @JsonProperty("x") Integer x,
                  @JsonProperty("y") Integer y,
                  @JsonProperty("height") Integer height,
                  @JsonProperty("width") Integer width,
                  @JsonProperty("zindex") Integer zIndex) {
        super(x, y, height, width);
        this.id = id;
        this.zIndex = zIndex;
        this.modified = new Date();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    @Override
    public void setX(Integer x) {
        super.setX(x);
        this.setModified(new Date());
    }

    @Override
    public void setY(Integer y) {
        super.setY(y);
        this.setModified(new Date());
    }

    @Override
    public void setHeight(Integer height) {
        super.setHeight(height);
        this.setModified(new Date());
    }

    @Override
    public void setWidth(Integer width) {
        super.setWidth(width);
        this.setModified(new Date());
    }

    public Integer getZIndex() {
        return zIndex;
    }

    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
        this.setModified(new Date());
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void incrementZIndex() {
        this.zIndex++;
    }
}
