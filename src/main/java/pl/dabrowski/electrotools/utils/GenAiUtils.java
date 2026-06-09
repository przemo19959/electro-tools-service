package pl.dabrowski.electrotools.utils;

import com.google.genai.types.Content;
import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionResponse;
import com.google.genai.types.Part;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

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
}
