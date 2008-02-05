package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class MethodStateManager extends CallableUnitStateManager {

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
