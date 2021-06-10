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
        return projectRepository.findAllByOrderByNumberProjectAsc();
    }
    public Project findById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

//    public Project findByNumberProjectAndChangeProject(String numberProject, String changeProject){
//        return projectRepository.findByNumberProjectAndChangeProject(numberProject, changeProject).orElse(null);
//    }
    public Project findByNumberProject(String numberProject){
        return projectRepository.findByNumberProject(numberProject).orElse(null);
    }

    public Project save(Project project) {
        // szukanie w bazie danych projektu o tym samym numerze
        Optional<Project> projectTemp = projectRepository.findByNumberProject(project.getNumberProject());

        // jeśli znaleziono projekt o takim samym numerze, ale innym id - błąd
        if (projectTemp.isPresent() && !projectTemp.get().getId().equals(project.getId())) {
            System.out.println("---------------------------------");
            System.out.println("Projekt o podanym numerze istnieje w bazie danych.");
            System.out.println("---------------------------------");
            return null;
        }

        return projectRepository.save(project);
    }


    public Project delete(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        project.ifPresent(projectRepository::delete);


        return project.orElse(null);
    }





}
