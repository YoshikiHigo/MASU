package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import java.util.EventListener;


/**
 * ���ۍ\���؂̃r�W�^�[���ʒm����C�x���g���󂯎��C���^�t�F�[�X.
 * 
 * �C�ӂ̃m�[�ha�ɂ���,{@link #visited(AstVisitEvent)},{@link #entered(AstVisitEvent)},{@link #exited(AstVisitEvent)}
 * �̏��Ԃɒʒm�����.
 * 
 * @author kou-tngt
 *
 */
public interface AstVisitListener extends EventListener {
    /**
     * ���钸�_�ɓ��B�����C�x���g���󂯎��.
     * @param e ���B�C�x���g
     */
    public void visited(AstVisitEvent e);

    /**
     * ���钸�_�̒��ɓ���C�x���g���󂯎��.
     * @param e
     */
    public void entered(AstVisitEvent e);

    /**
     * ���钸�_�̊O�ɏo���C�x���g���󂯎��
     * @param e
     */
    public void exited(AstVisitEvent e);
}
