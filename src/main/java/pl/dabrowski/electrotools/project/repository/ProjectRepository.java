package pl.dabrowski.electrotools.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dabrowski.electrotools.project.Project;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
  boolean existsByName(String name);

  boolean existsByIdNotAndName(UUID id, String name);
}
