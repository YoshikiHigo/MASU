package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolved�ȕϐ��̋��ʂȐe�N���X.
 * <ul>
 * <li>�ϐ���</li>
 * <li>�^</li>
 * <li>�C���q</li>
 * <li>�ʒu���</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public abstract class UnresolvedVariableInfo<TVar extends VariableInfo, TUnit extends UnresolvedUnitInfo<? extends UnitInfo>>
        extends UnresolvedUnitInfo<TVar> implements ModifierSetting {

    /**
     * �ϐ�����Ԃ�
     * 
     * @return �ϐ���
     */
    public final String getName() {
        return this.name;
    }

    /**
     * �ϐ������Z�b�g����
     * 
     * @param name �ϐ���
     */
    public final void setName(final String name) {

        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * �ϐ��̌^��Ԃ�
     * 
     * @return �ϐ��̌^
     */
    public final UnresolvedTypeInfo getType() {
        return this.type;
    }

    /**
     * �ϐ��̌^���Z�b�g����
     * 
     * @param type �ϐ��̌^
     */
    public final void setType(final UnresolvedTypeInfo type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * �C���q�� Set ��Ԃ�
     * 
     * @return �C���q�� Set
     */
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }
    
    public final TUnit getDefinitionUnit() {
        return this.definitionUnit;
    }

    /**
     * �C���q��ǉ�����
     * 
     * @param modifier �ǉ�����C���q
     */
    public final void addModifier(final ModifierInfo modifier) {

        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * ���O�������ꂽ����Ԃ�
     * 
     * @return ���O�������ꂽ���
     */
    @Override
    public final TVar getResolvedUnit() {
        return this.resolvedInfo;
    }

    /**
     * ���ɖ��O�������ꂽ���ǂ�����Ԃ�
     * 
     * @return ���O��������Ă���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * �ϐ��I�u�W�F�N�g������������D
     * 
     * @param name �ϐ���
     * @param type �ϐ��̌^
     * @param definitionUnit �錾���Ă�����
     */
    UnresolvedVariableInfo(final String name, final UnresolvedTypeInfo type, final TUnit definitionUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == name) || (null == type) || (null == definitionUnit)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.type = type;
        this.modifiers = new HashSet<ModifierInfo>();
        this.definitionUnit = definitionUnit;
    }

    /**
     * �ϐ��I�u�W�F�N�g������������D
     * 
     * @param �錾���Ă�����
     */
    UnresolvedVariableInfo(final TUnit definitionUnit) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        
        if(null == definitionUnit) {
            throw new IllegalArgumentException();
        }
        
        this.name = null;
        this.type = null;
        this.modifiers = new HashSet<ModifierInfo>();
        this.definitionUnit = definitionUnit;
        
        this.resolvedInfo = null;
    }

    /**
     * �ϐ�����\���ϐ�
     */
    private String name;

    /**
     * �ϐ��̌^��\���ϐ�
     */
    private UnresolvedTypeInfo type;

    /**
     * ���̃t�B�[���h�̏C���q��ۑ����邽�߂̕ϐ�
     */
    private Set<ModifierInfo> modifiers;
    
    private final TUnit definitionUnit;

    /**
     * ���O�������ꂽ�����i�[���邽�߂̕ϐ�
     */
    protected TVar resolvedInfo;
}
