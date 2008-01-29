package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import java.util.Collections;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * �����������o(���\�b�h�C�R���X�g���N�^)�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author t-miyake
 *
 */
public abstract class UnresolvedMemberCallInfo  implements UnresolvedEntityUsageInfo {

	/**
     * ���̖��������\�b�h�Ăяo�������łɉ�������Ă��邩�ǂ�����Ԃ�
     * 
     * @return ���ɉ�������Ă���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * �����ς݃��\�b�h�Ăяo������Ԃ�
     * 
     * @return �����ς݃��\�b�h�Ăяo�����
     * @throw ��������Ă��Ȃ��ꍇ�ɃX���[�����
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }
    
    
    
    /**
     * �^�p�����[�^�g�p��ǉ�����
     * 
     * @param typeParameterUsage �ǉ�����^�p�����[�^�g�p
     */
    public void addTypeArgument(final UnresolvedReferenceTypeInfo typeParameterUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeParameterUsage);
    }

    /**
     * ������ǉ�
     * 
     * @param typeInfo
     */
    public void addParameter(final UnresolvedEntityUsageInfo typeInfo) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeInfo) {
            throw new NullPointerException();
        }

        this.parameterTypes.add(typeInfo);
    }

    /**
     * ������ List ��Ԃ�
     * 
     * @return ������ List
     */
    public List<UnresolvedEntityUsageInfo> getParameters() {
        return Collections.unmodifiableList(this.parameterTypes);
    }

    /**
     * �^�p�����[�^�g�p�� List ��Ԃ�
     * 
     * @return �^�p�����[�^�g�p�� List
     */
    public List<UnresolvedReferenceTypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }
    
    /**
     * ���\�b�h����Ԃ�
     * 
     * @return ���\�b�h��
     */
    public String getMemberName() {
        return this.memberName;
    }
    
    /**
     * �����o����ۑ����邽�߂̕ϐ�
     */
    protected String memberName;

    /**
     * �^�p�����[�^�g�p��ۑ����邽�߂̕ϐ�
     */
    protected List<UnresolvedReferenceTypeInfo> typeArguments;

    /**
     * ������ۑ����邽�߂̕ϐ�
     */
    protected List<UnresolvedEntityUsageInfo> parameterTypes;
    
    /**
     * �����ς݃��\�b�h�Ăяo������ۑ����邽�߂̕ϐ�
     */
    protected EntityUsageInfo resolvedInfo;
    
    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    protected static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "UnresolvedMethodCall";
        }
    }, MESSAGE_TYPE.ERROR);
}
