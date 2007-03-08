package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������z��ɑ΂���v�f�̎Q�Ƃ�\�����߂̃N���X�D�ȉ��̏������D
 * 
 * @author kou-tngt
 * @see UnresolvedEntityUsageInfo
 */
public class UnresolvedArrayElementUsageInfo implements UnresolvedEntityUsageInfo {

    /**
     * �v�f���Q�Ƃ��ꂽ�z��̌^��^����.
     * 
     * @param ownerArrayType �v�f���Q�Ƃ��ꂽ�z��̌^
     */
    public UnresolvedArrayElementUsageInfo(final UnresolvedEntityUsageInfo ownerArrayType) {

        if (null == ownerArrayType) {
            throw new NullPointerException("ownerArrayType is null.");
        }

        this.ownerArrayType = ownerArrayType;
        this.resolvedInfo = null;
    }

    /**
     * �������z��v�f�̎Q�Ƃ��������C�����ςݎQ�Ƃ�Ԃ��D
     * 
     * @param usingClass �������z��v�f�Q�Ƃ��s���Ă���N���X
     * @param usingMethod �������z��v�f�Q�Ƃ��s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return �����ςݎQ��
     */
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        // �v�f�g�p���������Ă��関��`�^���擾
        final UnresolvedEntityUsageInfo unresolvedOwnerUsage = this.getOwnerArrayType();
        EntityUsageInfo ownerUsage = unresolvedOwnerUsage.resolveEntityUsage(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert ownerUsage != null : "resolveEntityUsage returned null!";

        // �������^�̖��O�������ł��Ȃ������ꍇ
        if (ownerUsage.getType() instanceof UnknownTypeInfo) {

            // �������^���z��^�ł���ꍇ�́C�^���쐬����
            if (unresolvedOwnerUsage instanceof UnresolvedArrayTypeInfo) {
                final UnresolvedEntityUsageInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedOwnerUsage)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedOwnerUsage).getDimension();
                final ExternalClassInfo externalClassInfo = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedElementType);
                classInfoManager.add(externalClassInfo);
                ownerUsage = ArrayTypeInfo.getType(new ClassReferenceInfo(externalClassInfo),
                        dimension);

                // �z��^�ȊO�̏ꍇ�͂ǂ����悤���Ȃ�
            } else {

                usingMethod.addUnresolvedUsage(this);
                this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                return this.resolvedInfo;
            }
        }

        // �z��̎����ɉ����Č^�𐶐�
        final int ownerArrayDimension = ((ArrayTypeInfo) ownerUsage).getDimension();
        final EntityUsageInfo ownerArrayElement = ((ArrayTypeInfo) ownerUsage).getElement();

        // �z�񂪓񎟌��ȏ�̏ꍇ�́C����������Ƃ����z���Ԃ��C�z�񂪈ꎟ���̏ꍇ�͗v�f�̌^��Ԃ�
        this.resolvedInfo = 1 < ownerArrayDimension ? ArrayTypeInfo.getType(ownerArrayElement,
                ownerArrayDimension - 1) : ownerArrayElement;
        return this.resolvedInfo;
    }

    /**
     * ���̖������z��v�f�̎Q�Ƃ������ς݂��ǂ����Ԃ�
     * 
     * @return �����ς݂̏ꍇ�� true, �����łȂ��ꍇ�� false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * ���̖������z��v�f�̎Q�Ƃ̉����ςݗv�f��Ԃ�
     * 
     * @return �����ςݗv�f
     * @throws NotResolvedException ��������Ԃł��̃��\�b�h���Ă΂ꂽ�ꍇ�ɃX���[�����
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * �������z��v�f�g�p�̌^����Ԃ�
     * 
     * @return �������z��v�f�g�p�̌^��
     */
    public String getTypeName() {
        final String ownerTypeName = this.getOwnerArrayType().getTypeName();
        return "An element usage of " + ownerTypeName;
    }

    /**
     * �v�f���Q�Ƃ��ꂽ�z��̌^��Ԃ�
     * 
     * @return �v�f���Q�Ƃ��ꂽ�z��̌^
     */
    public UnresolvedEntityUsageInfo getOwnerArrayType() {
        return this.ownerArrayType;
    }

    /**
     * �v�f���Q�Ƃ��ꂽ�z��̌^
     */
    private final UnresolvedEntityUsageInfo ownerArrayType;

    /**
     * �����ςݔz��v�f�g�p��ۑ����邽�߂̕ϐ�
     */
    private EntityUsageInfo resolvedInfo;

}
