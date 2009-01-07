package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������R���X�g���N�^�Ăяo����ۑ����邽�߂̃N���X
 * 
 * @author t-miyake, higo
 *
 */
public class UnresolvedConstructorCallInfo extends UnresolvedCallInfo<ConstructorCallInfo> {

    /**
     * �R���X�g���N�^�Ăяo�������s�����Q�ƌ^��^���ăI�u�W�F�N�g��������
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

    /**
     * �R���X�g���N�^�Ăяo�������s�����Q�ƌ^��^���ď�����
     * @param unresolvedReferenceType �R���X�g���N�^�Ăяo�������s�����^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedConstructorCallInfo(
            final UnresolvedReferenceTypeInfo<?> unresolvedReferenceType, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        this(unresolvedReferenceType);

        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * ���O�������s��
     */
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
        final UnresolvedTypeInfo<?> unresolvedReferenceType = this.getReferenceType();
        final TypeInfo referenceType = unresolvedReferenceType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert referenceType instanceof ClassTypeInfo : "Illegal type was found";

        final List<TargetConstructorInfo> constructors = NameResolver
                .getAvailableConstructors((ClassTypeInfo) referenceType);

        for (final ConstructorInfo constructor : constructors) {

            if (constructor.canCalledWith(actualParameters)) {
                this.resolvedInfo = new ConstructorCallInfo((ReferenceTypeInfo) referenceType,
                        constructor, usingMethod, fromLine, fromColumn, toLine, toColumn);
                this.resolvedInfo.addArguments(actualParameters);
                this.resolvedInfo.addTypeArguments(typeArguments);
                return this.resolvedInfo;
            }
        }

        // �ΏۃN���X�ɒ�`���ꂽ�R���X�g���N�^�ŊY��������̂��Ȃ��̂ŁC�O���N���X�ɒ�`���ꂽ�R���X�g���N�^���Ăяo���Ă��邱�Ƃɂ���
        {
            ClassInfo classInfo = ((ClassTypeInfo) referenceType).getReferencedClass();
            if (classInfo instanceof TargetClassInfo) {
                classInfo = NameResolver.getExternalSuperClass((TargetClassInfo) classInfo);
            }
            final ExternalConstructorInfo constructor = new ExternalConstructorInfo(classInfo);
            this.resolvedInfo = new ConstructorCallInfo((ReferenceTypeInfo) referenceType,
                    constructor, usingMethod, fromLine, fromColumn, toLine, toColumn);
            this.resolvedInfo.addArguments(actualParameters);
            this.resolvedInfo.addTypeArguments(typeArguments);
            return this.resolvedInfo;
        }
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
