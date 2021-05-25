package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.Project;

@Repository
public interface ProjectRepository   extends JpaRepository<Project, Long> {
}
