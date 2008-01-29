package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;


/**
 * ���������[�J���ϐ��g�p��ۑ����邽�߂̃N���X
 * 
 * @author t-miyake
 *
 */
public class UnresolvedLocalVariableUsageInfo extends UnresolvedVariableUsageInfo {

    public UnresolvedLocalVariableUsageInfo(UnresolvedLocalVariableInfo referencedVariable, boolean reference){
        this.referencedVariable = referencedVariable;
        this.reference = reference;
    }
    
    public EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass,
            TargetMethodInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {
        // TODO �����������ꂽ���\�b�h�E�X�^�u
        return null;
    }
   
}
