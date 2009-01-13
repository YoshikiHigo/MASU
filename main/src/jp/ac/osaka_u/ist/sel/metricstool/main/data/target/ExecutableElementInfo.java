package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.Set;


/**
 * ���s�\�ȒP�ʂ�\���v�f
 * 
 * @author higo
 *
 */
public interface ExecutableElementInfo extends Position, Comparable<ExecutableElementInfo>,
        Serializable {

    /**
     * �ϐ��̎g�p��Ԃ�
     * 
     * @return �����ł̕ϐ��̎g�p
     */
    Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();

    /**
     * ���\�b�h�Ăяo����Ԃ�
     * 
     * @return ���\�b�h�Ăяo��
     */
    Set<CallInfo<?>> getCalls();
}