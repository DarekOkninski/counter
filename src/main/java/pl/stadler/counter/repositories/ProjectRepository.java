package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.stadler.counter.models.Project;

public interface ProjectRepository   extends JpaRepository<Project, Long> {
}
