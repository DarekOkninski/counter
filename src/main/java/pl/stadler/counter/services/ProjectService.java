package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.models.Project;
import pl.stadler.counter.repositories.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findByNumberProjectAndChangeProject(String numberProject, String changeProject){
        return projectRepository.findByNumberProjectAndChangeProject(numberProject, changeProject).orElse(null);
    }

    public Project save(Project project){
        return projectRepository.save(project);
    }



}
