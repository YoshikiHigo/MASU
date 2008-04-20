package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
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

        this.parameters = new LinkedList<ParameterInfo>();
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
     * ���̃R���X�g���N�^�̈����� List ��Ԃ��D
     * 
     * @return ���̃��\�b�h�̈����� List
     */
    public List<ParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * ���̃R���X�g���N�^���C�����ŗ^����ꂽ�����g���ČĂяo�����Ƃ��ł��邩�ǂ����𔻒肷��
     * 
     * @param actualParameters �����̌^�̃��X�g
     * @return �Ăяo����ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public final boolean canCalledWith(final List<EntityUsageInfo> actualParameters) {

        if (null == actualParameters) {
            throw new NullPointerException();
        }

        // �����̐����������Ȃ��ꍇ�͊Y�����Ȃ�
        final List<ParameterInfo> dummyParameters = this.getParameters();
        if (dummyParameters.size() != actualParameters.size()) {
            return false;
        }

        // �����̌^��擪����`�F�b�N�������Ȃ��ꍇ�͊Y�����Ȃ�
        final Iterator<ParameterInfo> dummyParameterIterator = dummyParameters.iterator();
        final Iterator<EntityUsageInfo> actualParameterIterator = actualParameters.iterator();
        NEXT_PARAMETER: while (dummyParameterIterator.hasNext()
                && actualParameterIterator.hasNext()) {
            final ParameterInfo dummyParameter = dummyParameterIterator.next();
            final EntityUsageInfo actualParameter = actualParameterIterator.next();

            // ���������Q�ƌ^�̏ꍇ
            if (actualParameter.getType() instanceof ClassTypeInfo) {

                // �������̌^�̃N���X���擾
                final ClassInfo actualParameterClass = ((ClassTypeInfo) actualParameter.getType())
                        .getReferencedClass();

                // ���������Q�ƌ^�łȂ��ꍇ�͊Y�����Ȃ�
                if (!(dummyParameter.getType() instanceof ClassTypeInfo)) {
                    return false;
                }

                // �������̌^�̃N���X���擾
                final ClassInfo dummyParameterClass = ((ClassTypeInfo) dummyParameter.getType())
                        .getReferencedClass();

                // �������C���������ɑΏۃN���X�ł���ꍇ�́C���̌p���֌W���l������D�܂�C���������������̃T�u�N���X�łȂ��ꍇ�́C�Ăяo���\�ł͂Ȃ�
                if ((actualParameterClass instanceof TargetClassInfo)
                        && (dummyParameterClass instanceof TargetClassInfo)) {

                    // ���������������Ɠ����Q�ƌ^�i�N���X�j�ł��Ȃ��C�������̃T�u�N���X�ł��Ȃ��ꍇ�͊Y�����Ȃ�
                    if (actualParameterClass.equals(dummyParameterClass)) {
                        continue NEXT_PARAMETER;

                    } else if (actualParameterClass.isSubClass(dummyParameterClass)) {
                        continue NEXT_PARAMETER;

                    } else {
                        return false;
                    }

                    // �������C���������ɊO���N���X�ł���ꍇ�́C�������ꍇ�̂݌Ăяo���\�Ƃ���
                } else if ((actualParameterClass instanceof ExternalClassInfo)
                        && (dummyParameterClass instanceof ExternalClassInfo)) {

                    if (actualParameterClass.equals(dummyParameterClass)) {
                        continue NEXT_PARAMETER;
                    }

                    return false;

                    // ���������O���N���X�C���������ΏۃN���X�̏ꍇ�́C���������������̃T�u�N���X�ł���ꍇ�C�Ăяo���\�Ƃ���
                } else if ((actualParameterClass instanceof TargetClassInfo)
                        && (dummyParameterClass instanceof ExternalClassInfo)) {

                    if (actualParameterClass.isSubClass(dummyParameterClass)) {
                        continue NEXT_PARAMETER;
                    }

                    return false;

                    // ���������ΏۃN���X�C���������O���N���X�̏ꍇ�́C�Ăяo���s�\�Ƃ���
                } else {
                    return false;
                }

                // ���������v���~�e�B�u�^�̏ꍇ
            } else if (actualParameter.getType() instanceof PrimitiveTypeInfo) {

                // PrimitiveTypeInfo#equals ���g���ē������̔���D
                // �������Ȃ��ꍇ�͊Y�����Ȃ�
                if (actualParameter.getType().equals(dummyParameter.getType())) {
                    continue NEXT_PARAMETER;
                }

                return false;

                // ���������z��^�̏ꍇ
            } else if (actualParameter.getType() instanceof ArrayTypeInfo) {

                if (!(dummyParameter.getType() instanceof ArrayTypeInfo)) {
                    return false;
                }

                if (!actualParameter.getType().equals(dummyParameter.getType())) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java����̏ꍇ�́C�������� java.lang.object �ł�OK�ȏ������K�v

                // �������� null �̏ꍇ
            } else if (actualParameter instanceof NullUsageInfo) {

                // ���������Q�ƌ^�łȂ��ꍇ�͊Y�����Ȃ�
                if (!(dummyParameter.getType() instanceof ClassInfo)) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java����̏ꍇ�́C���������z��^�̏ꍇ�ł�OK�ȏ������K�v

                // �������̌^�������ł��Ȃ������ꍇ
            } else if (actualParameter.getType() instanceof UnknownTypeInfo) {

                // �������̌^���s���ȏꍇ�́C�������̌^�����ł��낤�Ƃ�OK�ɂ��Ă���
                continue NEXT_PARAMETER;

            } else {
                assert false : "Here shouldn't be reached!";
            }
        }

        return true;
    }

    /**
     * ���̃R���X�g���N�^�̈����̐���Ԃ�
     * 
     * @return ���̃��\�b�h�̈����̐�
     */
    public int getParameterNumber() {
        return this.parameters.size();
    }

    /**
     * �����̃��X�g�̕ۑ����邽�߂̕ϐ�
     */
    protected final List<ParameterInfo> parameters;

}
