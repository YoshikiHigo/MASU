package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashSet;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;


/**
 * �t�H���[�h�X���C�X�C�o�b�N���[�h�X���C�X���s���N���X
 * 
 * @author higo
 *
 */
class ProgramSlice {

    /**
     * �o�b�N���[�h�X���C�X���s��
     * 
     * @param nodeA�@���ǂ錳�ƂȂ钸�_A
     * @param nodeB�@���ǂ錳�ƂȂ钸�_B
     * @param clonePair ���ݍ\�z���̃N���[���y�A
     * @param checkedNodesA �����ςݒ��_�Q��ۑ����邽�߂̃L���b�V��A
     * @param checkedNodesB �����ςݒ��_�Q��ۑ����邽�߂̃L���b�V��B
     */
    static void addDuplicatedElementsWithBackwordSlice(final PDGNode<?> nodeA,
            final PDGNode<?> nodeB, final ClonePairInfo clonePair,
            final HashSet<PDGNode<?>> checkedNodesA, final HashSet<PDGNode<?>> checkedNodesB) {

        final SortedSet<PDGEdge> edgesA = nodeA.getBackwardEdges();
        final SortedSet<PDGEdge> edgesB = nodeB.getBackwardEdges();

        for (final PDGEdge edgeA : edgesA) {

            final PDGNode<?> fromNodeA = edgeA.getFromNode();

            if (checkedNodesA.contains(fromNodeA)) {
                continue;
            }

            final ExecutableElementInfo coreA = fromNodeA.getCore();
            final int hashA = Conversion.getNormalizedString(coreA).hashCode();

            for (final PDGEdge edgeB : edgesB) {

                //�@�G�b�W�̎�ނ��Ⴄ�ꍇ�͉������Ȃ�
                if (edgeA.getClass() != edgeB.getClass()) {
                    continue;
                }

                final PDGNode<?> fromNodeB = edgeB.getFromNode();

                if (checkedNodesB.contains(fromNodeB)) {
                    continue;
                }

                final ExecutableElementInfo coreB = fromNodeB.getCore();
                final int hashB = Conversion.getNormalizedString(coreB).hashCode();

                DetectionThread.increaseNumberOfComparison();
               
                if (hashA == hashB) {

                    //A,B�ǂ��炩��̗v�f�����łɃN���[���y�A�Ɋ܂܂�Ă���ꍇ�͉������Ȃ�
                    final CodeCloneInfo codeFragmentA = clonePair.getCodeFragmentA();
                    final CodeCloneInfo codeFragmentB = clonePair.getCodeFragmentB();
                    if (codeFragmentA.contain(coreA) || codeFragmentB.contain(coreB)) {
                        continue;
                    }

                    //A,B�ǂ��炪�̗v�f���C���葤�̃N���[���y�A�Ɋ܂܂�Ă���ꍇ�͉������Ȃ�
                    if (codeFragmentA.contain(coreB) || codeFragmentB.contain(coreA)) {
                        continue;
                    }

                    //coreA��coreB������ExecutableElement�ł���Ή������Ȃ�
                    if (coreA == coreB) {
                        continue;
                    }

                    clonePair.add(coreA, coreB);
                    checkedNodesA.add(fromNodeA);
                    checkedNodesB.add(fromNodeB);

                    if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.BACKWARD)) {
                        addDuplicatedElementsWithBackwordSlice(fromNodeA, fromNodeB, clonePair,
                                checkedNodesA, checkedNodesB);
                    }
                    if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.FORWARD)) {
                        addDuplicatedElementsWithForwordSlice(fromNodeA, fromNodeB, clonePair,
                                checkedNodesA, checkedNodesB);
                    }
                }
            }
        }
    }

    /**
     * �t�H���[�h�X���C�X���s��
     * 
     * @param nodeA�@���ǂ錳�ƂȂ钸�_A
     * @param nodeB�@���ǂ錳�ƂȂ钸�_B
     * @param clonePair ���ݍ\�z���̃N���[���y�A
     * @param checkedNodesA �����ςݒ��_�Q��ۑ����邽�߂̃L���b�V��A
     * @param checkedNodesB �����ςݒ��_�Q��ۑ����邽�߂̃L���b�V��B
     */
    static void addDuplicatedElementsWithForwordSlice(final PDGNode<?> nodeA,
            final PDGNode<?> nodeB, final ClonePairInfo clonePair,
            final HashSet<PDGNode<?>> checkedNodesA, final HashSet<PDGNode<?>> checkedNodesB) {

        final SortedSet<PDGEdge> edgesA = nodeA.getForwardEdges();
        final SortedSet<PDGEdge> edgesB = nodeB.getForwardEdges();

        for (final PDGEdge edgeA : edgesA) {

            final PDGNode<?> toNodeA = edgeA.getToNode();

            if (checkedNodesA.contains(toNodeA)) {
                continue;
            }

            final ExecutableElementInfo coreA = toNodeA.getCore();
            final int hashA = Conversion.getNormalizedString(coreA).hashCode();

            for (final PDGEdge edgeB : edgesB) {

                //�@�G�b�W�̎�ނ��Ⴄ�ꍇ�͉������Ȃ�
                if (edgeA.getClass() != edgeB.getClass()) {
                    continue;
                }

                final PDGNode<?> toNodeB = edgeB.getToNode();

                if (checkedNodesB.contains(toNodeB)) {
                    continue;
                }

                final ExecutableElementInfo coreB = toNodeB.getCore();
                final int hashB = Conversion.getNormalizedString(coreB).hashCode();

                DetectionThread.increaseNumberOfComparison();
                
                if (hashA == hashB) {

                    //A,B�ǂ��炩��̗v�f�����łɃN���[���y�A�Ɋ܂܂�Ă���ꍇ�͉������Ȃ�
                    final CodeCloneInfo codeFragmentA = clonePair.getCodeFragmentA();
                    final CodeCloneInfo codeFragmentB = clonePair.getCodeFragmentB();
                    if (codeFragmentA.contain(coreA) || codeFragmentB.contain(coreB)) {
                        continue;
                    }

                    //A,B�ǂ��炪�̗v�f���C���葤�̃N���[���y�A�Ɋ܂܂�Ă���ꍇ�͉������Ȃ�
                    if (codeFragmentA.contain(coreB) || codeFragmentB.contain(coreA)) {
                        continue;
                    }

                    //coreA��coreB������ExecutableElement�ł���Ή������Ȃ�
                    if (coreA == coreB) {
                        continue;
                    }

                    clonePair.add(coreA, coreB);
                    checkedNodesA.add(toNodeA);
                    checkedNodesB.add(toNodeB);

                    if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.BACKWARD)) {
                        addDuplicatedElementsWithBackwordSlice(toNodeA, toNodeB, clonePair,
                                checkedNodesA, checkedNodesB);
                    }
                    if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.FORWARD)) {
                        addDuplicatedElementsWithForwordSlice(toNodeA, toNodeB, clonePair,
                                checkedNodesA, checkedNodesB);
                    }
                }
            }
        }
    }
}
