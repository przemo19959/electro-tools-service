package pl.dabrowski.electrotools.wire.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dabrowski.electrotools.wire.Wire;

import java.util.UUID;

@Repository
public interface WireRepository extends JpaRepository<Wire, UUID> {
}
