package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * ��`�����u���b�N�̏�Ԃ��Ǘ����钊�ۃN���X.
 * <p>
 * �N���X��`���A���\�b�h��`���A���O��Ԓ�`���Ȃǂ̏�ԊǗ��ɗ��p�����.
 * �u���b�N�����̏ꍇ��u���b�N���ł���ɓ���q�ɐ錾���������悤�ȍ\��������.
 * <p>
 * ��ԑJ�ڃp�^�[��
 * �p�^�[��1: ��`�݂̂̏ꍇ
 * INIT --�i��`�m�[�h�̒��ɓ���j--> DEFINITION --�i��`�m�[�h����o��j--> INIT
 * �p�^�[��2: ��`�̌�Ɋ֘A����u���b�N�������ꍇ
 * INIT --�i��`�m�[�h�̒��ɓ���j--> DEFINITION --�i�u���b�N�ɓ���j--> BLOCK --�i�u���b�N����o��j--> DEFINITION --�i��`�m�[�h����o��j--> INIT
 * �p�^�[��3: ����q�ɂȂ�ꍇ
 * INIT --�i��`�m�[�h�̒��ɓ���j--> DEFINITION --�i�u���b�N�ɓ���j--> BLOCK --�i��`�m�[�h�̒��ɓ���j--> DEFINITION --�i�u���b�N�ɓ���j-->
 * BLOCK --> ... --�i�u���b�N����o��j--> DEFINITION --�i��`�m�[�h����o��j--> BLOCK --�i�u���b�N����o��j--> DEFINITION --�i��`�m�[�h����o��j--> INIT
 * 
 * @author kou-tngt
 */
public abstract class DeclaredBlockStateManager extends
        StackedAstVisitStateManager<DeclaredBlockStateManager.STATE> {

    @Override
    public void entered(final AstVisitEvent event) {
        //��Ԃ��X�^�b�N�֋L�^
        super.entered(event);

        final AstToken token = event.getToken();

        if (this.isDefinitionToken(token)) {
            this.state = STATE.DECLARE;
            this.fireStateChangeEvent(this.getDefinitionEnterEventType(),event);
        } else if (this.isBlockToken(token) && STATE.DECLARE == this.state) {
            this.state = STATE.BLOCK;
            this.fireStateChangeEvent(this.getBlockEnterEventType(),event);
        }
    }

    @Override
    public void exited(final AstVisitEvent event) {
        //�X�^�b�N�̈�ԏ�̏�Ԃɖ߂�
        super.exited(event);

        final AstToken token = event.getToken();

        if (this.isDefinitionToken(token)) {
            this.fireStateChangeEvent(this.getDefinitionExitEventType(),event);
        } else if (this.isBlockToken(token) && STATE.DECLARE == this.state) {
            this.fireStateChangeEvent(this.getBlockExitEventType(),event);
        }
    }

    protected abstract StateChangeEventType getBlockEnterEventType();

    protected abstract StateChangeEventType getBlockExitEventType();

    protected abstract StateChangeEventType getDefinitionEnterEventType();

    protected abstract StateChangeEventType getDefinitionExitEventType();

    protected abstract boolean isDefinitionToken(AstToken token);

    protected boolean isBlockToken(final AstToken token) {
        return token.isBlock();
    }

    public boolean isInBlock() {
        return STATE.BLOCK == this.state;
    }

    public boolean isInDefinition() {
        return STATE.DECLARE == this.state || this.isInBlock();
    }

    public boolean isInPreDeclaration() {
        return STATE.DECLARE == this.state;
    }

    @Override
    protected STATE getState() {
        return this.state;
    }

    @Override
    protected boolean isStateChangeTriggerToken(final AstToken token) {
        return isBlockToken(token)|| this.isDefinitionToken(token);
    }

    @Override
    protected void setState(final STATE state) {
        this.state = state;
    };

    protected static enum STATE {
        INIT, DECLARE, BLOCK
    }

    private STATE state = STATE.INIT;

}
