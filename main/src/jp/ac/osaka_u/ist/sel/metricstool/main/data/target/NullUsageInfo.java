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
     * ���O��������Ă��邩�ǂ�����Ԃ��D
     * 
     * @return ��� true ��Ԃ�
     */
    public boolean alreadyResolved() {
        return true;
    }

    /**
     * ���O�������ꂽ�g�p����Ԃ�
     * 
     * @return �������g��Ԃ�
     */
    @Override
    public EntityUsageInfo getResolvedEntityUsage() {
        return this;
    }

    /**
     * �g�p���̖��O��������
     * null�͊��ɉ����ς݂Ȃ̂Ŏ������g�����̂܂ܕԂ�
     * 
     * @return �����ς݂̎g�p���i�������g�j
     */
    @Override
    public EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass,
            TargetMethodInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {
        return this;
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

    /**
     * null�g�p�̌^��ۑ����邽�߂̒萔
     */
    private static final TypeInfo NULLTYPE = UnknownTypeInfo.getInstance();
}
