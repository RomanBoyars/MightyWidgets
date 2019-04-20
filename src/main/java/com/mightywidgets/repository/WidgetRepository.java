package com.mightywidgets.repository;

import com.mightywidgets.Widget;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public final class WidgetRepository extends InMemoryRepository<Widget, Long> {
    private AtomicLong primaryKeyGenerator = new AtomicLong();

    @Override
    public Long generatePrimaryKey() {
        while (findById(primaryKeyGenerator.get()).isPresent())
            primaryKeyGenerator.incrementAndGet();
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
