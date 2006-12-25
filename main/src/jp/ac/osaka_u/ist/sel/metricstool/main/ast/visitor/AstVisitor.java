package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;

import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;

/**
 * �C�ӂ̍\����AST��K�₷��r�W�^�[�̃C���^�t�F�[�X.
 * 
 * ���̃C���^�t�F�[�X����������N���X�́CAST�m�[�h�ւ̓��B�������C���̃m�[�h�̓����ɓ��鎞�C���̃m�[�h�̓�������o�鎞�ɁC
 * �o�^���ꂽ {@link AstVisitListener} �ɑ΂��ēK�؂ȃC�x���g�𔭍s����.
 * 
 * @author kou-tngt
 *
 * @param <T>�@�K�₷��AST�m�[�h�̌^
 */
public interface AstVisitor<T> {
    public void addVisitListener(AstVisitListener listener);
    public void enter();
    public void exit();
    public boolean isVisited(T node);
    public void removeVisitListener(AstVisitListener listener);
    public void reset();
    public void setPositionManager(PositionManager lineColumn);
    public void visit(T node);
}
