package net.snortum.scrabblewords.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import net.snortum.scrabblewords.controller.WordSearcher;

/**
 * Custom event to hold the progress thus far in the {@link WordSearcher} task
 *
 * @author Knute Snortum
 * @version 2.7.1
 */
public class ProgressEvent extends Event {
    private final Object source;
    private final EventTarget target;
    private final EventType<? extends Event> eventType;
    private final double thusFar;

    public ProgressEvent(Object source, EventTarget target, EventType<? extends Event> eventType, double thusFar) {
        super(source, target, eventType);
        this.source = source;
        this.target = target;
        this.eventType = eventType;
        this.thusFar = thusFar;
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public EventTarget getTarget() {
        return target;
    }

    @Override
    public EventType<? extends Event> getEventType() {
        return eventType;
    }

    public double getThusFar() {
        return thusFar;
    }

}
