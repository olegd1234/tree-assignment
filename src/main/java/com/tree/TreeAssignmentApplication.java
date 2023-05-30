package com.tree;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TreeAssignmentApplication {

    public static void main(String[] args) throws IOException {

        File jsonFile = new ClassPathResource("files.json").getFile();
        ObjectMapper mapper = new ObjectMapper();
        HwFile[] hwFilesArr = mapper.readValue(jsonFile, HwFile[].class);

        Map<String, Node<HwFile>> idToHwFileNodeMap = new HashMap<>();
        for (HwFile hwFile : hwFilesArr) {
            idToHwFileNodeMap.put(hwFile.getId(), new Node<>(hwFile));
        }

        List<Node<HwFile>> roots = new ArrayList<>();
        for (Node<HwFile> node : idToHwFileNodeMap.values()) {
            String parentId = node.getData().getParentId();
            if (parentId == null) {
                roots.add(node);
            } else {
                idToHwFileNodeMap.get(parentId).addChild(node);
            }
        }

        //now we can traverse our multiple roots and print the paths of the nodes meeting the criteria
        for (Node<HwFile> root : roots) {
            traverse(
                    root,
                    hwFile -> hwFile.getCreatedBy().equals("user@kramerav.com"),
                    (path, node) -> System.out.println(path));
        }

    }
    private static void traverse(Node<HwFile> root, Predicate<HwFile> criteria, TraverseNodeMethod traverseNodeMethod) {
        traverse(root, "", criteria, traverseNodeMethod);
    }

    private static void traverse(Node<HwFile> nodeOnPath, String onPath, Predicate<HwFile> criteria, TraverseNodeMethod traverseNodeMethod) {

        String nodePath = onPath.equals("") ?
                nodeOnPath.getData().getName() :
                onPath + "/" + nodeOnPath.getData().getName();
        if (criteria.test(nodeOnPath.getData())) {
            traverseNodeMethod.execute(nodePath, nodeOnPath);
        }
        if (CollectionUtils.isEmpty(nodeOnPath.getChildNodes())) {
            return;
        }

        List<Node<HwFile>> childNodes = nodeOnPath.getChildNodes();
        for (Node<HwFile> node : childNodes){
            traverse(node, nodePath, criteria, traverseNodeMethod);
        }
    }

    interface TraverseNodeMethod {
        void execute(String path, Node<HwFile> node);
    }
}
