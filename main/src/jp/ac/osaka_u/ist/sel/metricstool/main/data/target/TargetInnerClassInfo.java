package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Arrays;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;


/**
 * �C���i�[�N���X��\���N���X�D
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public class TargetInnerClassInfo extends TargetClassInfo implements InnerClassInfo {

    public static ClassInfo getOutestClass(final InnerClassInfo innerClass) {

        if (null == innerClass) {
            throw new IllegalArgumentException();
        }

        final String[] fqName = ((ClassInfo) innerClass).getFullQualifiedName();
        final String[] outerFQName = Arrays.copyOf(fqName, fqName.length - 1);

        final ClassInfo outerClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                outerFQName);
        return outerClass instanceof InnerClassInfo ? getOutestClass((InnerClassInfo) outerClass)
                : outerClass;
    }

    /**
     * �C���i�[�N���X�I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q���� Set
     * @param namespace ���O���
     * @param className �N���X��
     * @param outerClass �O���̃N���X
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param isInterface �C���^�t�F�[�X���ǂ���
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final TargetClassInfo outerClass, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance, final boolean isInterface,
            final FileInfo fileInfo, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(modifiers, namespace, className, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance, isInterface, fileInfo, fromLine,
                fromColumn, toLine, toColumn);

        if (null == outerClass) {
            throw new IllegalArgumentException();
        }

        this.outerClass = outerClass;
    }

    /**
     * �C���i�[�N���X�I�u�W�F�N�g������������
     * 
     * @param modifiers �C���q���� Set
     * @param fullQualifiedName ���S���薼
     * @param outerClass �O���̃N���X
     * @param privateVisible �N���X������̂ݎQ�Ɖ\
     * @param namespaceVisible �������O��Ԃ���Q�Ɖ\
     * @param inheritanceVisible �q�N���X����Q�Ɖ\
     * @param publicVisible �ǂ�����ł��Q�Ɖ\
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param isInterface �C���^�t�F�[�X���ǂ���
     * @param fileInfo ���̃N���X��錾���Ă���t�@�C�����
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetInnerClassInfo(final Set<ModifierInfo> modifiers,
            final String[] fullQualifiedName, final TargetClassInfo outerClass,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final boolean isInterface, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, fullQualifiedName, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, instance, isInterface, fileInfo, fromLine, fromColumn, toLine,
                toColumn);

        if (null == outerClass) {
            throw new IllegalArgumentException();
        }

        this.outerClass = outerClass;
    }

    /**
     * �O���̃��j�b�g��Ԃ�
     * 
     * @return �O���̃��j�b�g
     */
    @Override
    public UnitInfo getOuterUnit() {
        return this.getOuterClass();
    }

    /**
     * �O���̃N���X��Ԃ�.
     * �܂�CgetOuterUnit �̕Ԃ�l��TargetClassInfo�ł���ꍇ�́C���̃I�u�W�F�N�g��Ԃ��C
     * �Ԃ�l���CTargetMethodInfo�ł���ꍇ�́C���̃I�u�W�F�N�g�� ownerClass ��Ԃ��D
     * 
     * @return�@�O���̃N���X
     */
    @Override
    public final ClassInfo getOuterClass() {
        return this.outerClass;
    }

    @Override
    public TypeParameterizable getOuterTypeParameterizableUnit() {
        return this.getOuterClass();
    }

    /**
     * �O���̃N���X��ۑ�����ϐ�
     */
    private final TargetClassInfo outerClass;
}
