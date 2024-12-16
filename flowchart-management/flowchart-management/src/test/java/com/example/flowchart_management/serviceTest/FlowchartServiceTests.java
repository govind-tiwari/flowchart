package com.example.flowchart_management.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.flowchart_management.Repository.EdgeRepository;
import com.example.flowchart_management.Repository.FlowchartRepository;
import com.example.flowchart_management.Repository.NodeRepository;
import com.example.flowchart_management.entity.Edge;
import com.example.flowchart_management.entity.Flowchart;
import com.example.flowchart_management.entity.Node;
import com.example.flowchart_management.service.FlowchartService;

class FlowchartServiceTests {

    @Mock
    private FlowchartRepository flowchartRepository;

    @Mock
    private NodeRepository nodeRepository;

    @Mock
    private EdgeRepository edgeRepository;

    @InjectMocks
    private FlowchartService flowchartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFlowchart() {
        Flowchart flowchart = new Flowchart();
        flowchart.setName("Test Flowchart");

        when(flowchartRepository.save(any(Flowchart.class))).thenReturn(flowchart);

        Flowchart savedFlowchart = flowchartService.createFlowchart(flowchart);

        assertNotNull(savedFlowchart);
        assertEquals("Test Flowchart", savedFlowchart.getName());
        verify(flowchartRepository, times(1)).save(any(Flowchart.class));
    }

    @Test
    void testGetFlowchart() {
        Long id = 1L;
        Flowchart flowchart = new Flowchart();
        flowchart.setId(id);
        flowchart.setName("Test Flowchart");

        when(flowchartRepository.findById(id)).thenReturn(Optional.of(flowchart));

        Optional<Flowchart> result = Optional.ofNullable(flowchartService.getFlowchart(id));

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        assertEquals("Test Flowchart", result.get().getName());
    }

    @Test
    void testUpdateFlowchart() {
        Long id = 1L;
        Flowchart existingFlowchart = new Flowchart();
        existingFlowchart.setId(id);
        existingFlowchart.setName("Old Name");

        Flowchart updatedFlowchart = new Flowchart();
        updatedFlowchart.setName("New Name");

        when(flowchartRepository.findById(id)).thenReturn(Optional.of(existingFlowchart));
        when(flowchartRepository.save(any(Flowchart.class))).thenReturn(updatedFlowchart);

        Flowchart result = flowchartService.updateFlowchart(id, updatedFlowchart);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(flowchartRepository, times(1)).save(any(Flowchart.class));
    }

    @Test
    void testDeleteFlowchart() {
        Long id = 1L;

        flowchartService.deleteFlowchart(id);

        verify(flowchartRepository, times(1)).deleteById(id);
    }

    @Test
    void testValidateGraph() {
        Flowchart flowchart = new Flowchart();
        Node node1 = new Node();
        Node node2 = new Node();
        Edge edge = new Edge();
        edge.setSource(node1);
        edge.setTarget(node2);

        flowchart.setNodes(Arrays.asList(node1, node2));
        flowchart.setEdges(List.of(edge));

        boolean isValid = flowchartService.validateGraph(flowchart);

        assertTrue(isValid);
    }

    @Test
    void testGetOutgoingEdges() {
        Long nodeId = 1L;
        Node node = new Node();
        node.setId(nodeId);

        Edge edge1 = new Edge();
        Edge edge2 = new Edge();

        when(nodeRepository.findById(nodeId)).thenReturn(Optional.of(node));
        when(edgeRepository.findBySource(node)).thenReturn(Arrays.asList(edge1, edge2));

        List<Edge> outgoingEdges = flowchartService.getOutgoingEdges(nodeId);

        assertEquals(2, outgoingEdges.size());
        verify(edgeRepository, times(1)).findBySource(node);
    }

    @Test
    void testGetConnectedNodes() {
        Long nodeId = 1L;
        Node startNode = new Node();
        startNode.setId(nodeId);

        Node connectedNode1 = new Node();
        Node connectedNode2 = new Node();

        Edge edge1 = new Edge();
        edge1.setSource(startNode);
        edge1.setTarget(connectedNode1);

        Edge edge2 = new Edge();
        edge2.setSource(connectedNode1);
        edge2.setTarget(connectedNode2);

        when(nodeRepository.findById(nodeId)).thenReturn(Optional.of(startNode));
        when(edgeRepository.findBySource(startNode)).thenReturn(List.of(edge1));
        when(edgeRepository.findBySource(connectedNode1)).thenReturn(List.of(edge2));
        when(edgeRepository.findBySource(connectedNode2)).thenReturn(List.of());

        Set<Node> connectedNodes = flowchartService.getConnectedNodes(nodeId);

        assertEquals(3, connectedNodes.size());
        assertTrue(connectedNodes.contains(startNode));
        assertTrue(connectedNodes.contains(connectedNode1));
        assertTrue(connectedNodes.contains(connectedNode2));
    }
}
