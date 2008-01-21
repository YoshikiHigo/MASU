package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;

public class SingleIdentifierElement implements IdentifierElement{
    
    public SingleIdentifierElement(String name, UnresolvedTypeInfo owner){
        this.name = name;
        this.qualifiedName = new String[]{name};
        this.owner = owner;
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
    
    public UnresolvedTypeInfo getOwnerType() {
        return owner;
    }
    
    public UnresolvedTypeInfo resolveAsAssignmetedVariable(BuildDataManager buildDataManager) {
        UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(name);
        
        if (null == variable || variable instanceof UnresolvedFieldInfo){
            //�ϐ����݂���Ȃ��̂ő����ǂ����̃t�B�[���h or ���������ϐ����t�B�[���h������
            UnresolvedFieldUsageInfo usage = new UnresolvedFieldUsageInfo(buildDataManager.getAllAvaliableNames(),owner,name);
            buildDataManager.addFieldAssignment(usage);
            return usage;
        } else {
            //����or���[�J���ϐ�����������
            return variable.getType();
        }
    }
    
    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager) {
        //���ɉ������Ȃ�
        return this;
    }

    public UnresolvedTypeInfo resolveAsReferencedVariable(BuildDataManager buildDataManager) {
        UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(name);
        
        if (null == variable || variable instanceof UnresolvedFieldInfo){
            //�ϐ����݂���Ȃ��̂ő����ǂ����̃t�B�[���h or ���������ϐ����t�B�[���h������
            UnresolvedFieldUsageInfo usage = new UnresolvedFieldUsageInfo(buildDataManager.getAllAvaliableNames(),owner,name);
            buildDataManager.addFieldReference(usage);
            return usage;
        } else {
            return variable.getType();
        }
    }
    
    public UnresolvedTypeInfo resolveReferencedEntityIfPossible(BuildDataManager buildDataManager) {
        UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(name);
        if (null != variable){
            if (variable instanceof UnresolvedFieldInfo){
                UnresolvedFieldUsageInfo fieldUsage = new UnresolvedFieldUsageInfo(buildDataManager.getAllAvaliableNames(),owner,name);
                buildDataManager.addFieldReference(fieldUsage);
                return fieldUsage;
            } else {
                return variable.getType();
            }
        } else {
            return null;
        }
    }
    
    
    private final String name;
    private final String[] qualifiedName;
    private final UnresolvedTypeInfo owner;
}
