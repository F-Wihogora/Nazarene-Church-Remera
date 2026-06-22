package com.ncrde.church.repository;

import com.ncrde.church.entity.Sermon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SermonRepository extends JpaRepository<Sermon, Long> {
    List<Sermon> findByTitleContainingIgnoreCaseOrPreacherContainingIgnoreCaseOrScriptureContainingIgnoreCase(
            String title, String preacher, String scripture);
}
