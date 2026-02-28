package pl.dabrowski.electrotools.elements.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dabrowski.electrotools.elements.basic.BasicElement;

import java.util.List;
import java.util.UUID;

@Repository
public interface BasicElementRepository extends JpaRepository<BasicElement, UUID> {
  List<BasicElement> findAllByProjectId(UUID projectId);
  void deleteByProjectIdIn(List<UUID> projectIds);
}
