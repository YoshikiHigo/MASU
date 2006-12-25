package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;


/**
 * {@link AstVisitor} �ւ̑���Ɛݒ���Ǘ�����C���^�t�F�[�X.
 * 
 * @author kou-tngt
 *
 * @param <T>�@�Ǘ�����r�W�^�[���K�₷��AST�̃m�[�h�̌^
 */
public interface AstVisitorManager<T> {

    /**
     * ����node���\���m�[�h����Ǘ�����r�W�^�[�̖K����J�n����.
     * 
     * @param node�@�r�W�^�[�̖K����J�n����m�[�h
     */
    public void visitStart(T node);

    /**
     * AST�m�[�h�̃\�[�X�R�[�h��̈ʒu�����Ǘ����� {@link PositionManager} ���Z�b�g����.
     * 
     * @param position�@�Z�b�g����PositionManager
     */
    public void setPositionManager(PositionManager position);
}