package tran.tuananh.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tran.tuananh.elearning.entity.SysUser;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, String> {
}
