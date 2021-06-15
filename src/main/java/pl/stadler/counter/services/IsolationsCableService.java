package pl.stadler.counter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.stadler.counter.models.IsolationsCable;
import pl.stadler.counter.repositories.IsolationsCableRepository;

import java.util.List;

@Service
public class IsolationsCableService {


    private final IsolationsCableRepository isolationsCableRepository;

    @Autowired
    public IsolationsCableService(IsolationsCableRepository isolationsCableRepository){
        this.isolationsCableRepository=isolationsCableRepository;
    }

    public List<IsolationsCable> findAll() {
        return isolationsCableRepository.findAll();
    }
    public IsolationsCable findByTypeIsolationsAndPrzekrojWew(String typeIsolations, String przekrojWew) {
        return isolationsCableRepository.findByTypeIsolationsAndPrzekrojWew(typeIsolations, przekrojWew).orElse(null);
    }
    public IsolationsCable findByTypeIsolations(String typeIsolations) {
        return isolationsCableRepository.findByTypeIsolations(typeIsolations);
    }
    public IsolationsCable save(IsolationsCable isolationsCable){
        return isolationsCableRepository.save(isolationsCable);
    }

}
