package pl.dabrowski.electrotools.connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dabrowski.electrotools.connection.Connection;

import java.util.UUID;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
}
