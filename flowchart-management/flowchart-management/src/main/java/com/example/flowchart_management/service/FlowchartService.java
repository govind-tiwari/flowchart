package com.example.flowchart_management.service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.flowchart_management.Repository.EdgeRepository;
import com.example.flowchart_management.Repository.FlowchartRepository;
import com.example.flowchart_management.Repository.NodeRepository;
import com.example.flowchart_management.entity.Edge;
import com.example.flowchart_management.entity.Flowchart;
import com.example.flowchart_management.entity.Node;

@Service
public class FlowchartService {

    @Autowired
    private FlowchartRepository flowchartRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    public Flowchart createFlowchart(Flowchart flowchart) {
        // Set flowchart reference in nodes and edges
        for (Node node : flowchart.getNodes()) {
            node.setFlowchart(flowchart);
        }
        for (Edge edge : flowchart.getEdges()) {
            edge.setFlowchart(flowchart);
        }
        return flowchartRepository.save(flowchart);
    }

    public Flowchart getFlowchart(Long id) {
        return flowchartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Flowchart not found with id: " + id));
    }

    public Flowchart updateFlowchart(Long id, Flowchart newFlowchart) {
        return flowchartRepository.findById(id)
                .map(flowchart -> {
                    flowchart.setName(newFlowchart.getName());
                    flowchart.getNodes().clear();
                    flowchart.getEdges().clear();
                    flowchart.getNodes().addAll(newFlowchart.getNodes());
                    flowchart.getEdges().addAll(newFlowchart.getEdges());
                    
                    // Set flowchart reference in updated nodes and edges
                    for (Node node : flowchart.getNodes()) {
                        node.setFlowchart(flowchart);
                    }
                    for (Edge edge : flowchart.getEdges()) {
                        edge.setFlowchart(flowchart);
                    }
                    return flowchartRepository.save(flowchart);
                })
                .orElseThrow(() -> new EntityNotFoundException("Flowchart not found with id: " + id));
    }

    public void deleteFlowchart(Long id) {
        if (!flowchartRepository.existsById(id)) {
            throw new EntityNotFoundException("Flowchart not found with id: " + id);
        }
        flowchartRepository.deleteById(id);
    }

    public boolean validateGraph(Flowchart flowchart) {
        Set<Node> nodes = new HashSet<>(flowchart.getNodes());
        for (Edge edge : flowchart.getEdges()) {
            if (!nodes.contains(edge.getSource()) || !nodes.contains(edge.getTarget())) {
                return false;
            }
        }
        return true;
    }

    public List<Edge> getOutgoingEdges(Long nodeId) {
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        return edgeRepository.findBySource(node);
    }

    public Set<Node> getConnectedNodes(Long nodeId) {
        Node startNode = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new EntityNotFoundException("Node not found with id: " + nodeId));
        Set<Node> connectedNodes = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        queue.offer(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            if (connectedNodes.add(currentNode)) {
                List<Edge> outgoingEdges = edgeRepository.findBySource(currentNode);
                for (Edge edge : outgoingEdges) {
                    queue.offer(edge.getTarget());
                }
            }
        }

        return connectedNodes;
    }
}
