package pl.dabrowski.electrotools.login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
  @GetMapping("/login")
  public ResponseEntity<Void> login() {
    return ResponseEntity.ok().build();
  }
}
