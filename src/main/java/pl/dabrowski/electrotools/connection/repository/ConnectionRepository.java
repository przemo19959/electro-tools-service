package pl.dabrowski.electrotools.connection.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.dabrowski.electrotools.connection.Connection;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {
  @Query(nativeQuery = true, value = "select * from t_connections tc " +
      "where cast(tc.from_element_id as varchar ) in (:elementIds) " +
      "or cast(tc.to_element_id as varchar ) in (:elementIds)")
  List<Connection> findAllByElementIdsIn(List<String> elementIds);
}
