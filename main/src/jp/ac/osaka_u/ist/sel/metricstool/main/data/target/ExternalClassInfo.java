package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * �O���N���X����\���N���X
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class ExternalClassInfo
        extends
        ClassInfo<ExternalFieldInfo, ExternalMethodInfo, ExternalConstructorInfo, ExternalInnerClassInfo> {

    /**
     * ���O��Ԗ��ƃN���X����^���āC�I�u�W�F�N�g��������
     * 
     * @param namespace ���O��Ԗ�
     * @param className �N���X��
     */
    public ExternalClassInfo(final NamespaceInfo namespace, final String className) {

        super(new HashSet<ModifierInfo>(), namespace, className, false, true, true, true, true,
                false, 0, 0, 0, 0);
    }

    /**
     * ���S���薼�ƃA�N�Z�X����q��^���āC�N���X���I�u�W�F�N�g��������
     * 
     * @param fullQualifiedName ���S���薼
     */
    public ExternalClassInfo(final String[] fullQualifiedName, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final boolean isInterface) {

        super(new HashSet<ModifierInfo>(), fullQualifiedName, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance, isInterface, 0, 0, 0, 0);
    }

    /**
     * ���S���薼��^���āC�N���X���I�u�W�F�N�g��������
     * 
     * @param fullQualifiedName ���S���薼
     */
    public ExternalClassInfo(final String[] fullQualifiedName) {

        super(new HashSet<ModifierInfo>(), fullQualifiedName, false, true, true, true, true, false,
                0, 0, 0, 0);
    }

    /**
     * ���O��Ԃ��s���ȊO���N���X�̃I�u�W�F�N�g��������
     * 
     * @param className �N���X��
     */
    public ExternalClassInfo(final String className) {

        super(new HashSet<ModifierInfo>(), NamespaceInfo.UNKNOWN, className, false, true, true,
                true, true, false, 0, 0, 0, 0);
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public void addTypeParameterUsage(TypeParameterInfo typeParameterInfo, TypeInfo usedType) {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Map<TypeParameterInfo, TypeInfo> getTypeParameterUsages() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        throw new CannotUseException();
    }

    /**
     * ExternalClassInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        throw new CannotUseException();
    }

    /**
     * �s���ȊO���N���X��\�����߂̒萔
     */
    public static final ExternalClassInfo UNKNOWN = new ExternalClassInfo("UNKNOWN");
}
