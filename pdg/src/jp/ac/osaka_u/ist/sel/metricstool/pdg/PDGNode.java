package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * PDG���\������m�[�h��\���N���X
 * 
 * @author t-miyake
 *
 * @param <T> �m�[�h�̊j�ƂȂ���̌^
 */
public abstract class PDGNode<T> {

    /**
     * �t�H���[�h�G�b�W�i���̃m�[�h����̈ˑ��Ӂj
     */
    private final Set<PDGEdge> forwardEdges;

    /**
     * �o�b�N���[�h�G�b�W�i���̃m�[�h�ւ̈ˑ��Ӂj
     */
    private final Set<PDGEdge> backwardEdges;

    /**
     * �m�[�h��Œl����`�E�ύX�����ϐ�
     */
    private final Set<VariableInfo<? extends UnitInfo>> definedVariables;

    /**
     * �m�[�h�̊j�ƂȂ���
     */
    private final T core;

    protected String text;
    
    /**
     * �m�[�h�̊j�ƂȂ����^���ď�����
     * @param core �m�[�h�̊j�ƂȂ���
     */
    protected PDGNode(final T core) {
        if (null == core) {
            throw new IllegalArgumentException("statement is null.");
        }

        this.core = core;
        this.forwardEdges = new HashSet<PDGEdge>();
        this.backwardEdges = new HashSet<PDGEdge>();

        this.definedVariables = this.extractDefinedVariables(core);
    }

    /**
     * �m�[�h��Œ�`�E�ύX�����ϐ��𒊏o
     * @param core �m�[�h�̊j�ƂȂ���
     * @return �m�[�h��Œ�`�E�ύX�����ϐ�
     */
    protected abstract Set<VariableInfo<? extends UnitInfo>> extractDefinedVariables(final T core);

    public abstract boolean isDefine(final VariableInfo<? extends UnitInfo> variable);

    public  abstract boolean isReferenace(final VariableInfo<? extends UnitInfo> variable);
    
    /**
     * ���̃m�[�h�̃t�H���[�h�G�b�W��ǉ�
     * @param forwardEdge ���̃m�[�h�̃t�H���[�h�G�b�W
     */
    protected boolean addFowardEdge(PDGEdge forwardEdge) {
        if (null == forwardEdge) {
            throw new IllegalArgumentException("forwardNode is null.");
        }

        if (!forwardEdge.getFromNode().equals(this)) {
            throw new IllegalArgumentException();
        }

        return this.forwardEdges.add(forwardEdge);
    }

    /**
     * ���̃m�[�h�̃o�b�N���[�h�G�b�W��ǉ�
     * @param backwardEdge
     */
    protected boolean addBackwardEdge(PDGEdge backwardEdge) {
        return this.backwardEdges.add(backwardEdge);
    }
    
    void removeBackwardEdge(final PDGEdge backwardEdge){
        this.backwardEdges.remove(backwardEdge);
    }
    
    void removeForwardEdge(final PDGEdge forwardEdge) {
        this.forwardEdges.remove(forwardEdge);
    }
    
    

    /**
     * ���̃m�[�h����̃f�[�^�ˑ��ς�ǉ�
     * @param dependingNode
     */
    public boolean addDataDependingNode(final PDGNode<?> dependingNode) {
        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        boolean added = false;
        final DataDependenceEdge dataFlow = new DataDependenceEdge(this, dependingNode);
        added = this.addFowardEdge(dataFlow);
        added &= dependingNode.addBackwardEdge(dataFlow);
        return added;
    }
    
    /**
     * ���̃m�[�h�̃o�b�N���[�h�G�b�W���擾
     * @return ���̃m�[�h�̃o�b�N���[�h�G�b�W
     */
    public final Set<PDGEdge> getBackwardEdges() {
        return Collections.unmodifiableSet(this.backwardEdges);
    }

    /**
     * ���̃m�[�h�̃t�H���[�h�G�b�W���擾
     * @return ���̃m�[�h�̃t�H���[�h�G�b�W
     */
    public final Set<PDGEdge> getForwardEdges() {
        return Collections.unmodifiableSet(this.forwardEdges);
    }

    /**
     * ���̃m�[�h�Œ�`�E�ύX����Ă���ϐ����擾
     * @return ���̃m�[�h�Œ�`�E�ύX����Ă���ϐ�
     */
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return Collections.unmodifiableSet(this.definedVariables);
    }

    /**
     * ���̃m�[�h�̊j�ƂȂ���擾
     * @return ���̃m�[�h�̊j�ƂȂ���
     */
    public final T getCore() {
        return this.core;
    }
    
    public String getText() {
        return this.text;
    }
}
