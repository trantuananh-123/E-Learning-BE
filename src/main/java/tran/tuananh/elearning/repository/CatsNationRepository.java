package tran.tuananh.elearning.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tran.tuananh.elearning.entity.CatsNation;

@Repository
public interface CatsNationRepository extends JpaRepository<CatsNation, Integer> {

    @Query(value = "SELECT * FROM cats_nation c " +
            "WHERE(:id IS NULL OR c.id = :id)", nativeQuery = true,
            countQuery = "SELECT COUNT(c.id) FROM cats_nation c " +
                    "WHERE(:id IS NULL OR c.id = :id)")
    Page<CatsNation> search(Integer id, Pageable pageable);

}
