package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * {@link AstVisitor} ���ǂ̂悤��AST�̃m�[�h��K�₷�邩�𐧌䂷��C���^�t�F�[�X.
 * 
 * @author kou-tngt
 *
 * @param <T> �r�W�^�[���K�₷��AST�̊e�m�[�h�̌^
 */
public interface AstVisitStrategy<T> {

    /**
     * �r�W�^�[�����ɖK�₷�ׂ��m�[�h��I�����C���̑I���ɉ����� {@link AstVisitor#visit(T)}�C
     * {@link AstVisitor#enter()}�C {@link AstVisitor#exit()}��3�̃��\�b�h��K�؂ɌĂяo��.
     * 
     * @param visitor �K�����w������Ώۂ̃r�W�^�[
     * @param node �r�W�^�[�����ݓ��B���Ă���m�[�h
     * @param token �r�W�^�[�����ݓ��B���Ă���m�[�h�̎�ނ�\���g�[�N��
     */
    public void guide(AstVisitor<T> visitor, T node, AstToken token);
}
