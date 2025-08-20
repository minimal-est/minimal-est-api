package kr.minimalest.api.domain;

import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot {

    @Transient
    private final List<DomainEvent> events = new ArrayList<>();

    public void registerEvent(DomainEvent event) {
        events.add(event);
    }

    public List<DomainEvent> releaseEvents() {
        List<DomainEvent> domainEvents = new ArrayList<>(events);
        events.clear();
        return domainEvents;
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }
}
