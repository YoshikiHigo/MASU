package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �R���X�g���N�^��\���N���X
 * 
 * @author higo
 *
 */
public abstract class ConstructorInfo extends CallableUnitInfo implements
        Comparable<ConstructorInfo> {

    /**
     * �K�v�ȏ���^���ď�����
     * 
     * @param modifiers ���̃R���X�g���N�^�̏C���q
     * @param ownerClass ���̃R���X�g���N�^���`���Ă���N���X
     * @param privateVisible private���ǂ�����
     * @param namespaceVisible �������O��Ԃ�������ǂ���
     * @param inheritanceVisible �q�N���X��������ǂ���
     * @param publicVisible public ���ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public ConstructorInfo(final Set<ModifierInfo> modifiers, final ClassInfo ownerClass,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, ownerClass, privateVisible, namespaceVisible, inheritanceVisible,
                publicVisible, fromLine, fromColumn, toLine, toColumn);

    }

    /**
     * �R���X�g���N�^�Ԃ̏����֌W���`���郁�\�b�h�D�ȉ��̏����ŏ��������߂�D
     * <ol>
     * <li>�R���X�g���N�^���`���Ă���N���X�̖��O��Ԗ�</li>
     * <li>�R���X�g���N�^���`���Ă���N���X�̃N���X��</li>
     * <li>�R���X�g���N�^�̈����̌�</li>
     * <li>�R���X�g���N�^�̈����̌^�i���������珇�ԂɁj</li>
     */
    public final int compareTo(final ConstructorInfo constructor) {

        if (null == constructor) {
            throw new NullPointerException();
        }

        // �N���X�I�u�W�F�N�g�� compareTo ��p����D
        // �N���X�̖��O��Ԗ��C�N���X������r�ɗp�����Ă���D
        final ClassInfo ownerClass = this.getOwnerClass();
        final ClassInfo correspondOwnerClass = constructor.getOwnerClass();
        final int classOrder = ownerClass.compareTo(correspondOwnerClass);
        if (classOrder != 0) {
            return classOrder;
        }

        // �����̌��Ŕ�r
        final int parameterNumber = this.getParameterNumber();
        final int correspondParameterNumber = constructor.getParameterNumber();
        if (parameterNumber < correspondParameterNumber) {
            return 1;
        } else if (parameterNumber > correspondParameterNumber) {
            return -1;
        } else {

            // �����̌^�Ŕ�r�D���������珇�ԂɁD
            final Iterator<ParameterInfo> parameterIterator = this.getParameters().iterator();
            final Iterator<ParameterInfo> correspondParameterIterator = constructor.getParameters()
                    .iterator();
            while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                final ParameterInfo parameter = parameterIterator.next();
                final ParameterInfo correspondParameter = correspondParameterIterator.next();
                final String typeName = parameter.getName();
                final String correspondTypeName = correspondParameter.getName();
                final int typeOrder = typeName.compareTo(correspondTypeName);
                if (typeOrder != 0) {
                    return typeOrder;
                }
            }

            return 0;
        }
    }

    /**
     * ���̃R���X�g���N�^�̈�����ǉ�����D public �錾���Ă��邪�C �v���O�C������̌Ăяo���͂͂����D
     * 
     * @param parameter �ǉ��������
     */
    public void addParameter(final ParameterInfo parameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameter) {
            throw new NullPointerException();
        }

        this.parameters.add(parameter);
    }

    /**
     * ���̃R���X�g���N�^�̈�����ǉ�����D public �錾���Ă��邪�C �v���O�C������̌Ăяo���͂͂����D
     * 
     * @param parameters �ǉ���������Q
     */
    public void addParameters(final List<ParameterInfo> parameters) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameters) {
            throw new NullPointerException();
        }

        this.parameters.addAll(parameters);
    }

    /**
     * ���̃R���X�g���N�^���C�����ŗ^����ꂽ�����g���ČĂяo�����Ƃ��ł��邩�ǂ����𔻒肷��
     * 
     * @param actualParameters �����̌^�̃��X�g
     * @return �Ăяo����ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    @Override
    public final boolean canCalledWith(final List<ExpressionInfo> actualParameters) {

        if (null == actualParameters) {
            throw new IllegalArgumentException();
        }

        return super.canCalledWith(actualParameters);
    }

    /**
     * ���̃R���X�g���N�^�̈����̐���Ԃ�
     * 
     * @return ���̃��\�b�h�̈����̐�
     */
    public int getParameterNumber() {
        return this.parameters.size();
    }
}
