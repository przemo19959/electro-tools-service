package pl.dabrowski.electrotools.ai.event;

public record ToolResultEvent(String tool, float duration, Object result) implements AgentEvent {
}
