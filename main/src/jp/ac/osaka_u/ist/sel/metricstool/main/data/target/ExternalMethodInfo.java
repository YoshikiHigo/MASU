package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * �O���N���X�ɒ�`����Ă��郁�\�b�h����ۑ����邽�߂̃N���X
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class ExternalMethodInfo extends MethodInfo {

    /**
     * �O���N���X�ɒ�`����Ă��郁�\�b�h�I�u�W�F�N�g������������
     * �A�N�Z�X����q�܂ŕ������Ă���ꍇ
     *  
     * @param methodName ���\�b�h��
     * @param ownerClass ���̃��\�b�h���`���Ă���N���X
     */
    public ExternalMethodInfo(final Set<ModifierInfo> modifiers, final String methodName,
            final ExternalClassInfo ownerClass, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance) {

        super(modifiers, methodName, ownerClass, privateVisible, namespaceVisible,
                inheritanceVisible, publicVisible, instance, getDummyPosition(),
                getDummyPosition(), getDummyPosition(), getDummyPosition());

        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * �O���N���X�ɒ�`����Ă��郁�\�b�h�I�u�W�F�N�g������������
     * 
     * @param methodName ���\�b�h��
     * @param ownerClass ���̃��\�b�h���`���Ă���N���X
     */
    public ExternalMethodInfo(final String methodName, final ExternalClassInfo ownerClass) {

        super(new HashSet<ModifierInfo>(), methodName, ownerClass, false, true, true, true, true,
                getDummyPosition(), getDummyPosition(), getDummyPosition(), getDummyPosition());

        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * �O���N���X�ɒ�`����Ă��郁�\�b�h�I�u�W�F�N�g������������D
     * ��`���Ă���N���X���s���ȏꍇ�ɗp����R���X�g���N�^
     * 
     * @param methodName ���\�b�h��
     */
    public ExternalMethodInfo(final String methodName) {

        super(new HashSet<ModifierInfo>(), methodName, ExternalClassInfo.UNKNOWN, false, true,
                true, true, true, getDummyPosition(), getDummyPosition(), getDummyPosition(),
                getDummyPosition());
        this.setReturnType(UnknownTypeInfo.getInstance());
    }

    /**
     * ���SortedSet��Ԃ�
     */
    @Override
    public SortedSet<StatementInfo> getStatements() {
        return Collections.unmodifiableSortedSet(new TreeSet<StatementInfo>());
    }
}
