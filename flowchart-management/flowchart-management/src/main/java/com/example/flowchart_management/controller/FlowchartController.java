package com.example.flowchart_management.controller;


import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.flowchart_management.entity.Edge;
import com.example.flowchart_management.entity.Flowchart;
import com.example.flowchart_management.entity.Node;
import com.example.flowchart_management.service.FlowchartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Flowchart Management")
@RestController
@RequestMapping("/api/flowcharts")
public class FlowchartController {

    @Autowired
    private FlowchartService flowchartService;

    @Operation(summary = "Create a new flowchart")
    @PostMapping
    public ResponseEntity<Flowchart> createFlowchart(@RequestBody Flowchart flowchart) {
        Flowchart createdFlowchart = flowchartService.createFlowchart(flowchart);
        return ResponseEntity.ok(createdFlowchart);
    }

    @Operation(summary = "Fetch a flowchart by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Flowchart> getFlowchart(@PathVariable Long id) {
        try {
            Flowchart flowchart = flowchartService.getFlowchart(id);
            return ResponseEntity.ok(flowchart);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update an existing flowchart")
    @PutMapping("/{id}")
    public ResponseEntity<Flowchart> updateFlowchart(@PathVariable Long id, @RequestBody Flowchart flowchart) {
        try {
            Flowchart updatedFlowchart = flowchartService.updateFlowchart(id, flowchart);
            return ResponseEntity.ok(updatedFlowchart);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a flowchart")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlowchart(@PathVariable Long id) {
        try {
            flowchartService.deleteFlowchart(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Validate a flowchart")
    @GetMapping("/{id}/validate")
    public ResponseEntity<Boolean> validateFlowchart(@PathVariable Long id) {
        try {
            Flowchart flowchart = flowchartService.getFlowchart(id);
            return ResponseEntity.ok(flowchartService.validateGraph(flowchart));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get outgoing edges for a node")
    @GetMapping("/nodes/{nodeId}/outgoing-edges")
    public ResponseEntity<List<Edge>> getOutgoingEdges(@PathVariable Long nodeId) {
        try {
            List<Edge> outgoingEdges = flowchartService.getOutgoingEdges(nodeId);
            return ResponseEntity.ok(outgoingEdges);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all connected nodes")
    @GetMapping("/nodes/{nodeId}/connected-nodes")
    public ResponseEntity<Set<Node>> getConnectedNodes(@PathVariable Long nodeId) {
        try {
            Set<Node> connectedNodes = flowchartService.getConnectedNodes(nodeId);
            return ResponseEntity.ok(connectedNodes);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
