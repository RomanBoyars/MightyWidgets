/*

Created 18.04.2019

 */

package com.mightywidgets.repository;

import com.mightywidgets.CanvasArea;
import com.mightywidgets.Widget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Represents a repository of Widgets
 */
public class WidgetRepository extends InMemoryRepository<Widget, Long> {
    private AtomicLong primaryKeyGenerator = new AtomicLong();

    @Override
    public Long generatePrimaryKey() {
        while (findById(primaryKeyGenerator.get()).isPresent()) {
            primaryKeyGenerator.incrementAndGet();
        }
        return primaryKeyGenerator.get();
    }

    /**
     * {@inheritDoc}
     * Save {@link Widget} and handle z-Indexes
     *
     * @param widget the {@link Widget} object
     * @param <S>
     * @return saved {@link Widget}
     */
    @Override
    public <S extends Widget> S save(S widget) {

        if (widget.getZIndex() == null) {
            widget.setzIndex(getMaxZIndex() + 1);
        } else {
            moveZIndexes(widget);
        }

        return super.save(widget);
    }

    /**
     * Finds all widgets within specific {@link CanvasArea}
     * Supports pagination and sorting.
     *
     * @param canvasArea the {@link CanvasArea} object
     * @param pageable   the {@link Pageable} object
     * @return {@link Page} with found widgets
     */
    public Page<Widget> findAll(CanvasArea canvasArea, Pageable pageable) {
        List<Widget> widgets = findAll().stream().filter(e ->
                e.getX() > canvasArea.getX() &&
                        e.getY() > canvasArea.getY() &&
                        e.getX() + e.getWidth() < canvasArea.getX() + canvasArea.getWidth() &&
                        e.getY() + e.getHeight() < canvasArea.getY() + canvasArea.getHeight()).collect(Collectors.toList());
        widgets = sort(widgets, pageable.getSort());
        return paginate(widgets, pageable);
    }

    /**
     * Moves z-Indexes
     *
     * @param widget the {@link Widget} object
     * @param <S>
     * @return saved {@link Widget}
     */
    private <S extends Widget> void moveZIndexes(S widget) {
        List<Widget> widgets = findAll();
        if (widget.getId() == null || !findById(widget.getId()).isPresent() ||
                widgets.stream()
                        .anyMatch(e -> e.getId().equals(widget.getId()) && !e.getZIndex().equals(widget.getZIndex()))) {
            widgets.stream()
                    .filter(e -> !e.getId().equals(widget.getId()) && e.getZIndex() >= widget.getZIndex())
                    .forEach(Widget::incrementZIndex);
        }
    }

    /**
     * Get max z-Index among all Widgets
     *
     * @return {@link Integer} max z-Index
     */
    private Integer getMaxZIndex() {
        List<Widget> widgets = findAll();
        Optional<Widget> maxZIndexWidget = widgets.stream().max(Comparator.comparing(Widget::getZIndex));
        if (maxZIndexWidget.isPresent()) {
            return maxZIndexWidget.get().getZIndex();
        } else {
            return 0;
        }
    }
}
