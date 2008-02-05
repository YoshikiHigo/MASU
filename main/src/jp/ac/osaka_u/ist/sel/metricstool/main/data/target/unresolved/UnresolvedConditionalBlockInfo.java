package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������������t���u���b�N����\���N���X
 * 
 * @author t-miyake
 *
 */

// TODO ConditionalBlockInfo���ǉ����ꂽ��T��ConditionalBlockInfo���p�����ׂ�
public abstract class UnresolvedConditionalBlockInfo<T extends /*Conditional*/BlockInfo> extends UnresolvedBlockInfo<T> {

	/**
	 * ��������������Ԃ�
	 * @return ������������
	 */
	public UnresolvedConditionalClauseInfo getConditionalSpace() {
		return this.conditionalSpace;
	}

	/**
	 * ���������������Z�b�g����
	 * @param conditionalSpace ������������
	 */
	public void setConditionalSpace(UnresolvedConditionalClauseInfo conditionalSpace) {
		// �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == conditionalSpace) {
            throw new IllegalArgumentException("conditionalSpace is null.");
        }
        
		this.conditionalSpace = conditionalSpace;
	}
	
	/**
	 * ��������������ۑ����邽�߂̕ϐ�
	 */
	private UnresolvedConditionalClauseInfo conditionalSpace;

	
}
