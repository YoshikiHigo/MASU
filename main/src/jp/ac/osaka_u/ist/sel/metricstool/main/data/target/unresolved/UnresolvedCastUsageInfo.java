package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;

/**
 * �������L���X�g�g�p��\���N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedCastUsageInfo extends UnresolvedEntityUsageInfo {

    /**
     * �L���X�g���ꂽ����^���ď�����
     * 
     * @param castedType �L���X�g���ꂽ�^
     */
    public UnresolvedCastUsageInfo(UnresolvedTypeInfo castedType) {
        this.castedType = castedType;
    }

    /**
     * �L���X�g���ꂽ�^��Ԃ�
     * @return �L���X�g���ꂽ�^
     */
    public UnresolvedTypeInfo getCastedType() {
        return castedType;
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
     * �L���X�g���ꂽ�^��ۑ�����ϐ�
     */
    private final UnresolvedTypeInfo castedType;


}
