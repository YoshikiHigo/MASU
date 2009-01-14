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
     * �ϐ��̎g�p��Set��Ԃ�
     * 
     * @return �ϐ��̎g�p��Set
     */
    Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages();


    /**
     * ��`����Ă���ϐ���Set��Ԃ�
     * 
     * @return ���̒��Œ�`����Ă���ϐ���Set
     */
    Set<VariableInfo<? extends UnitInfo>> getDefinedVariables();
    
    /**
     * ���\�b�h�Ăяo����Ԃ�
     * 
     * @return ���\�b�h�Ăяo��
     */
    Set<CallInfo<?>> getCalls();
}