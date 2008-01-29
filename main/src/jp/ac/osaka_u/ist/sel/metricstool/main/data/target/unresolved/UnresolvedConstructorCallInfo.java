package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import java.util.LinkedList;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * �������R���X�g���N�^�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author t-miyake
 *
 */
public final class UnresolvedConstructorCallInfo extends UnresolvedMemberCallInfo {


    /**
     * �R���X�g���N�^�Ăяo�������s�����Q�ƌ^�Ɩ��O��^���ăI�u�W�F�N�g��������
     * 
     * @param type �R���X�g���N�^�Ăяo�������s�����^
     * @param callName �Ăяo���ꂽ�Ƃ��̖��O
     */
    public UnresolvedConstructorCallInfo(final UnresolvedReferenceTypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new IllegalArgumentException();
        }
        this.type = type;
        this.memberName = type.getTypeName();
        this.typeArguments = new LinkedList<UnresolvedReferenceTypeInfo>();
        this.parameterTypes = new LinkedList<UnresolvedEntityUsageInfo>();

        this.resolvedInfo = null;
    }

	public EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass,
			TargetMethodInfo usingMethod, ClassInfoManager classInfoManager,
			FieldInfoManager fieldInfoManager,
			MethodInfoManager methodInfoManager) {
		// TODO ��������Ƃ�����
		return null;
	}
	
	public UnresolvedReferenceTypeInfo getType() {
		return this.type;
	}
	
	private final UnresolvedReferenceTypeInfo type;
    
    
}
