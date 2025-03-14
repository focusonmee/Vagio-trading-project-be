package com.example.repository;

import com.example.entity.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList, Long> {


    Optional<WatchList> findByUser_Id(Long userId);
}
