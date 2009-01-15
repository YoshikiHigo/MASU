package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * �N���X�C���\�b�h�C�t�B�[���h�Ȃǂ̏C���q��\���N���X�D���݈ȉ��́C�C���q��������
 * <ul>
 * <li>public</li>
 * <li>private</li>
 * <li>virtual(abstract)
 * <li>
 * </ul>
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class ModifierInfo implements Serializable {

    /**
     * abstract ��\���萔
     */
    public static final String ABSTRACT_STRING = "abstract";

    /**
     * final ��\���萔
     */
    public static final String FINAL_STRING = "final";

    /**
     * private ��\���萔
     */
    public static final String PRIVATE_STRING = "private";

    /**
     * protected ��\���萔
     */
    public static final String PROTECTED_STRING = "protected";

    /**
     * public ��\���萔
     */
    public static final String PUBLIC_STRING = "public";

    /**
     * static ��\���萔
     */
    public static final String STATIC_STRING = "static";

    /**
     * virtual ��\���萔
     */
    public static final String VIRTUAL_STRING = "virtual";

    /**
     * abstract ��\���萔
     */
    public static final ModifierInfo ABSTRACT = new ModifierInfo(ABSTRACT_STRING);

    /**
     * final ��\���萔
     */
    public static final ModifierInfo FINAL = new ModifierInfo(FINAL_STRING);

    /**
     * private ��\���萔
     */
    public static final ModifierInfo PRIVATE = new ModifierInfo(PRIVATE_STRING);

    /**
     * protected ��\���萔
     */
    public static final ModifierInfo PROTECTED = new ModifierInfo(PROTECTED_STRING);

    /**
     * public ��\���萔
     */
    public static final ModifierInfo PUBLIC = new ModifierInfo(PUBLIC_STRING);

    /**
     * static ��\���萔
     */
    public static final ModifierInfo STATIC = new ModifierInfo(STATIC_STRING);

    /**
     * virtual ��\���萔
     */
    public static final ModifierInfo VIRTUAL = new ModifierInfo(VIRTUAL_STRING);

    /**
     * �C���q������C�C���q�I�u�W�F�N�g�𐶐�����t�@�N�g�����\�b�h
     * 
     * @param name �C���q��
     * @return �C���q�I�u�W�F�N�g
     */
    public static ModifierInfo getModifierInfo(final String name) {

        ModifierInfo requiredModifier = MODIFIERS.get(name);
        if (null == requiredModifier) {
            requiredModifier = new ModifierInfo(name);
            MODIFIERS.put(name, requiredModifier);
        }

        return requiredModifier;
    }

    /**
     * �C���q����^���āC�I�u�W�F�N�g��������
     * 
     * @param name
     */
    private ModifierInfo(final String name) {
        this.name = name;
    }

    /**
     * �C���q����Ԃ�
     * 
     * @return �C���q��
     */
    public String getName() {
        return this.name;
    }

    /**
     * �������� ModifierInfo �I�u�W�F�N�g��ۑ����Ă������߂̒萔
     */
    private static final Map<String, ModifierInfo> MODIFIERS = new HashMap<String, ModifierInfo>();

    static {
        MODIFIERS.put(ABSTRACT_STRING, ABSTRACT);
        MODIFIERS.put(FINAL_STRING, FINAL);
        MODIFIERS.put(PRIVATE_STRING, PRIVATE);
        MODIFIERS.put(PROTECTED_STRING, PROTECTED);
        MODIFIERS.put(PUBLIC_STRING, PUBLIC);
        MODIFIERS.put(STATIC_STRING, STATIC);
        MODIFIERS.put(VIRTUAL_STRING, VIRTUAL);
    }

    /**
     * �C���q����ۑ����邽�߂̕ϐ�
     */
    private final String name;
}
