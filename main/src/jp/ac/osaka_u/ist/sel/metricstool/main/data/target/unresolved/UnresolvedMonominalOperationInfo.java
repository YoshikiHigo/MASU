package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;

/**
 * �ꍀ���Z�̓��e��\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedMonominalOperationInfo extends UnresolvedEntityUsageInfo {

    /**
     * ���ƈꍀ���Z�̌��ʂ̌^��^���ď�����
     * 
     * @param term ��
     * @param type �ꍀ���Z�̌��ʂ̌^
     */
    public UnresolvedMonominalOperationInfo(final UnresolvedEntityUsageInfo term, final PrimitiveTypeInfo type) {
        this.term = term;
        this.type = type;
    }
    
    @Override
    boolean alreadyResolved() {
        // TODO �����������ꂽ���\�b�h�E�X�^�u
        return false;
    }

    @Override
    EntityUsageInfo getResolvedEntityUsage() {
        // TODO �����������ꂽ���\�b�h�E�X�^�u
        return null;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass,
            CallableUnitInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {
        // TODO �����������ꂽ���\�b�h�E�X�^�u
        return null;
    }
    
    /**
     * �ꍀ���Z�̍���Ԃ�
     * 
     * @return �ꍀ���Z�̍�
     */
    public UnresolvedEntityUsageInfo getTerm() {
        return term;
    }

    /**
     * �ꍀ���Z�̌��ʂ̌^��Ԃ�
     * 
     * @return �ꍀ���Z�̌��ʂ̌^
     */
    public PrimitiveTypeInfo getResultType() {
        return type;
    }
    
    /**
     * �ꍀ���Z�̍�
     */
    private final UnresolvedEntityUsageInfo term;
    
    /**
     * �ꍀ���Z�̌��ʂ̌^
     */
    private final PrimitiveTypeInfo type;

    

}
