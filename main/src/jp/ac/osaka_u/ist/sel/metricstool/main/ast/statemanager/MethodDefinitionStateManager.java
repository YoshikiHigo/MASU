package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * ���\�b�h��`���Ƃ��̌�̃u���b�N�ɑ΂���r�W�^�[�̏�Ԃ��Ǘ����C��ԑJ�ڃC�x���g�𔭍s����N���X�D
 * 
 * @author kou-tngt
 *
 */
public class MethodDefinitionStateManager extends DeclaredBlockStateManager {

    /**
     * ���s�����ԑJ�ڃC�x���g�̎�ނ�\��Enum
     * 
     * @author kou-tngt
     *
     */
    public static enum METHOD_STATE_CHANGE implements StateChangeEventType {
        ENTER_METHOD_DEF, EXIT_METHOD_DEF,

        ENTER_METHOD_BLOCK, EXIT_METHOD_BLOCK, ;
    }

    /**
     * ���\�b�h��`���ɑ����u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���\�b�h��`���ɑ����u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return METHOD_STATE_CHANGE.ENTER_METHOD_BLOCK;
    }

    /**
     * ���\�b�h��`���ɑ����u���b�N����o�����ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���\�b�h��`���ɑ����u���b�N����o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return METHOD_STATE_CHANGE.EXIT_METHOD_BLOCK;
    }

    /**
     * ���\�b�h��`���ɓ��������ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���\�b�h��`���ɂɓ��������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return METHOD_STATE_CHANGE.ENTER_METHOD_DEF;
    }

    /**
     * ���\�b�h��`������o�����ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���\�b�h��`������o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return METHOD_STATE_CHANGE.EXIT_METHOD_DEF;
    }

    /**
     * �����̃g�[�N�������\�b�h��`����\�����ǂ�����Ԃ��D
     * token.isMethodDefinition()���\�b�h��p���Ĕ��肷��D
     * 
     * @param token�@���\�b�h��`����\�����ǂ����𒲂ׂ���AST�g�[�N��
     * @return ���\�b�h��`����\���g�[�N���ł����true
     */
    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isMethodDefinition();
    }

}
