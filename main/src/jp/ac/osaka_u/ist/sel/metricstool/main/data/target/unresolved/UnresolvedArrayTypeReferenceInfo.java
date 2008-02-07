package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;

/**
 * �������z��^�Q�Ƃ�\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedArrayTypeReferenceInfo extends UnresolvedEntityUsageInfo {

    /**
     * �Q�Ƃ���Ă��関�����z��^��^���ď�����
     * 
     * @param referencedType �Q�Ƃ���Ă��関�����z��^
     */
    public UnresolvedArrayTypeReferenceInfo(final UnresolvedArrayTypeInfo referencedType) {
        this.referencedType = referencedType;
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
     * �Q�Ƃ���Ă��関�����z��^��Ԃ�
     * @return �Q�Ƃ���Ă��関�����z��^
     */
    public UnresolvedArrayTypeInfo getType() {
        return this.referencedType;
    }
    
    /**
     * �Q�Ƃ���Ă��関�����z��^��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedArrayTypeInfo referencedType;
}
