package pl.dabrowski.electrotools.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dabrowski.electrotools.ai.event.AgentEvent;
import pl.dabrowski.electrotools.ai.service.AiService;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AiController {

    private final AiService aiService;

    @PostMapping
    public String chat(@RequestBody String prompt) {
        return aiService.handle(prompt);
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AgentEvent> chatStream(@RequestBody String prompt) {
        return aiService.handleStream(prompt);
    }
}
