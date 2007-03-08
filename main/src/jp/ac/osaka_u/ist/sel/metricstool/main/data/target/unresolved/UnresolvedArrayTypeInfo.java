package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������z��^��\�����߂̃N���X�D�ȉ��̏������D
 * <ul>
 * <li>�������^ (UnresolvedTypeInfo)</li>
 * <li>���� (int)</li>
 * </ul>
 * 
 * @author y-higo
 * @see UnresolvedTypeInfo
 */
public final class UnresolvedArrayTypeInfo implements UnresolvedTypeInfo, UnresolvedEntityUsageInfo {

    // /**
    // * ���������ǂ����̃`�F�b�N���s��
    // */
    // public boolean equals(final UnresolvedEntityUsage entityUsage) {
    //
    // if (null == entityUsage) {
    // throw new NullPointerException();
    // }
    //
    // if (!(entityUsage instanceof UnresolvedArrayUsage)) {
    // return false;
    // }
    //
    // final UnresolvedEntityUsage elementTypeInfo = this.getElementType();
    // final UnresolvedEntityUsage correspondElementTypeInfo = ((UnresolvedArrayUsage) entityUsage)
    // .getElementType();
    // if (!elementTypeInfo.equals(correspondElementTypeInfo)) {
    // return false;
    // }
    //
    // final int dimension = this.getDimension();
    // final int correspondDimension = ((UnresolvedArrayUsage) entityUsage).getDimension();
    // return dimension == correspondDimension;
    // }

    /**
     * ���̖������z��g�p�������ς݂��ǂ����Ԃ�
     * 
     * @return �����ς݂̏ꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * �����ςݔz��g�p��Ԃ�
     * 
     * @return �����ςݔz��g�p
     * @throws NotResolvedException �������̏ꍇ�ɃX���[�����
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * �������z��g�p���������C�����ςݎQ�Ƃ�Ԃ��D
     * 
     * @param usingClass �������z��g�p���s���Ă���N���X
     * @param usingMethod �������z��g�p���s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ςݔz��g�p
     */
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        final UnresolvedEntityUsageInfo unresolvedElement = this.getElementType();
        final int dimension = this.getDimension();

        final EntityUsageInfo element = unresolvedElement.resolveEntityUsage(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert element != null : "resolveEntityUsage returned null!";

        // �v�f�̌^���s���̂Ƃ��� UnnownTypeInfo ��Ԃ�
        if (element instanceof UnknownEntityUsageInfo) {
            this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
            return this.resolvedInfo;
        }

        // �v�f�̌^�������ł����ꍇ�͂��̔z��^���쐬���Ԃ�
        this.resolvedInfo = ArrayTypeInfo.getType(element, dimension);
        return this.resolvedInfo;
    }

    /**
     * �z��̗v�f�̖������^��Ԃ�
     * 
     * @return �z��̗v�f�̖������^
     */
    public UnresolvedEntityUsageInfo getElementType() {
        return this.type;
    }

    /**
     * �z��̎�����Ԃ�
     * 
     * @return �z��̎���
     */
    public int getDimension() {
        return this.dimension;
    }

    /**
     * ���̃C���X�^���X���\���z��̎�����1�傫�������z���\���C���X�^���X��Ԃ��D
     * 
     * @return ���̃C���X�^���X���\���z��̎�����1�傫�������z��
     */
    public UnresolvedArrayTypeInfo getDimensionInclementedArrayType() {
        return getType(getElementType(), getDimension() + 1);
    }

