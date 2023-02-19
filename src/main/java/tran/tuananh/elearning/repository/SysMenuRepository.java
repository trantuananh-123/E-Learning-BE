package tran.tuananh.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tran.tuananh.elearning.entity.SysMenu;

import java.util.List;

@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, String> {

    List<SysMenu> findAllByIsActiveIsTrueAndIsDeleteIsFalse();
}
