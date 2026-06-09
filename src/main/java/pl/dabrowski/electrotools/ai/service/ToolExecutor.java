package pl.dabrowski.electrotools.ai.service;

import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;

public interface ToolExecutor {
    FunctionResponse execute(FunctionCall call);
}
