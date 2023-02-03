package tran.tuananh.elearning.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tran.tuananh.elearning.entity.CatsCategory;

import java.util.List;

@Repository
public interface CatsCategoryRepository extends JpaRepository<CatsCategory, Integer> {

    @Query(value = "WITH RECURSIVE category_tree(id, name, image_url, parent_id, create_date, update_date, is_active, is_delete) AS (" +
            "SELECT c1.* " +
            "FROM cats_category c1 " +
            "WHERE (:id IS NULL AND c1.parent_id IS NULL) OR (c1.id = :id) AND c1.is_active = TRUE " +
            "UNION ALL " +
            "SELECT c2.* " +
            "FROM cats_category c2 " +
            "JOIN category_tree d ON c2.parent_id = d.id " +
            "WHERE c2.is_active = TRUE) " +
            "SELECT * FROM category_tree", nativeQuery = true,
            countQuery = "WITH RECURSIVE category_tree(id, name, image_url, parent_id, create_date, update_date, is_active, is_delete) AS (" +
                    "SELECT c1.* " +
                    "FROM cats_category c1 " +
                    "WHERE (:id IS NULL AND c1.parent_id IS NULL) OR (c1.id = :id) AND c1.is_active = TRUE " +
                    "UNION ALL " +
                    "SELECT c2.* " +
                    "FROM cats_category c2 " +
                    "JOIN category_tree d ON c2.parent_id = d.id " +
                    "WHERE c2.is_active = TRUE) " +
                    "SELECT COUNT(category_tree.id) FROM category_tree")
    Page<CatsCategory> search(Integer id, Pageable pageable);

    @Query(value = "WITH RECURSIVE category_tree(id, name, image_url, parent_id, create_date, update_date, is_active, is_delete) AS (" +
            "SELECT c1.* " +
            "FROM cats_category c1 " +
            "WHERE (:id IS NULL AND c1.parent_id IS NULL) OR (c1.id = :id) AND c1.is_active = TRUE " +
            "UNION ALL " +
            "SELECT c2.* " +
            "FROM cats_category c2 " +
            "JOIN category_tree d ON c2.parent_id = d.id " +
            "WHERE c2.is_active = TRUE) " +
            "SELECT * FROM category_tree", nativeQuery = true,
            countQuery = "WITH RECURSIVE category_tree(id, name, image_url, parent_id, create_date, update_date, is_active, is_delete) AS (" +
                    "SELECT c1.* " +
                    "FROM cats_category c1 " +
                    "WHERE (:id IS NULL AND c1.parent_id IS NULL) OR (c1.id = :id) AND c1.is_active = TRUE " +
                    "UNION ALL " +
                    "SELECT c2.* " +
                    "FROM cats_category c2 " +
                    "JOIN category_tree d ON c2.parent_id = d.id " +
                    "WHERE c2.is_active = TRUE) " +
                    "SELECT COUNT(category_tree.id) FROM category_tree")
    List<CatsCategory> searchWithoutPage(Integer id);
}
