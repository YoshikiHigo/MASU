package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������R���X�g���N�^�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedConstructorCallInfo extends UnresolvedCallInfo<ConstructorCallInfo> {

    /**
     * �R���X�g���N�^�Ăяo�������s�����Q�ƌ^�Ɩ��O��^���ăI�u�W�F�N�g��������
     * 
     * @param unresolvedReferenceType �R���X�g���N�^�Ăяo�������s�����^
     */
    public UnresolvedConstructorCallInfo(
            final UnresolvedReferenceTypeInfo<?> unresolvedReferenceType) {

        if (null == unresolvedReferenceType) {
            throw new IllegalArgumentException();
        }

        this.unresolvedReferenceType = unresolvedReferenceType;
    }

    @Override
    public ConstructorCallInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        //�@�ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �R���X�g���N�^�̃V�O�l�`�����擾
        final List<ExpressionInfo> actualParameters = super.resolveArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final List<ReferenceTypeInfo> typeArguments = super.resolveTypeArguments(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //�@�R���X�g���N�^�̌^������
        final TypeInfo referenceType = this.getReferenceType().resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        if (!(referenceType instanceof ReferenceTypeInfo)) {
            assert false : "Error handling must be inserted!";
        }

        // �v�f�g�p�̃I�[�i�[�v�f��Ԃ�
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);

        this.resolvedInfo = new ConstructorCallInfo(ownerExecutableElement,
                (ReferenceTypeInfo) referenceType, fromLine, fromColumn, toLine, toColumn);
        this.resolvedInfo.addArguments(actualParameters);
        this.resolvedInfo.addTypeArguments(typeArguments);
        return this.resolvedInfo;
    }

    /**
     * ���̖������R���X�g���N�^�Ăяo���̌^��Ԃ�
     * 
     * @return ���̖������R���X�g���N�^�Ăяo���̌^
     */
    public UnresolvedReferenceTypeInfo<?> getReferenceType() {
        return this.unresolvedReferenceType;
    }

    private final UnresolvedReferenceTypeInfo<?> unresolvedReferenceType;

}
