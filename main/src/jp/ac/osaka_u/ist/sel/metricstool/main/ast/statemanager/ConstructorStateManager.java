package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class ConstructorStateManager extends CallableUnitStateManager {

    /**
     * �����̃g�[�N�����R���X�g���N�^��`����\�����ǂ�����Ԃ��D
     * token.isConstructorDefinition()���\�b�h��p���Ĕ��肷��D
     * 
     * @param token�@�R���X�g���N�^��`����\�����ǂ����𒲂ׂ���AST�g�[�N��
     * @return �R���X�g���N�^��`����\���g�[�N���ł����true
     */
    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isConstructorDefinition();
    }

}
