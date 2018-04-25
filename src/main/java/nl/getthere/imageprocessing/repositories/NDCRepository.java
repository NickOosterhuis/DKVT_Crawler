package nl.getthere.imageprocessing.repositories;

import nl.getthere.imageprocessing.models.NDCModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface NDCRepository extends JpaRepository<NDCModel, Long> {
    NDCModel findByMaterialId(long id);
}
