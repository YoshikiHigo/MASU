package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * ����\���C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public interface ExpressionInfo extends ConditionInfo {

    /**
     * ���̎��̌^��Ԃ�
     * 
     * @return ���̎��̌^
     */
    TypeInfo getType();

    /**
     * ����ExecutableElement�̒��ڂ̃I�[�i�[�ł���ExecutableElement��Ԃ�
     * 
     * @return ����ExecutableElement�̒��ڂ̃I�[�i�[�ł���ExecutableElement
     */
    ExecutableElementInfo getOwnerExecutableElement();

    void setOwnerExecutableElement(ExecutableElementInfo ownerExecutableElement);

    /**
     * ����ExecutableElement�̃I�[�i�[�ł���StatementInfo��Ԃ�
     * 
     * @return ����ExecutableElement�̃I�[�i�[�ł���StatementInfo
     */
    StatementInfo getOwnerStatement();
}
