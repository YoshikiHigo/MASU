package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������z��R���X�g���N�^�Ăяo����\���N���X
 * 
 * @author higo
 *
 */
public class UnresolvedArrayConstructorCallInfo extends
        UnresolvedConstructorCallInfo<UnresolvedArrayTypeInfo, ArrayConstructorCallInfo> {

    /**
     * �z��R���X�g���N�^�Ăяo�������s�����Q�ƌ^��^���ăI�u�W�F�N�g��������
     * 
     * @param unresolvedArrayType �R���X�g���N�^�Ăяo�������s�����^
     */
    public UnresolvedArrayConstructorCallInfo(final UnresolvedArrayTypeInfo unresolvedArrayType) {

        super(unresolvedArrayType);

        this.indexExpression = null;
    }

    /**
     * �z��R���X�g���N�^�Ăяo�������s�����^�ƈʒu����^���ď�����
     * @param unresolvedArrayType �R���X�g���N�^�Ăяo�������s�����^
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public UnresolvedArrayConstructorCallInfo(final UnresolvedArrayTypeInfo unresolvedArrayType,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        this(unresolvedArrayType);
        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * ���O�������s��
     */
    @Override
    public ArrayConstructorCallInfo resolve(final TargetClassInfo usingClass,
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
        final UnresolvedTypeInfo<?> unresolvedArrayType = this.getReferenceType();
        final TypeInfo arrayType = unresolvedArrayType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert arrayType instanceof ArrayTypeInfo : "Illegal Type was found";

        // �C���f�b�N�X�̎�������
        final UnresolvedExpressionInfo<?> unresolvedIndexExpression = this.getIndexExpression();
        final ExpressionInfo indexExpression = null != unresolvedIndexExpression ? unresolvedIndexExpression
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager)
                : new EmptyExpressionInfo(usingMethod, fromLine, fromColumn + 1, fromLine,
                        fromColumn + 1);

        this.resolvedInfo = new ArrayConstructorCallInfo((ArrayTypeInfo) arrayType,
                indexExpression, usingMethod, fromLine, fromColumn, toLine, toColumn);
        this.resolvedInfo.addArguments(actualParameters);
        this.resolvedInfo.addTypeArguments(typeArguments);
        return this.resolvedInfo;
    }

    /**
     * �C���f�b�N�X�̎����Z�b�g
     * 
     * @param indexExpression
     */
    public void setIndexExpression(
            final UnresolvedExpressionInfo<? extends ExpressionInfo> indexExpression) {
        this.indexExpression = indexExpression;
    }

    /**
     * �C���f�b�N�X�̎����擾
     * 
     * @return �C���f�b�N�X�̎�
     */
    public UnresolvedExpressionInfo<? extends ExpressionInfo> getIndexExpression() {
        return this.indexExpression;
    }

    /**
     * �C���f�b�N�X�̎���ۑ����邽�߂̕ϐ�
     */
    private UnresolvedExpressionInfo<? extends ExpressionInfo> indexExpression;
}
