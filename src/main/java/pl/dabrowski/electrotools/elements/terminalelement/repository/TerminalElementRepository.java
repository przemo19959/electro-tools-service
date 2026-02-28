package pl.dabrowski.electrotools.elements.terminalelement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dabrowski.electrotools.elements.terminalelement.TerminalElement;

import java.util.List;
import java.util.UUID;

@Repository
public interface TerminalElementRepository extends JpaRepository<TerminalElement, UUID> {
  List<TerminalElement> findAllByProjectId(UUID projectId);
  void deleteByProjectIdIn(List<UUID> projectIds);
}
