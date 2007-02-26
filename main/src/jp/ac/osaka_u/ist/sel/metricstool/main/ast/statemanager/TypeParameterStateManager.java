package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �r�W�^�[���^�p�����[�^��`���֏o���肷��ۂ̏�Ԃ��Ǘ�����X�e�[�g�}�l�[�W���D
 * @author kou-tngt
 *
 */
public class TypeParameterStateManager extends StackedAstVisitStateManager<TypeParameterStateManager.STATE> {

    /**
     * ���M�����ԕω��C�x���g�̎�ނ�\��enum
     * @author kou-tngt
     *
     */
    public enum TYPE_PARAMETER implements StateChangeEventType {
        ENTER_TYPE_PARAMETER_DEF, EXIT_TYPE_PARAMETER_DEF,
        ENTER_TYPE_UPPER_BOUNDS,EXIT_TYPE_UPPER_BOUNDS,
        ENTER_TYPE_LOWER_BOUNDS, EXIT_TYPE_LOWER_BOUNDS
    }
    
    /**
     * �^�p�����[�^��`���̃m�[�h�ɓ��������ɌĂяo����C
     * ��ԕω��������N�����āC��ԕω��C�x���g��ʒm����D
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    public void entered(AstVisitEvent event){
        super.entered(event);
        
        AstToken token = event.getToken();
        
        if (token.isTypeParameterDefinition()){
            this.state = STATE.IN_PARAMETER_DEF;
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_PARAMETER_DEF, event);
        } else if (token.isTypeLowerBoundsDescription()){
            this.state = STATE.IN_LOWER_BOUNDS;
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_LOWER_BOUNDS, event);
        } else if (token.isTypeUpperBoundsDescription()){
            this.state = STATE.IN_UPPER_BOUNDS;
            fireStateChangeEvent(TYPE_PARAMETER.ENTER_TYPE_UPPER_BOUNDS, event);            
        }
    }
    
    /**
     * �^�p�����[�^��`���̃m�[�h����o�����ɌĂяo����C
     * ��ԕω��������N�����āC��ԕω��C�x���g��ʒm����D
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent)
     */
    public void exited(AstVisitEvent event){
        super.exited(event);
        
        AstToken token = event.getToken();
        
        if (token.isTypeParameterDefinition()){
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_PARAMETER_DEF, event);
        } else if (token.isTypeLowerBoundsDescription()){
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_LOWER_BOUNDS, event);
        } else if (token.isTypeUpperBoundsDescription()){
            fireStateChangeEvent(TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS, event);
        }
    }

    /**
     * �^�p�����[�^��`���Ɋ֘A����m�[�h���ǂ����𔻒肷��
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#isStateChangeTriggerToken(jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken)
     */
    @Override
    protected boolean isStateChangeTriggerToken(final AstToken token) {
        return token.isTypeParameterDefinition() || token.isTypeLowerBoundsDescription()
            || token.isTypeUpperBoundsDescription();
    }

    /**
     * ���݂̏�Ԃ�Ԃ��D
     * @return ���݂̏�ԁD
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#getState()
     */
    @Override
    protected STATE getState() {
        return this.state;
    }

    /**
     * ���� state�@��p���ď�Ԃ𕜌�����D
     * @param state ����������
     * 
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#setState(java.lang.Object)
     */
    @Override
    protected void setState(final STATE state) {
        this.state = state;
    }
    
    /**
     * ��Ԃ�\��enum
     * 
     * @author kou-tngt
     *
     */
    protected enum STATE{
        OUT,IN_PARAMETER_DEF,IN_UPPER_BOUNDS,IN_LOWER_BOUNDS
    }
    
    /**
     * ���݂̏��
     */
    private STATE state = STATE.OUT;
}
