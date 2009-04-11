package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGParameterNode;


public class ProgramSlice {

    static void addDuplicatedElementsWithBackwordSlice(final PDGNode<?> nodeA,
            final PDGNode<?> nodeB, final IPDGNodeFactory pdgNodeFactory,
            final Set<ClonePairInfo> clonePairs, final ClonePairInfo clonePair,
            final HashSet<PDGNode<?>> checkedNodesA, final HashSet<PDGNode<?>> checkedNodesB) {

        final Set<PDGEdge> edgesA = nodeA.getBackwardEdges();
        final Set<PDGEdge> edgesB = nodeB.getBackwardEdges();

        boolean extend = false;

        for (final PDGEdge edgeA : edgesA) {

            final PDGNode<?> fromNodeA = edgeA.getFromNode();

            if (checkedNodesA.contains(fromNodeA)) {
                continue;
            }

            //ParameterNode�ł���΂Ƃ΂��C�����͕ύX�̕K�v����
            if (fromNodeA instanceof PDGParameterNode) {
                continue;
            }

            final ExecutableElementInfo coreA = (ExecutableElementInfo) fromNodeA.getCore();
            final int hashA = Conversion.getNormalizedString(coreA).hashCode();

            for (final PDGEdge edgeB : edgesB) {

                final PDGNode<?> fromNodeB = edgeB.getFromNode();

                if (checkedNodesB.contains(fromNodeB)) {
                    continue;
                }

                //ParameterNode�ł���΂Ƃ΂��C�����͕ύX�̕K�v����
                if (fromNodeB instanceof PDGParameterNode) {
                    continue;
                }

                final ExecutableElementInfo coreB = (ExecutableElementInfo) fromNodeB.getCore();
                final int hashB = Conversion.getNormalizedString(coreB).hashCode();

                if (hashA == hashB) {

                    clonePair.add(coreA, coreB);
                    checkedNodesA.add(fromNodeA);
                    checkedNodesB.add(fromNodeB);

                    // ���򂪂Ȃ���
                    if (!extend) {
                        addDuplicatedElementsWithBackwordSlice(fromNodeA, fromNodeB,
                                pdgNodeFactory, clonePairs, clonePair, checkedNodesA, checkedNodesB);
                        extend = true;
                    }

                    // ���򂪂��鎞
                    else {
                        addDuplicatedElementsWithBackwordSlice(fromNodeA, fromNodeB,
                                pdgNodeFactory, clonePairs, clonePair.clone(),
                                (HashSet<PDGNode<?>>) checkedNodesA.clone(),
                                (HashSet<PDGNode<?>>) checkedNodesB.clone());
                    }

                    if ((fromNodeA instanceof PDGControlNode)
                            && (fromNodeB instanceof PDGControlNode)) {

                        // ���򂪂Ȃ���
                        if (!extend) {
                            addDuplicatedElementsWithForwordSlice(fromNodeA, fromNodeB,
                                    pdgNodeFactory, clonePairs, clonePair, checkedNodesA,
                                    checkedNodesB);
                            extend = true;
                        }

                        // ���򂪂��鎞
                        else {
                            addDuplicatedElementsWithForwordSlice(fromNodeA, fromNodeB,
                                    pdgNodeFactory, clonePairs, clonePair.clone(),
                                    (HashSet<PDGNode<?>>) checkedNodesA.clone(),
                                    (HashSet<PDGNode<?>>) checkedNodesB.clone());
                        }
                    }
                }
            }
        }

        if (!extend && (Configuration.INSTANCE.getS() <= clonePair.length())) {
            clonePairs.add(clonePair);
        }
    }

    static void addDuplicatedElementsWithForwordSlice(final PDGNode<?> nodeA,
            final PDGNode<?> nodeB, final IPDGNodeFactory pdgNodeFactory,
            final Set<ClonePairInfo> clonePairs, final ClonePairInfo clonePair,
            final HashSet<PDGNode<?>> checkedNodesA, final HashSet<PDGNode<?>> checkedNodesB) {

        final Set<PDGEdge> edgesA = nodeA.getForwardEdges();
        final Set<PDGEdge> edgesB = nodeB.getForwardEdges();

        boolean extend = false;

        for (final PDGEdge edgeA : edgesA) {

            final PDGNode<?> toNodeA = edgeA.getToNode();

            if (checkedNodesA.contains(toNodeA)) {
                continue;
            }

            //ParameterNode�ł���΂Ƃ΂��C�����͕ύX�̕K�v����
            if (toNodeA instanceof PDGParameterNode) {
                continue;
            }

            final ExecutableElementInfo coreA = (ExecutableElementInfo) toNodeA.getCore();
            final int hashA = Conversion.getNormalizedString(coreA).hashCode();

            for (final PDGEdge edgeB : edgesB) {

                final PDGNode<?> toNodeB = edgeB.getToNode();

                if (checkedNodesB.contains(toNodeB)) {
                    continue;
                }

                //ParameterNode�ł���΂Ƃ΂��C�����͕ύX�̕K�v����
                if (toNodeB instanceof PDGParameterNode) {
                    continue;
                }

                final ExecutableElementInfo coreB = (ExecutableElementInfo) toNodeB.getCore();
                final int hashB = Conversion.getNormalizedString(coreB).hashCode();

                if (hashA == hashB) {

                    extend = true;

                    clonePair.add(coreA, coreB);
                    checkedNodesA.add(toNodeA);
                    checkedNodesB.add(toNodeB);

                    // ���򂪂Ȃ���
                    if (!extend) {
                        addDuplicatedElementsWithBackwordSlice(toNodeA, toNodeB, pdgNodeFactory,
                                clonePairs, clonePair, checkedNodesA, checkedNodesB);
                        extend = true;
                    }

                    // ���򂪂��鎞
                    else {
                        addDuplicatedElementsWithBackwordSlice(toNodeA, toNodeB, pdgNodeFactory,
                                clonePairs, clonePair.clone(), (HashSet<PDGNode<?>>) checkedNodesA
                                        .clone(), (HashSet<PDGNode<?>>) checkedNodesB.clone());
                    }

                    if ((toNodeA instanceof PDGControlNode) && (toNodeB instanceof PDGControlNode)) {

                        // ���򂪂Ȃ���
                        if (!extend) {
                            addDuplicatedElementsWithForwordSlice(toNodeA, toNodeB, pdgNodeFactory,
                                    clonePairs, clonePair, checkedNodesA, checkedNodesB);
                            extend = true;
                        }

                        // ���򂪂��鎞
                        else {
                            addDuplicatedElementsWithForwordSlice(toNodeA, toNodeB, pdgNodeFactory,
                                    clonePairs, clonePair.clone(),
                                    (HashSet<PDGNode<?>>) checkedNodesA.clone(),
                                    (HashSet<PDGNode<?>>) checkedNodesB.clone());
                        }

                    }
                }
            }
        }

        if (!extend && (Configuration.INSTANCE.getS() <= clonePair.length())) {
            clonePairs.add(clonePair);
        }
    }
}
