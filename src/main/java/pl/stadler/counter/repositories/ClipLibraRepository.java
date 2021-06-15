package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.ClipLibra;
import pl.stadler.counter.models.Distances;
import pl.stadler.counter.models.Mesh;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClipLibraRepository   extends JpaRepository<ClipLibra, Long> {
    public List<ClipLibra> findAll();

    public Optional<ClipLibra> findByClipNumberStadlerID(String clipNumberStadlerID);

    @Query("SELECT m FROM ClipLibra m where m.diameterClipMin <= ?1 and m.diameterClipMax >= ?1")
    public ClipLibra findBySize(float diameterClipSize);
}
