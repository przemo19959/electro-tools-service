package pl.dabrowski.electrotools.elements.overcurrentprotection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dabrowski.electrotools.elements.overcurrentprotection.OvercurrentProtectionElement;

import java.util.List;
import java.util.UUID;

@Repository
public interface OvercurrentProtectionElementRepository extends JpaRepository<OvercurrentProtectionElement, UUID> {
  List<OvercurrentProtectionElement> findAllByProjectId(UUID projectId);
}
