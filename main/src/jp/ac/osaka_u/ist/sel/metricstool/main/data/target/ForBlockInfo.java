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
        
        this.initilizerExpressions = new TreeSet<ExpressionInfo>();
        this.updateExpressions = new TreeSet<ExpressionInfo>();
        
    }
    
    /**
     * for���̏���������ǉ�
     * @param initializerExpression ��������
     */
    public final void addInitializerExpressions(final ExpressionInfo initializerExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        
        if(null == initializerExpression) {
            throw new IllegalArgumentException("initializerExpression is null");
        }
        
        this.initilizerExpressions.add(initializerExpression);
    }
    
    /**
     * for���̍X�V����ǉ�
     * @param updateExpression �X�V��
     */
    public final void addUpdateExpressions(final ExpressionInfo updateExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        
        if(null == updateExpression) {
            throw new IllegalArgumentException("updateExpression is null");
        }
        
        this.updateExpressions.add(updateExpression);
    }
    
    /**
     * ���������̃Z�b�g��Ԃ�
     * @return ���������̃Z�b�g
     */
    public final SortedSet<ExpressionInfo> getInitializerExpressions() {
        return Collections.unmodifiableSortedSet(this.initilizerExpressions);
    }
    
    /**
     * �X�V���̃Z�b�g��Ԃ�
     * @return �X�V��
     */
    public final SortedSet<ExpressionInfo> getUpdateExpressions() {
        return Collections.unmodifiableSortedSet(this.updateExpressions);
    }
    
    /**
     * ����������ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<ExpressionInfo> initilizerExpressions;
    
    /**
     * �X�V����ۑ����邽�߂̕ϐ�
     */
    private final SortedSet<ExpressionInfo> updateExpressions;
}
