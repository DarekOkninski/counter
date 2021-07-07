package pl.stadler.counter.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.stadler.counter.controler.KabelListController;
import pl.stadler.counter.models.KabelList;
import pl.stadler.counter.models.Mesh;
import pl.stadler.counter.repositories.KabelListRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XEMVService {

    private final KabelListRepository kabelListRepository;
    Logger loggerXEMVService = LoggerFactory.getLogger(XEMVService.class);

    public XEMVService(KabelListRepository kabelListRepository) {
        this.kabelListRepository = kabelListRepository;
    }


    /**
     * Metoda do wyliczenia Xemv
     *
     * @param args Unused.
     * @return Map<String, Integer>
     * @throws NullPointerException
     * @see NullPointerException
     */

    public Map<String, Integer> XEMVCount(){
        Map<String, Integer> finalScore = new HashMap<>();
        List<KabelList> kabelLists = kabelListRepository.findAll();
        int count = 0;
        try {
            //Zliczenie wszystkich wystąpień XEMV w kabelliscie
            for (KabelList kabelList : kabelLists) {
                if (kabelList.getPositionFrom().toUpperCase().contains("XEMV") || kabelList.getPositionTo().toUpperCase().contains("XEMV")) {
                    count++;
                }
            }
            finalScore.put("EMC-Clip - 12156014", count);
            finalScore.put("Rastelement Hutschienenrastung - 12014714", (count / 10));

        } catch (NullPointerException n) {
            loggerXEMVService.error("XEMVService.XEMVCount() - Błąd " + n);
        }
        return finalScore;
    }
}
