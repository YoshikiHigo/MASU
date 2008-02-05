package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;


/**
 * �������ϐ��g�p��ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 *
 */
public abstract class UnresolvedVariableUsageInfo extends UnresolvedEntityUsageInfo {

    public UnresolvedVariableUsageInfo(final String usedVariableName, final boolean reference) {
        this.usedVariableName = usedVariableName;
        this.reference = reference;
    }

    /**
     * ���̕ϐ��g�p���Q�Ƃł��邩�ǂ�����Ԃ�
     * 
     * @return �Q�Ƃł���ꍇ�� true�C����ł���ꍇ�� false
     */
    public final boolean isReference() {
        return this.reference;
    }

    /**
     * ���̕ϐ��g�p������ł��邩�ǂ�����Ԃ�
     * 
     * @return ����ł���ꍇ�� true�C�Q�Ƃł���ꍇ�� false
     */
    public final boolean isAssignment() {
        return !this.reference;
    }

    /**
     * ���̖������ϐ��g�p�����ɉ�������Ă��邩�ǂ�����Ԃ�
     * 
     * @return ���ɉ�������Ă���ꍇ�� true, �����łȂ��ꍇ�� false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * �����ςݕϐ��g�p��Ԃ�
     */
    @Override
    public final EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }
    
    /**
     * �g�p����Ă���ϐ��̖��O��Ԃ�
     * @return �g�p����Ă���ϐ��̖��O
     */
    public String getUsedVariableName() {
        return usedVariableName;
    }

    /**
     * �g�p����Ă���ϐ��̖��O��ۑ�����ϐ�
     */
    protected final String usedVariableName;
    
    protected boolean reference;

    /**
     * �����ςݕϐ��g�p��ۑ����邽�߂̕ϐ�
     */
    protected EntityUsageInfo resolvedInfo;

    
}
