package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;


/**
 * ��̎���\���N���X
 * return ; �� for ( ; ; ) �Ȃǂɗp����
 * 
 * @author higo
 *
 */
public final class EmptyExpressionInfo extends EntityUsageInfo {

    /**
     * @param ownerExecutableElement �I�[�i�[�G�������g 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public EmptyExpressionInfo(final ExecutableElementInfo ownerExecutableElement,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerExecutableElement, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * void �^��Ԃ�
     * 
     * return void�^ 
     */
    @Override
    public TypeInfo getType() {
        return VoidTypeInfo.getInstance();
    }

    /**
     * ����0��String��Ԃ�
     * 
     * return ����0��String
     */
    @Override
    public String getText() {
        return "";
    }

    /**
     * �g�p����Ă���ϐ���Set��Ԃ��D
     * ���ۂ͂Ȃɂ��g�p����Ă��Ȃ��̂ŁC���Set���Ԃ����D
     * 
     * @return �g�p����Ă���ϐ���Set
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return new HashSet<VariableUsageInfo<?>>();
    }

}
