package com.ncrde.church.repository;

import com.ncrde.church.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByCategory(String category);
    List<Song> findByTitleContainingIgnoreCase(String title);
}
