package pl.dabrowski.electrotools.ai.event;

public record ToolCallEvent(String tool, Object args) implements AgentEvent {
}
