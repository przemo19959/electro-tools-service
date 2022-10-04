package pl.dabrowski.electrotools.elements.rcdelement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dabrowski.electrotools.elements.rcdelement.RcdElement;

import java.util.List;
import java.util.UUID;

@Repository
public interface RcdElementRepository extends JpaRepository<RcdElement, UUID> {
  List<RcdElement> findAllByProjectId(UUID projectId);
}
