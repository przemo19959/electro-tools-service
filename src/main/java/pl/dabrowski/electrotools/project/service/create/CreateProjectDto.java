package pl.dabrowski.electrotools.project.service.create;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateProjectDto {
  private String name;
}
