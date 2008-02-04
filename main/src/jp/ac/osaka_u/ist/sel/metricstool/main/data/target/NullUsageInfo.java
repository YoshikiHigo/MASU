package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;


/**
 * null�g�p��\���N���X�D
 * 
 * @author higo, t-miyake
 * 
 */
public final class NullUsageInfo extends EntityUsageInfo implements UnresolvedEntityUsageInfo {

    public NullUsageInfo() {
        super();
    }

    /**
     * null�g�p�̌^�͕s��
     * 
     * @return �s���^��Ԃ�
     */
    @Override
    public TypeInfo getType() {
        return NULLTYPE;
    }

    @Override
    public boolean alreadyResolved() {
        return true;
    }

    @Override
    public EntityUsageInfo getResolvedEntityUsage() {
        return this;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass,
            TargetMethodInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {
        return this;
    }

    /**
     * null�g�p�̌^��ۑ����邽�߂̒萔
     */
    private static final TypeInfo NULLTYPE = UnknownTypeInfo.getInstance();
}
