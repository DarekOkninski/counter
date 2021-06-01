package pl.stadler.counter.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.stadler.counter.models.Project;
import pl.stadler.counter.services.ProjectService;

import java.util.List;

@RestController
@RequestMapping(path = "/project")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


/////////////////////////////////////////////////////////////////////////////////////
// lista projektów
/////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/show")
    public List<Project> findAll() {
        return projectService.findAll();
    }

/////////////////////////////////////////////////////////////////////////////////////
// szukanie projektu po id
/////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = "/id/{id}")
    public Project findById(@PathVariable(value = "id") Long id) {
        return projectService.findById(id);
    }

/////////////////////////////////////////////////////////////////////////////////////
// szukanie projektu po numerze
/////////////////////////////////////////////////////////////////////////////////////

    @GetMapping(path = "/number/{number}")
    public Project findByNumber(@PathVariable(value = "number") String number) {
        return projectService.findByNumberProject(number);
    }


/////////////////////////////////////////////////////////////////////////////////////
// dodanie nowego projektu
/////////////////////////////////////////////////////////////////////////////////////

    @PostMapping(path = "/save")
    public Project save(@RequestBody Project project) {
        return projectService.save(project);
    }

/////////////////////////////////////////////////////////////////////////////////////
// usunięcie projektu
/////////////////////////////////////////////////////////////////////////////////////

    @PostMapping(path = "/delete/{id}")
    public Project delete(@PathVariable(value = "id", required = true) Long id) {
        Project project = projectService.delete(id);
        if (project != null) {
            System.out.println("-----------------------------------------------------");
            System.out.println("Projekt został skasowany. " + project.getNumberProject());
            System.out.println("-----------------------------------------------------");
            return project;
        }
        return null;
    }
}

