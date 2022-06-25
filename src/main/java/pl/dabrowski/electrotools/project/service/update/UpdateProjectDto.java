package pl.dabrowski.electrotools.project.service.update;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UpdateProjectDto {
  private String name;
  private String owner;
}
