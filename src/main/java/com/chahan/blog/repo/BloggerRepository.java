package com.chahan.blog.repo;

import com.chahan.blog.model.Blogger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloggerRepository extends JpaRepository<Blogger, Long> {

    Blogger getByUsername(String username);
}
