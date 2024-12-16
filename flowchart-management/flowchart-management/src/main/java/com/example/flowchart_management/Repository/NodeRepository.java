package com.example.flowchart_management.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.flowchart_management.entity.Node;

public interface NodeRepository extends JpaRepository<Node, Long> {
}
