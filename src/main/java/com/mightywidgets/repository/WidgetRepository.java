package com.mightywidgets.repository;

import com.mightywidgets.CanvasArea;
import com.mightywidgets.Widget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public final class WidgetRepository extends InMemoryRepository<Widget, Long> {
    private AtomicLong primaryKeyGenerator = new AtomicLong();

    @Override
    public Long generatePrimaryKey() {
        while (findById(primaryKeyGenerator.get()).isPresent()) {
            primaryKeyGenerator.incrementAndGet();
        }
        return primaryKeyGenerator.get();
    }

    @Override
    public <S extends Widget> S save(S widget) {

        if (widget.getzIndex() == null) {
            widget.setzIndex(getMaxZIndex() + 1);
        } else {
            moveZIndexes(widget);
        }

        return super.save(widget);
    }

    public Page<Widget> findAll(CanvasArea canvasArea, Pageable pageable) {
        List<Widget> widgets = findAll().stream().filter(e ->
                e.getX() > canvasArea.getX() &&
                        e.getY() > canvasArea.getY() &&
                        e.getX() + e.getWidth() < canvasArea.getX() + canvasArea.getWidth() &&
                        e.getY() + e.getHeight() < canvasArea.getY() + canvasArea.getHeight()).collect(Collectors.toList());
        widgets = sort(widgets, pageable.getSort());
        return paginate(widgets, pageable);
    }

    private <S extends Widget> void moveZIndexes(S widget) {
        List<Widget> widgets = findAll();
        if (widget.getId() == null || !findById(widget.getId()).isPresent() ||
                widgets.stream()
                        .anyMatch(e -> e.getId().equals(widget.getId()) && !e.getzIndex().equals(widget.getzIndex()))) {
            widgets.stream()
                    .filter(e -> !e.getId().equals(widget.getId()) && e.getzIndex() >= widget.getzIndex())
                    .forEach(Widget::incrementZIndex);
        }
    }

    private Integer getMaxZIndex() {
        List<Widget> widgets = findAll();
        return widgets.stream().max(Comparator.comparing(Widget::getzIndex)).get().getzIndex();
    }
}
