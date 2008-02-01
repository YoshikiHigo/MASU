package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �����������o(���\�b�h�C�R���X�g���N�^)�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 *
 */
public abstract class UnresolvedMemberCallInfo implements UnresolvedEntityUsageInfo {

    public UnresolvedMemberCallInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.typeArguments = new LinkedList<UnresolvedClassTypeInfo>();
        this.parameterTypes = new LinkedList<UnresolvedEntityUsageInfo>();

        this.resolvedInfo = null;

    }

    /**
     * ���̖��������\�b�h�Ăяo�������łɉ�������Ă��邩�ǂ�����Ԃ�
     * 
     * @return ���ɉ�������Ă���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * �����ς݃��\�b�h�Ăяo������Ԃ�
     * 
     * @return �����ς݃��\�b�h�Ăяo�����
     * @throw ��������Ă��Ȃ��ꍇ�ɃX���[�����
     */
    public final EntityUsageInfo getResolvedEntityUsage() {

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
    public final void addTypeArgument(final UnresolvedClassTypeInfo typeParameterUsage) {

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
    public final void addParameter(final UnresolvedEntityUsageInfo typeInfo) {

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
    public final List<UnresolvedEntityUsageInfo> getParameters() {
        return Collections.unmodifiableList(this.parameterTypes);
    }

    /**
     * �^�p�����[�^�g�p�� List ��Ԃ�
     * 
     * @return �^�p�����[�^�g�p�� List
     */
    public final List<UnresolvedClassTypeInfo> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * 
     * @param unresolvedParameters
     * @return
     */
    protected final List<EntityUsageInfo> resolveParameters(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        //�@�����ςݎ��������i�[���邽�߂̕ϐ�
        final List<EntityUsageInfo> parameters = new LinkedList<EntityUsageInfo>();

        for (final UnresolvedEntityUsageInfo unresolvedParameter : this.getParameters()) {

            EntityUsageInfo parameter = unresolvedParameter.resolveEntityUsage(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            assert parameter != null : "resolveEntityUsage returned null!";

            if (parameter instanceof UnknownEntityUsageInfo) {

                // �N���X�Q�Ƃ������ꍇ
                if (unresolvedParameter instanceof UnresolvedClassReferenceInfo) {

                    // TODO �^�p�����[�^�̏����i�[����                    
                    final ExternalClassInfo externalClassInfo = NameResolver
                            .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedParameter);
                    classInfoManager.add(externalClassInfo);
                    final ClassTypeInfo referenceType = new ClassTypeInfo(externalClassInfo);
                    parameter = new ClassReferenceInfo(referenceType);

                } else {
                    assert false : "Here shouldn't be reached!";
                }
            }
            parameters.add(parameter);
        }

        return parameters;
    }



    /**
     * �^�p�����[�^�g�p��ۑ����邽�߂̕ϐ�
     */
    protected List<UnresolvedClassTypeInfo> typeArguments;

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
