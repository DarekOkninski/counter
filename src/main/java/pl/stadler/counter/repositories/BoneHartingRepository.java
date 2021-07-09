package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.AparatList;
import pl.stadler.counter.models.BoneHarting;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoneHartingRepository extends JpaRepository<BoneHarting, Long> {
    public List<BoneHarting> findAll();
    public Optional<BoneHarting> findByNumberProducer(String numberProducer);
}
