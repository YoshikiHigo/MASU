package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * �O���N���X�ɒ�`����Ă���t�B�[���h�̏���ۑ����邽�߂̃N���X�D
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ExternalFieldInfo extends FieldInfo {

    /**
     * ���O�C�^�C��`���Ă���N���X����^���ď������D 
     * 
     * @param name �t�B�[���h��
     * @param type �^
     * @param definitionClass �t�B�[���h���`���Ă���N���X
     */
    public ExternalFieldInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final ExternalClassInfo definitionClass,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance) {
        super(modifiers, name, type, definitionClass, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance, new Random().nextInt(), new Random()
                        .nextInt(), new Random().nextInt(), new Random().nextInt());
    }

    /**
     * ���O�ƒ�`���Ă���N���X����^���ď������D �^�͕s���D
     * 
     * @param name �t�B�[���h��
     * @param definitionClass �t�B�[���h���`���Ă���N���X
     */
    public ExternalFieldInfo(final String name, final ExternalClassInfo definitionClass) {
        super(new HashSet<ModifierInfo>(), name, UnknownTypeInfo.getInstance(), definitionClass,
                true, true, true, true, true, new Random().nextInt(), new Random().nextInt(),
                new Random().nextInt(), new Random().nextInt());
    }

    /**
     * ���O��^���ď������D��`���Ă���N���X���s���ȏꍇ�ɗp����D
     * 
     * @param name �t�B�[���h��
     */
    public ExternalFieldInfo(final String name) {
        super(new HashSet<ModifierInfo>(), name, UnknownTypeInfo.getInstance(),
                ExternalClassInfo.UNKNOWN, true, true, true, true, true, new Random().nextInt(),
                new Random().nextInt(), new Random().nextInt(), new Random().nextInt());
    }
}
