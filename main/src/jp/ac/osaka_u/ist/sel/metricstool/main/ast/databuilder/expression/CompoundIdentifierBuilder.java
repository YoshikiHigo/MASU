package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;

/**
 * 
 * 
 * @author kou-tngt
 */
public class CompoundIdentifierBuilder extends ExpressionBuilder{

    /**
     * @param expressionManager
     */
    public CompoundIdentifierBuilder(ExpressionElementManager expressionManager, BuildDataManager buildManager) {
        super(expressionManager);
        this.buildDataManager = buildManager;
    }
    
    protected void afterExited(AstVisitEvent event){
        
        AstToken token = event.getToken();
        if (token.isNameSeparator()){
            buildCompoundIdentifierElement();
        }
    }
    
    protected void buildCompoundIdentifierElement(){
        ExpressionElement[] elements = getAvailableElements();
        
        if (elements.length == 2){
            ExpressionElement left = elements[0];
            ExpressionElement right = elements[1];
            
            if (right instanceof SingleIdentifierElement){
                //�E���͒P��̎��ʎq�̂͂�
                
                SingleIdentifierElement rightIdentifier = (SingleIdentifierElement)right;
                String rightName = rightIdentifier.getName();
                
                UnresolvedTypeInfo leftElementType = null;
                
                if (left instanceof FieldOrMethodElement){
                    IdentifierElement leftIdentifier = (IdentifierElement)left;
                    leftElementType = leftIdentifier.resolveAsReferencedVariable(buildDataManager);
                }
                else if (left instanceof SingleIdentifierElement){
                    //�������P��̎��ʎq�Ȃ�A�����͕ϐ���������Ȃ�
                    SingleIdentifierElement leftIdentifier = (SingleIdentifierElement)left;
                    String leftName = leftIdentifier.getName();
                    UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(leftName);
                    
                    if (null != variable){
                        //�X�R�[�v���ɕϐ����݂�����
                        if (variable instanceof UnresolvedFieldInfo){
                            //���̓t�B�[���h�ł���
                            leftElementType = new UnresolvedFieldUsage(buildDataManager.getAvailableNameSpaceSet(),
                                    buildDataManager.getCurrentClass(),leftName);
                        } else {
                            leftElementType = variable.getType();
                        }
                    }
                } else if (left.equals(InstanceSpecificElement.THIS)){
                    //������this�Ȃ�E���͂��̃N���X�̃t�B�[���h�������\�b�h��
                    if (null != buildDataManager){
                        leftElementType = buildDataManager.getCurrentClass();
                    }
                } else {
                    leftElementType = left.getType();
                }
                
                if (null != leftElementType){
                    //�����̌^������ł����̂ŉE���̓t�B�[���h�������\�b�h�����낤
                    pushElement(new FieldOrMethodElement(leftElementType,rightName));
                } else if (left instanceof IdentifierElement){
                    //�����̌^��������Ȃ��̂őS�̂��Ȃ񂩂悭������񎯕ʎq�Ƃ��Ĉ���
                    pushElement(new CompoundIdentifierElement((IdentifierElement)left,rightName));
                } else {
                    assert(false) : "Illegal state: unknown left element type.";
                }
            }
            else {
                assert(false) : "Illegal state: unexpected element type.";
            }
        } else {
            assert(false) : "Illegal state: two elements must be available.";
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isNameSeparator();
    }
    
    private final BuildDataManager buildDataManager;
}