    /**
     * UnresolvedArrayTypeInfo �̃C���X�^���X��Ԃ����߂̃t�@�N�g�����\�b�h�D
     * 
     * @param type �������^��\���ϐ�
     * @param dimension ������\���ϐ�
     * @return �������� UnresolvedArrayTypeInfo �I�u�W�F�N�g
     */
    public static UnresolvedArrayTypeInfo getType(final UnresolvedEntityUsageInfo type,
            final int dimension) {

        if (null == type) {
            throw new NullPointerException();
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        final Key key = new Key(type, dimension);
        UnresolvedArrayTypeInfo arrayUsage = ARRAY_TYPE_MAP.get(key);
        if (arrayUsage == null) {
            arrayUsage = new UnresolvedArrayTypeInfo(type, dimension);
            ARRAY_TYPE_MAP.put(key, arrayUsage);
        }

        return arrayUsage;
    }

    /**
     * �������z��^�I�u�W�F�N�g�̏��������s���D�z��̗v�f�̖������^�Ɣz��̎������^�����Ȃ���΂Ȃ�Ȃ�
     * 
     * @param type �z��̗v�f�̖������^
     * @param dimension �z��̎���
     */
    private UnresolvedArrayTypeInfo(final UnresolvedEntityUsageInfo type, final int dimension) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new NullPointerException();
        }
        if (1 > dimension) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        this.type = type;
        this.dimension = dimension;
        this.resolvedInfo = null;
    }

    /**
     * �z��̗v�f�̌^��ۑ�����ϐ�
     */
    private final UnresolvedEntityUsageInfo type;

    /**
     * �z��̎�����ۑ�����ϐ�
     */
    private final int dimension;

    /**
     * �����ςݔz��g�p��ۑ����邽�߂̕ϐ�
     */
    private EntityUsageInfo resolvedInfo;

    /**
     * UnresolvedArrayTypeInfo �I�u�W�F�N�g���ꌳ�Ǘ����邽�߂� Map�D�I�u�W�F�N�g�̓t�@�N�g�����\�b�h�Ő��������D
     */
    private static final Map<Key, UnresolvedArrayTypeInfo> ARRAY_TYPE_MAP = new HashMap<Key, UnresolvedArrayTypeInfo>();

    /**
     * �ϐ��̌^�Ǝ�����p���ăL�[�ƂȂ�N���X�D
     * 
     * @author y-higo
     */
    static class Key {

        /**
         * ���L�[
         */
        private final UnresolvedEntityUsageInfo type;

        /**
         * ���L�[
         */
        private final int dimension;

        /**
         * ���C���L�[����C�L�[�I�u�W�F�N�g�𐶐�����
         * 
         * @param type ���L�[
         * @param dimension ���L�[
         */
        Key(final UnresolvedEntityUsageInfo type, final int dimension) {

            if (null == type) {
                throw new NullPointerException();
            }
            if (1 > dimension) {
                throw new IllegalArgumentException("Array dimension must be 1 or more!");
            }

            this.type = type;
            this.dimension = dimension;
        }

        /**
         * ���̃I�u�W�F�N�g�̃n�b�V���R�[�h��Ԃ��D
         */
        @Override
        public int hashCode() {
            return this.type.hashCode() + this.dimension;
        }

        /**
         * ���̃L�[�I�u�W�F�N�g�̑��L�[��Ԃ��D
         * 
         * @return ���L�[
         */
        public UnresolvedEntityUsageInfo getFirstKey() {
            return this.type;
        }

        /**
         * ���̃L�[�I�u�W�F�N�g�̑��L�[��Ԃ��D
         * 
         * @return ���L�[
         */
        public int getSecondKey() {
            return this.dimension;
        }

        /**
         * ���̃I�u�W�F�N�g�ƈ����Ŏw�肳�ꂽ�I�u�W�F�N�g������������Ԃ��D
         */
        @Override
        public boolean equals(Object o) {

            if (null == o) {
                throw new NullPointerException();
            }

            final UnresolvedEntityUsageInfo firstKey = this.getFirstKey();
            final UnresolvedEntityUsageInfo correspondFirstKey = ((Key) o).getFirstKey();
            if (!firstKey.equals(correspondFirstKey)) {
                return false;
            }

            final int secondKey = this.getSecondKey();
            final int correspondSecondKey = ((Key) o).getSecondKey();
            return secondKey == correspondSecondKey;
        }
    }
}
