package pl.dabrowski.electrotools.utils;

import com.google.genai.types.Content;
import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import com.google.genai.types.Part;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenAiUtils {
    public static Content createModelFunctionCallContent(FunctionCall functionCall) {
        return Content.builder()
                .role("model")
                .parts(List.of(
                        Part.builder()
                                .functionCall(functionCall)
                                .build()
                ))
                .build();
    }

    public static Content createPromptContent(String prompt) {
        return Content.builder()
                .role("user")
                .parts(List.of(
                        Part.builder()
                                .text(prompt)
                                .build()
                ))
                .build();
    }

    public static Content createResponseContent(FunctionResponse functionResponse) {
        return Content.builder()
                .role("user")
                .parts(List.of(
                        Part.builder()
                                .functionResponse(functionResponse)
                                .build()
                ))
                .build();
    }

    public static FunctionResponse createErrorResponse(FunctionCall call, String errorMessage) {
        return FunctionResponse.builder()
                .name(call.name().orElseThrow())
                .response(Map.of(
                        "error",
                        errorMessage
                ))
                .build();
    }

    public static FunctionResponse createSuccessResponse(FunctionCall call, String successMessage) {
        return FunctionResponse.builder()
                .name(call.name().orElseThrow())
                .response(Map.of("result", successMessage))
                .build();
    }
}
