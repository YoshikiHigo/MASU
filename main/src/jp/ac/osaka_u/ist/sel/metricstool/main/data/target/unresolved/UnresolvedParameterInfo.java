package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ������\�����߂̃N���X�D �^��񋟂���̂݁D
 * 
 * @author higo
 * 
 */
public final class UnresolvedParameterInfo extends UnresolvedVariableInfo<TargetParameterInfo> {

    /**
     * �����I�u�W�F�N�g������������D���O�ƌ^���K�v�D
     * 
     * @param name ������
     * @param type �����̌^
     */
    public UnresolvedParameterInfo(final String name, final UnresolvedTypeInfo type) {
        super(name, type);
    }

    /**
     * ���������������������C�����ςݎQ�Ƃ�Ԃ��D
     * 
     * @param usingClass �������������̒�`���s���Ă���N���X
     * @param usingMethod �������������̒�`���s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ς݈������
     */
    @Override
    public TargetParameterInfo resolveUnit(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // �C���q�C�p�����[�^���C�^�C�ʒu�����擾
        final Set<ModifierInfo> parameterModifiers = this.getModifiers();
        final String parameterName = this.getName();
        final UnresolvedTypeInfo unresolvedParameterType = this.getType();
        TypeInfo parameterType = unresolvedParameterType.resolveType(usingClass, usingMethod,
                classInfoManager, null, null);
        assert parameterType != null : "resolveTypeInfo returned null!";
        if (parameterType instanceof UnknownTypeInfo) {
            if (unresolvedParameterType instanceof UnresolvedClassReferenceInfo) {

                // TODO �^�p�����[�^�̏����i�[����
                final ExternalClassInfo externalClass = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedParameterType);
                parameterType = new ClassTypeInfo(externalClass);
                classInfoManager.add(externalClass);

            } else if (unresolvedParameterType instanceof UnresolvedArrayTypeInfo) {

                // TODO �^�p�����[�^�̏����i�[����
                final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedParameterType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedParameterType)
                        .getDimension();
                final TypeInfo elementType = unresolvedElementType.resolveType(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                parameterType = ArrayTypeInfo.getType(elementType, dimension);
            } else {
                assert false : "Can't resolve dummy parameter type : "
                        + unresolvedParameterType.toString();
            }
        }
        final int parameterFromLine = this.getFromLine();
        final int parameterFromColumn = this.getFromColumn();
        final int parameterToLine = this.getToLine();
        final int parameterToColumn = this.getToColumn();

        // �p�����[�^�I�u�W�F�N�g�𐶐�����
        this.resolvedInfo = new TargetParameterInfo(parameterModifiers, parameterName,
                parameterType, parameterFromLine, parameterFromColumn, parameterToLine,
                parameterToColumn);
        return this.resolvedInfo;
    }

}
