package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public abstract class CallableUnitInfo extends LocalSpaceInfo {

    /**
     * �I�u�W�F�N�g������������
     * 
     * @param ownerClass ���L�N���X
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    CallableUnitInfo(final ClassInfo ownerClass, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerClass, fromLine, fromColumn, toLine, toColumn);

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.unresolvedUsage = new HashSet<UnresolvedEntityUsageInfo>();
    }

    /**
     * �����Ŏw�肳�ꂽ�^�p�����[�^��ǉ�����
     * 
     * @param typeParameter �ǉ�����^�p�����[�^
     */
    public void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * ���̃N���X�̌^�p�����[�^�� List ��Ԃ��D
     * 
     * @return ���̃N���X�̌^�p�����[�^�� List
     */
    public List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * ���̌Ăяo�����j�b�g���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo����ǉ�����D �v���O�C������ĂԂƃ����^�C���G���[�D
     * 
     * @param entityUsage ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo��
     */
    public void addUnresolvedUsage(final UnresolvedEntityUsageInfo entityUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == entityUsage) {
            throw new NullPointerException();
        }

        this.unresolvedUsage.add(entityUsage);
    }

    /**
     * ���̌Ăяo�����j�b�g���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���� Set ��Ԃ��D
     * 
     * @return ���̃��\�b�h���ŁC���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���� Set
     */
    public Set<UnresolvedEntityUsageInfo> getUnresolvedUsages() {
        return Collections.unmodifiableSet(this.unresolvedUsage);
    }

    /**
     * �^�p�����[�^��ۑ�����ϐ�
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * ���O�����ł��Ȃ������N���X�Q�ƁC�t�B�[���h�Q�ƁE����C���\�b�h�Ăяo���Ȃǂ�ۑ����邽�߂̕ϐ�
     */
    private final Set<UnresolvedEntityUsageInfo> unresolvedUsage;
}
