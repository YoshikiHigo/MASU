package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;

public class MethodCallBuilder extends ExpressionBuilder{

    /**
     * @param expressionManager
     */
    public MethodCallBuilder(ExpressionElementManager expressionManager, BuildDataManager buildDataManager) {
        super(expressionManager);
        this.buildDataManager = buildDataManager;
    }

    @Override
    protected void afterExited(AstVisitEvent event){
        AstToken token = event.getToken();
        if (token.isMethodCall()){
            buildMethodCall(event);
        }
    }
    
    protected void buildMethodCall(final AstVisitEvent event){
        ExpressionElement[] elements = getAvailableElements();
        
        if (elements.length > 0){
            if (elements[0] instanceof IdentifierElement){
                IdentifierElement callee = (IdentifierElement)elements[0];
                
                callee = callee.resolveAsCalledMethod(this.buildDataManager);
                
                final UnresolvedMethodCallInfo methodCall = new UnresolvedMethodCallInfo(callee.getOwnerUsage(),callee.getName());
                // �J�n�ʒu�̓��\�b�h���̏o���ʒu
                methodCall.setFromLine(callee.getFromLine());
                methodCall.setFromColumn(callee.getFromColumn());
                // �I���ʒu�̓��\�b�h�Ăяo�����̏I���ʒu
                methodCall.setToLine(event.getEndLine());
                methodCall.setToColumn(event.getEndColumn());
                
                for(int i=1; i < elements.length; i++){
                    ExpressionElement argment = elements[i];
                    if (argment instanceof IdentifierElement){
                        methodCall.addParameter(((IdentifierElement)argment).resolveAsReferencedVariable(this.buildDataManager));
                    } else if (argment.equals(InstanceSpecificElement.THIS)){
                        methodCall.addParameter(InstanceSpecificElement.getThisInstanceType(this.buildDataManager));
                    } else if (argment instanceof TypeArgumentElement) {
                    	// TODO C#�Ȃǂ̏ꍇ�͌^�����ɎQ�ƌ^�ȊO���w��ł���̂őΏ����K�v����
                        TypeArgumentElement typeArgument = (TypeArgumentElement) argment;
                    	assert typeArgument.getType() instanceof UnresolvedReferenceTypeInfo : "type argument was not reference type.";
                        methodCall.addTypeArgument((UnresolvedReferenceTypeInfo) typeArgument.getType());
                    } else {
                        methodCall.addParameter(argment.getUsage());
                    }
                }
                
                pushElement(UsageElement.getInstance(methodCall));
                this.buildDataManager.addMethodCall(methodCall);
            }
            
        } else {
            assert(false) : "Illegal state: callee element was not found.";
        }
    }
    
    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isMethodCall();
    }
    
    private final BuildDataManager buildDataManager;

}
