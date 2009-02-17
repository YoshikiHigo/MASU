package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;

/**
 * PDG��\���N���X
 * 
 * @author t-miyake
 *
 */
public abstract class PDG {

    /**
     * PDG�̓����m�[�h
     */
    protected final Set<PDGNode<?>> enterNodes;

    /**
     * PDG�̏o���m�[�h
     */
    protected final Set<PDGNode<?>> exitNodes;

    protected final IPDGNodeFactory nodeFactory;

    public PDG(final IPDGNodeFactory nodeFactory) {

        if(null == nodeFactory) {
            throw new IllegalArgumentException();
        }
        this.nodeFactory = nodeFactory;
        
        this.enterNodes = new HashSet<PDGNode<?>>();
        this.exitNodes = new HashSet<PDGNode<?>>();

        //this.statementNodeCache = new HashMap<StatementInfo, ControllableNode<? extends StatementInfo>>();

    }
    
    /**
     * PDG���\�z����
     */
    protected abstract void buildPDG();
    
    /**
     * �����m�[�h���擾
     * @return �����m�[�h
     */
    public final Set<PDGNode<?>> getEnterNodes() {
        return this.enterNodes;
    }

    /**
     * �o���m�[�h���擾
     * @return �o���m�[�h
     */
    public final Set<PDGNode<?>> getExitNodes() {
        return this.exitNodes;
    }
    
    /**
     * PDG���̃m�[�h�����擾
     * @return 
     */
    public final int getNodeCount() {
        return this.nodeFactory.getAllNode().size();
    }
    
    /**
     * PDG�̃m�[�h���擾
     * @param statement �擾�������m�[�h�ɑΉ����镶
     * @return 
     */
    public final PDGNode<?> getPDGNode(final StatementInfo statement) {
        return this.nodeFactory.getNode(statement);
    }
    
    /**
     * PDG��̑S�m�[�h���擾
     * @return PDG�̑S�m�[�h
     */
    public final Collection<? extends PDGNode<?>> getAllNodes() {
        return this.nodeFactory.getAllNode();
    }
    
    public IPDGNodeFactory getNodeFactory() {
        return nodeFactory;
    }
    

}
