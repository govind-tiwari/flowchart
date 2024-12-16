package com.example.flowchart_management.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.flowchart_management.entity.Flowchart;

public interface FlowchartRepository extends JpaRepository<Flowchart, Long> {
}