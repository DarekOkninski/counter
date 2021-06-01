package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.models.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository   extends JpaRepository<Project, Long> {

    List<Project> findAllByOrderByNumberProjectAsc();
    Optional<Project> findByNumberProjectAndChangeProject(String numberProject, String changeProject);
    Optional<Project> findByNumberProject(String numberProject);
    Optional<Project> findById(Long id);

}
