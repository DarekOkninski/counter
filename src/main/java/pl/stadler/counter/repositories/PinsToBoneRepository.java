package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.BoneHarting;
import pl.stadler.counter.models.PinsToBone;

import java.util.List;

@Repository
public interface PinsToBoneRepository extends JpaRepository<PinsToBone, Long> {

    public List<PinsToBone> findAll();
    public List<PinsToBone> findAllByBoneHartingId(Long id);

}
