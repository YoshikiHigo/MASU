package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * �r�W�^�[���N���X�̐e�N���X�L�q���ɓ��B�������ɏ�ԑJ�ڂ��C
 * ��ԕω��C�x���g�𔭍s����X�e�[�g�}�l�[�W��
 * @author kou-tngt
 *
 */
public class InheritanceDefinitionStateManager extends EnterExitStateManager {

    /**
     * ���s�����ԑJ�ڃC�x���g�̃C�x���g�^�C�v��\��enum
     * @author kou-tngt
     *
     */
    public static enum INHERITANCE_STATE implements StateChangeEventType {
        ENTER_INHERITANCE_DEF, EXIT_INHERITANCE_DEF
    }

    /**
     * �e�N���X�L�q���̒��ɓ��������̏�ԕω��C�x���g�̎�ނ�Ԃ��D
     * @return�@�e�N���X�L�q���̒��ɓ��������̏�ԕω��C�x���g�̎��
     */
    @Override
    public StateChangeEventType getEnterEventType() {
        return INHERITANCE_STATE.ENTER_INHERITANCE_DEF;
    }

    /**
     * �e�N���X�L�q������o�����̏�ԕω��C�x���g�̎�ނ�Ԃ��D
     * @return�@�e�N���X�L�q������o�����̏�ԕω��C�x���g�̎��
     */
    @Override
    public StateChangeEventType getExitEventType() {
        return INHERITANCE_STATE.EXIT_INHERITANCE_DEF;
    }

    /**
     * �����ŗ^����ꂽ�g�[�N�����C���q�L�q����\�����ǂ�����Ԃ�.
     * ����ɂ�token.isInheritanceDescription()���\�b�h��p����D
     * 
     * @param token �e�N���X�L�q����\�����ǂ����𒲂ׂ�g�[�N��
     * @return �e�N���X�L�q����\���ꍇ��true
     */
    @Override
    protected boolean isStateChangeTriggerToken(final AstToken token) {
        return token.isInheritanceDescription();
    }

}
