package com.mightywidgets;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CanvasArea {
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

    public CanvasArea(Integer x, Integer y, Integer height, Integer width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CanvasArea)) return false;
        CanvasArea that = (CanvasArea) o;
        return x.equals(that.x) &&
                y.equals(that.y) &&
                height.equals(that.height) &&
                width.equals(that.width);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, height, width);
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
