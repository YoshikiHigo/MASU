package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;



/**
 * �O���N���X�ɒ�`����Ă���t�B�[���h�̏���ۑ����邽�߂̃N���X�D
 * 
 * @author higo
 */
public final class ExternalFieldInfo extends FieldInfo {

    /**
     * ���O�ƒ�`���Ă���N���X����^���ď������D �^�͕s���D
     * 
     * @param name �t�B�[���h��
     * @param definitionClass �t�B�[���h���`���Ă���N���X
     */
    public ExternalFieldInfo(final String name, final ClassInfo definitionClass) {
        super(new HashSet<ModifierInfo>(), name, UnknownTypeInfo.getInstance(), definitionClass, 0,
                0, 0, 0);
    }

    /**
     * ���O��^���ď������D��`���Ă���N���X���s���ȏꍇ�ɗp����D
     * 
     * @param name �t�B�[���h��
     */
    public ExternalFieldInfo(final String name) {
        super(new HashSet<ModifierInfo>(), name, UnknownTypeInfo.getInstance(),
                ExternalClassInfo.UNKNOWN, 0, 0, 0, 0);
    }

    /**
     * ExternalFieldInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getFromLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalFieldInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getFromColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalFieldInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getToLine() {
        throw new CannotUseException();
    }

    /**
     * ExternalFieldInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public int getToColumn() {
        throw new CannotUseException();
    }

    /**
     * ExternalFieldInfo �ł͗��p�ł��Ȃ�
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        throw new CannotUseException();
    }
}
