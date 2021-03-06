package com.mw.portfolio.user.repository;

import com.mw.portfolio.security.model.PrincipalRole;
import com.mw.portfolio.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUid(String uid);

  boolean existsByUid(String uid);

  @Query("select user.role from User user where user.uid = :uid")
  Optional<PrincipalRole> getUserRole(String uid);

  User findFirstByRole(PrincipalRole role);

  Optional<User> findByUsername(String username);

  @Modifying
  @Query("update User user set user.email = :email where user.uid = :uid")
  void updateEmail(String uid, String email);
}
