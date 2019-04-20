/**
 * Widget class
 * <p>
 * Represents a whiteboard widget.
 * <p>
 * Created 16.04.2019
 */

package com.mightywidgets;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

public class Widget implements Comparable<Widget> {

    @Id
    private Long id;
    @Digits(integer = 3, fraction = 0, message = "X coordinate value is too big. Only 3 digits allowed!")
    @NotNull(message = "X coordinate can not be null!")
    private Integer x;
    @Digits(integer = 3, fraction = 0, message = "Y coordinate value is too big. Only 3 digits allowed!")
    @NotNull(message = "Y coordinate can not be null!")
    private Integer y;
    @Digits(integer = 3, fraction = 0, message = "Height value is too big. Only 3 digits allowed!")
    @Min(value = 0, message = "Height can not be less than 0!")
    @NotNull(message = "Height can not be null!")
    private Integer height;
    @Digits(integer = 3, fraction = 0, message = "Width value is too big. Only 3 digits allowed!")
    @Min(value = 0, message = "Width can not be less than 0!")
    @NotNull(message = "Width can not be null!")
    private Integer width;
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
    public Widget(Long id, Integer x, Integer y, Integer height, Integer width, Integer zIndex) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.zIndex = zIndex;
        this.modified = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Widget)) return false;
        Widget widget = (Widget) o;
        return id.equals(widget.id) &&
                Objects.equals(x, widget.x) &&
                Objects.equals(y, widget.y) &&
                Objects.equals(height, widget.height) &&
                Objects.equals(width, widget.width) &&
                Objects.equals(zIndex, widget.zIndex) &&
                modified.equals(widget.modified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        this.setModified(new Date());
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
        this.setModified(new Date());
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
        this.setModified(new Date());
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
        this.setModified(new Date());
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
        this.setModified(new Date());
    }

    public Integer getzIndex() {
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

    @Override
    public int compareTo(Widget widget) {
        return (int) (this.id - widget.getId());
    }
}
