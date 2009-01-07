package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableLengthTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������ϒ��^��\���N���X
 * 
 * @author higo
 *
 */
public class UnresolvedVariableLengthTypeInfo extends UnresolvedArrayTypeInfo {

    /**
     * UnresolvedVariableLengthTypeInfo �̃C���X�^���X��Ԃ����߂̃t�@�N�g�����\�b�h�D
     * 
     * @param type �������^��\���ϐ�
     * @return �������� UnresolvedVariableLengthTypeInfo �I�u�W�F�N�g
     */
    public static UnresolvedVariableLengthTypeInfo getType(
            final UnresolvedTypeInfo<? extends TypeInfo> type) {

        if (null == type) {
            throw new IllegalArgumentException();
        }

        UnresolvedVariableLengthTypeInfo variableLengthUsage = VARIABLE_LENGTH_TYPE_MAP.get(type);
        if (variableLengthUsage == null) {
            variableLengthUsage = new UnresolvedVariableLengthTypeInfo(type);
            VARIABLE_LENGTH_TYPE_MAP.put(type, variableLengthUsage);
        }

        return variableLengthUsage;
    }

    UnresolvedVariableLengthTypeInfo(final UnresolvedTypeInfo<? extends TypeInfo> type) {
        super(type, 1);
    }

    /**
     * �������z��^���������C�����ςݔz��^��Ԃ��D
     * 
     * @param usingClass �������z��^�����݂���N���X
     * @param usingMethod �������z��^�����݂��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ςݔz��^
     */
    @Override
    public ArrayTypeInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new IllegalArgumentException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        final UnresolvedTypeInfo<?> unresolvedElementType = this.getElementType();
        final TypeInfo elementType = unresolvedElementType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert null != elementType : "resolveEntityUsage returned null!";

        // �v�f�̌^���s���̂Ƃ��� UnnownTypeInfo ��Ԃ�
        if (elementType instanceof UnknownTypeInfo) {
            this.resolvedInfo = VariableLengthTypeInfo.getType(UnknownTypeInfo.getInstance());
            return this.resolvedInfo;

            // �v�f�̌^�������ł����ꍇ�͂��̔z��^���쐬���Ԃ�
        } else {
            this.resolvedInfo = VariableLengthTypeInfo.getType(elementType);
            return this.resolvedInfo;
        }
    }

    /**
     * UnresolvedVariableLengthTypeInfo �I�u�W�F�N�g���ꌳ�Ǘ����邽�߂� Map�D�I�u�W�F�N�g�̓t�@�N�g�����\�b�h�Ő��������D
     */
    private static final Map<UnresolvedTypeInfo<?>, UnresolvedVariableLengthTypeInfo> VARIABLE_LENGTH_TYPE_MAP = new HashMap<UnresolvedTypeInfo<?>, UnresolvedVariableLengthTypeInfo>();
}
