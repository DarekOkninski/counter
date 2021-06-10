package pl.stadler.counter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.stadler.counter.models.KabelList;

import java.util.List;

@Repository
public interface KabelListRepository  extends JpaRepository<KabelList, Long> {

    public List<KabelList> findAll();

    public List<KabelList> findAllByStrang(String strang);

    public List<KabelList> findAllByStrangAndPositionFromAndPositionTo(String strang, String positionFrom, String positionTo);

    public List<KabelList> findAllByPositionFromAndPinFromAndPositionToAndPinTo(String positionFrom, String pinFrom, String positionTo, String pinTo);

    @Query("SELECT strang, positionFrom, positionTo FROM KabelList group by strang, positionFrom, positionTo")
    public List<Object[]> mesh();

    @Query("SELECT  positionFrom, pinFrom, positionTo, pinTo FROM KabelList where positionFrom not like positionTo and positionFrom not like '' and positionTo  not like '' and pinFrom not like '%sh%' and pinTo  not like '%sh%' group by positionFrom, pinFrom, positionTo, pinTo")
    public List<Object[]> groupE3();


}
