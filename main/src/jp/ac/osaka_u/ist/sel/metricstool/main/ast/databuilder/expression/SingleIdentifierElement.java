package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;

public class SingleIdentifierElement implements IdentifierElement{
    
    public SingleIdentifierElement(String name, UnresolvedEntityUsageInfo ownerUsage){
        this.name = name;
        this.qualifiedName = new String[]{name};
        this.ownerUsage = ownerUsage;
    }

    public UnresolvedTypeInfo getType() {
        return null;
    }
    
    public String[] getQualifiedName() {
        return qualifiedName;
    }
    
    public String getName(){
        return name;
    }
    
    public UnresolvedEntityUsageInfo getOwnerUsage() {
        return ownerUsage;
    }
    
    private UnresolvedVariableUsageInfo resolveAsVariableUsage(BuildDataManager buildDataManager, UnresolvedVariableInfo usedVariable, boolean reference) {

        if (null == usedVariable || usedVariable instanceof UnresolvedFieldInfo){
            //�ϐ����݂���Ȃ��̂ő����ǂ����̃t�B�[���h or ���������ϐ����t�B�[���h������
            UnresolvedFieldUsageInfo usage = new UnresolvedFieldUsageInfo(buildDataManager.getAllAvaliableNames(),ownerUsage,name, reference);
            buildDataManager.addFieldAssignment(usage);
            return usage;
        } else if( usedVariable instanceof UnresolvedParameterInfo) {
            UnresolvedParameterInfo parameter = (UnresolvedParameterInfo) usedVariable;
            return new UnresolvedParameterUsageInfo(parameter, reference);
        } else {
            assert( usedVariable instanceof UnresolvedLocalVariableInfo) : "Illegal state: unexpected VariableInfo";
            UnresolvedLocalVariableInfo localVariable = (UnresolvedLocalVariableInfo) usedVariable;
            return new UnresolvedLocalVariableUsageInfo(localVariable, reference);
        }
    }
    
    public UnresolvedVariableUsageInfo resolveAsAssignmetedVariable(BuildDataManager buildDataManager) {
        UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(name);
        
        return resolveAsVariableUsage(buildDataManager, variable, false);
    }
    
    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager) {
        //���ɉ������Ȃ�
        return this;
    }

    public UnresolvedVariableUsageInfo resolveAsReferencedVariable(BuildDataManager buildDataManager) {
        UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(name);
        
        return resolveAsVariableUsage(buildDataManager, variable, true);
    }
    
    public UnresolvedEntityUsageInfo resolveReferencedEntityIfPossible(BuildDataManager buildDataManager) {
        UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(name);
        if (null != variable){
            return resolveAsVariableUsage(buildDataManager, variable, true);
        } else {
            return null;
        }
    }
    
    
    private final String name;
    private final String[] qualifiedName;
    private final UnresolvedEntityUsageInfo ownerUsage;
}
