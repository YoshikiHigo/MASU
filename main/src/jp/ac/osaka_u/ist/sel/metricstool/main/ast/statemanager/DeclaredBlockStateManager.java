package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �r�W�^�[���N���X�⃁�\�b�h��`���Ȃǂ̃u���b�N�t����`���ɓ��B�������̃r�W�^�[�̏�Ԃ��Ǘ����钊�ۃN���X.
 * <p>
 * �N���X��`���A���\�b�h��`���A���O��Ԓ�`���Ȃǂ̏�ԊǗ��ɗ��p�����.
 * �u���b�N�����̏ꍇ��u���b�N���ł���ɓ���q�ɐ錾���������悤�ȍ\����������.
 * <p>
 * ��ԑJ�ڃp�^�[��
 * �p�^�[��1: ��`�݂̂̏ꍇ
 * INIT --�i��`�m�[�h�̒��ɓ���j--> DEFINITION --�i��`�m�[�h����o��j--> INIT
 * �p�^�[��2: ��`�̌�Ɋ֘A����u���b�N�������ꍇ
 * INIT --�i��`�m�[�h�̒��ɓ���j--> DEFINITION --�i�u���b�N�ɓ���j--> BLOCK --�i�u���b�N����o��j--> DEFINITION --�i��`�m�[�h����o��j--> INIT
 * �p�^�[��3: ����q�ɂȂ�ꍇ
 * INIT --�i��`�m�[�h�̒��ɓ���j--> DEFINITION --�i�u���b�N�ɓ���j--> BLOCK --�i��`�m�[�h�̒��ɓ���j--> DEFINITION --�i�u���b�N�ɓ���j-->
 * BLOCK --> ... --�i�u���b�N����o��j--> DEFINITION --�i��`�m�[�h����o��j--> BLOCK --�i�u���b�N����o��j--> DEFINITION --�i��`�m�[�h����o��j--> INIT
 * <p>
 * ���̃N���X�̃T�u�N���X��5�̒��ۃ��\�b�h {@link #getBlockEnterEventType()}, {@link #getBlockExitEventType()}
 * {@link #getDefinitionEnterEventType()}, {@link #getDefinitionExitEventType()}, {@link #isDefinitionEvent(AstToken)}
 * ���������Ȃ���΂Ȃ�Ȃ��D
 * �܂��K�v�ɉ����āC {@link #isBlockToken(AstToken)}��C�ӂɃI�[�o�[���C�h����K�v������D
 * 
 * @author kou-tngt
 */
public abstract class DeclaredBlockStateManager extends
        StackedAstVisitStateManager<DeclaredBlockStateManager.STATE> {

    /**
     * �r�W�^�[��AST�m�[�h�̒��ɓ��������̃C�x���g�ʒm���󂯎��C
     * ���̃m�[�h����`���₻�̌�ɑ����u���b�N��\�����̂Ȃ�Ώ�Ԃ�J�ڂ��ď�ԕω��C�x���g�𔭍s����D
     * 
     * �ǂ̃m�[�h����`����u���b�N��\�����́C {@link #isDefinitionEvent(AstToken)}�� {@link #isBlockToken(AstToken)}
     * ���I�[�o�[���C�h���Ďw�肷��D
     * 
     * �܂��C��`����u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�̎�ނ́C {@link #getDefinitionEnterEventType()}��
     * {@link #getBlockEnterEventType()}���I�[�o�[���C�h���Ďw�肷��D
     * 
     * @param event AST�r�W�b�g�C�x���g
     */
    @Override
    public void entered(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isStateChangeTriggerEvent(event)) {
            //��ԕω��g���K�Ȃ�

            //��Ԃ��X�^�b�N�֋L�^
            super.entered(event);

            if (this.isDefinitionEvent(event)) {
                //��`�m�[�h�Ȃ��ԑJ�ڂ��ăC�x���g�𔭍s
                this.state = STATE.DECLARE;
                this.fireStateChangeEvent(this.getDefinitionEnterEventType(), event);
            } else if (this.isBlockToken(token) && STATE.DECLARE == this.state) {
                //��`���ɂ����ԂŃu���b�N��\���m�[�h������Ώ�ԑJ�ڂ��ăC�x���g�𔭍s
                this.state = STATE.BLOCK;
                this.fireStateChangeEvent(this.getBlockEnterEventType(), event);
            }
        }
    }

    /**
     * �r�W�^�[��AST�m�[�h����o�����̃C�x���g�ʒm���󂯎��C
     * ���̃m�[�h����`���₻�̌�ɑ����u���b�N��\�����̂Ȃ�Ώ�Ԃ�߂��ď�ԕω��C�x���g�𔭍s����D
     * 
     *�@�ǂ̃m�[�h����`����u���b�N��\�����́C {@link #isDefinitionEvent(AstToken)}�� {@link #isBlockToken(AstToken)}
     * ���I�[�o�[���C�h���Ďw�肷��D
     * 
     * �܂��C��`����u���b�N����o�����ɔ��s�����ԕω��C�x���g�̎�ނ́C {@link #getDefinitionExitEventType()}��
     * {@link #getBlockExitEventType()}���I�[�o�[���C�h���Ďw�肷��D
     * 
     * @param event AST�r�W�b�g�C�x���g
     */
    @Override
    public void exited(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isStateChangeTriggerEvent(event)) {
            //��ԕω��g���K�Ȃ�

            //�X�^�b�N�̈�ԏ�̏�Ԃɖ߂�
            super.exited(event);

            if (this.isDefinitionEvent(event)) {
                //��`�m�[�h�Ȃ�C�x���g�𔭍s
                this.fireStateChangeEvent(this.getDefinitionExitEventType(), event);
            } else if (this.isBlockToken(token) && STATE.DECLARE == this.state) {
                //��`���ɂ����ԂŃu���b�N��\���m�[�h������΃C�x���g�𔭍s
                this.fireStateChangeEvent(this.getBlockExitEventType(), event);
            }
        }
    }

    /**
     * �u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v��Ԃ����ۃ��\�b�h�D
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�C�x���g�^�C�v��C�ӂɐݒ肷�邱�Ƃ��ł���D
     * @return �u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    protected abstract StateChangeEventType getBlockEnterEventType();

    /**
     * �u���b�N�ɂ���o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v��Ԃ����ۃ��\�b�h�D
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�C�x���g�^�C�v��C�ӂɐݒ肷�邱�Ƃ��ł���D
     * @return �u���b�N����o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    protected abstract StateChangeEventType getBlockExitEventType();

    /**
     * ��`�����������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v��Ԃ����ۃ��\�b�h�D
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�C�x���g�^�C�v��C�ӂɐݒ肷�邱�Ƃ��ł���D
     * @return ��`�����������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    protected abstract StateChangeEventType getDefinitionEnterEventType();

    /**
     * ��`������o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v��Ԃ����ۃ��\�b�h�D
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�C�x���g�^�C�v��C�ӂɐݒ肷�邱�Ƃ��ł���D
     * @return ��`������o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    protected abstract StateChangeEventType getDefinitionExitEventType();

    /**
     * �����̃C�x���g���Ή������`����\�����ǂ�����Ԃ����ۃ��\�b�h�D
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�C�ӂ̒�`���ɑΉ�����T�u�N���X���쐬���邱�Ƃ��ł���D
     * 
     * @param event�@��`����\�����ǂ����𒲂ׂ���AST�C�x���g
     * @return ��`����\���g�[�N���ł����true
     */
    protected abstract boolean isDefinitionEvent(AstVisitEvent event);

    /**
     * �����̃g�[�N�����Ή�����u���b�N��\�����ǂ�����Ԃ��D
     * �f�t�H���g�ł�token.isBlock()��p���Ĕ��肷��D
     * ���̃��\�b�h���I�[�o�[���C�h���邱�ƂŁC�C�ӂ̃u���b�N�Ή�����T�u�N���X���쐬���邱�Ƃ��ł���D
     * 
     * @param token�@�u���b�N��\�����ǂ����𒲂ׂ���AST�g�[�N��
     * @return �u���b�N��\���g�[�N���ł����true
     */
    protected boolean isBlockToken(final AstToken token) {
        return token.isBlock();
    }

    /**
     * �r�W�^�[�����ݒ�`�u���b�N���ɂ��邩�ǂ�����Ԃ��D
     * @return �r�W�^�[�����ݒ�`�u���b�N���ɂ���ꍇ��true
     */
    public boolean isInBlock() {
        return STATE.BLOCK == this.state;
    }

    /**
     * �r�W�^�[�����ݒ�`������`�u���b�N�ɂ��邩�ǂ�����Ԃ��D
     * @return �r�W�^�[�����ݒ�`���܂��͒�`�u���b�N�ɂ���ꍇ��true
     */
    public boolean isInDefinition() {
        return STATE.DECLARE == this.state || this.isInBlock();
    }

    /**
     * �r�W�^�[�����ݒ�`���ɂ��邩�ǂ�����Ԃ��D
     * @return�@�r�W�^�[�����ݒ�`���ɂ���ꍇ��true
     */
    public boolean isInPreDeclaration() {
        return STATE.DECLARE == this.state;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#getState()
     */
    @Override
    protected STATE getState() {
        return this.state;
    }

    /**
     * �����ŗ^����ꂽ�C�x���g����ԕω��̃g���K�ɂȂ蓾�邩�ǂ�����Ԃ�.
     * ����event��{@link #isBlockToken(AstToken)}�܂��� {@link #isDefinitionEvent(AstVisitEvent)}�̂ǂ��炩�𖞂�����true��Ԃ��D
     * 
     * @param event ��ԕω��̃g���K�ƂȂ蓾�邩�ǂ����𒲂ׂ�g�[�N��
     * @return ��ԕω��̃g���K�ɂȂ蓾��ꍇ��true
     */
    @Override
    protected final boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return this.isBlockToken(event.getToken()) || this.isDefinitionEvent(event);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StackedAstVisitStateManager#setState(java.lang.Object)
     */
    @Override
    protected void setState(final STATE state) {
        this.state = state;
    };

    /**
     * ��Ԃ�\��Enum
     * 
     * @author kou-tngt
     *
     */
    protected static enum STATE {
        OUT, DECLARE, BLOCK
    }

    /**
     * ���݂̏��
     */
    private STATE state = STATE.OUT;

}
