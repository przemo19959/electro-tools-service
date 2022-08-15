package pl.dabrowski.electrotools.elements.load.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dabrowski.electrotools.elements.load.LoadElement;

import java.util.UUID;

@Repository
public interface LoadElementRepository extends JpaRepository<LoadElement, UUID> {
}
