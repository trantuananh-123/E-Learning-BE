package tran.tuananh.elearning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tran.tuananh.elearning.entity.SysUser;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, String> {

    @Query(value = "SELECT * FROM sys_user WHERE username = :username OR email = :email LIMIT 1", nativeQuery = true)
    SysUser findByUsernameEqualsOrEmailEquals(String username, String email);
}
