package pl.dabrowski.electrotools.ai.service;

import com.google.genai.types.Content;

import java.util.List;

public record AgentState(
        List<Content> contents,
        int round
) {
}
