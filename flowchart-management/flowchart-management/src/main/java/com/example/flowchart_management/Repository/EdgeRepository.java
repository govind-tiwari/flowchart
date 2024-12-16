package com.example.flowchart_management.Repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.flowchart_management.entity.Edge;
import com.example.flowchart_management.entity.Node;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
    List<Edge> findBySource(Node source);
}

