package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;

/**
 * ���ۍ\���؂̃r�W�^�[���ǂ̂悤�ȏ�Ԃɂ��邩���Ǘ�����C���^�t�F�[�X.
 * 
 * @author kou-tngt
 *
 */
public interface AstVisitStateManager extends StateManager<AstVisitEvent>, AstVisitListener{

}
