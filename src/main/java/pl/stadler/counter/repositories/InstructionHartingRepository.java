package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.BoneHarting;
import pl.stadler.counter.models.InstructionHarting;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstructionHartingRepository extends JpaRepository<InstructionHarting, Long> {
    public List<InstructionHarting> findAll();
    public void deleteAll();

    public Optional<InstructionHarting> findByNameBone(String nameBone);

}
