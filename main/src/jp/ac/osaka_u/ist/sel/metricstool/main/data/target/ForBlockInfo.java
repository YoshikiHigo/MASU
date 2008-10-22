package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * for �u���b�N��\���N���X
 * 
 * @author higo
 * 
 */
public final class ForBlockInfo extends ConditionalBlockInfo {

    /**
     * �ʒu����^���� for �u���b�N��������
     * 
     * @param ownerClass �����N���X
     * @param ownerMethod �������\�b�h
     * @param outerSpace �O���̃u���b�N
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ForBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final LocalSpaceInfo outerSpace, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, outerSpace, fromLine, fromColumn, toLine, toColumn);
        
        this.initilizerExpressions = new TreeSet<ConditionInfo>();
        this.iteratorExpressions = new TreeSet<ExpressionInfo>();
        
    }
    
    /**
     * for���̏���������ǉ�
     * @param initializerExpression ��������
     */
    public final void addInitializerExpressions(final ConditionInfo initializerExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        
        if(null == initializerExpression) {
            throw new IllegalArgumentException("initializerExpression is null");
        }
        
        this.initilizerExpressions.add(initializerExpression);
    }
    
    /**
     * for���̍X�V����ǉ�
     * @param iteratorExpression �J��Ԃ���
     */
    public final void addIteratorExpressions(final ExpressionInfo iteratorExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        
        if(null == iteratorExpression) {
            throw new IllegalArgumentException("updateExpression is null");
        }
        
        this.iteratorExpressions.add(iteratorExpression);
    }
    
    /**
     * ���������̃Z�b�g��Ԃ�
     * @return ���������̃Z�b�g
     */
    public final SortedSet<ConditionInfo> getInitializerExpressions() {
        return Collections.unmodifiableSortedSet(this.initilizerExpressions);
    }
    
    /**
     * �X�V���̃Z�b�g��Ԃ�
     * @return �X�V��
     */
    public final SortedSet<ExpressionInfo> getIteratorExpressions() {
        return Collections.unmodifiableSortedSet(this.iteratorExpressions);
    }
    
    /**
     * ����������ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<ConditionInfo> initilizerExpressions;
    
    /**
     * �X�V����ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<ExpressionInfo> iteratorExpressions;
}
