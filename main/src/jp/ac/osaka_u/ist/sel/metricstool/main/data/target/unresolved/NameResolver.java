package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;


/**
 * �������^�����������邽�߂̃��[�e�B���e�B�N���X
 * 
 * @author higo
 * 
 */
public final class NameResolver {

    /**
     * �����ŗ^����ꂽ�������^����\�������ς݌^���N���X�𐶐�����D �����ň����Ƃ��ė^������̂́C�\�[�X�R�[�h���p�[�X����Ă��Ȃ��^�ł���̂ŁC������������ς݌^���N���X��
     * ExternalClassInfo �ƂȂ�D
     * 
     * @param unresolvedReferenceType �������^���
     * @return �����ς݌^���
     */
    public static ExternalClassInfo createExternalClassInfo(
            final UnresolvedClassReferenceInfo unresolvedReferenceType) {

        if (null == unresolvedReferenceType) {
            throw new NullPointerException();
        }

        // �������N���X���̎Q�Ɩ����擾
        final String[] referenceName = unresolvedReferenceType.getReferenceName();

        // ���p�\�Ȗ��O��Ԃ��������C�������N���X���̊��S���薼������
        for (AvailableNamespaceInfo availableNamespace : unresolvedReferenceType
                .getAvailableNamespaces()) {

            // ���O��Ԗ�.* �ƂȂ��Ă���ꍇ�́C�����邱�Ƃ��ł��Ȃ�
            if (availableNamespace.isAllClasses()) {
                continue;
            }

            // ���O���.�N���X�� �ƂȂ��Ă���ꍇ
            final String[] importName = availableNamespace.getImportName();

            // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
            if (importName[importName.length - 1].equals(referenceName[0])) {

                final String[] namespace = availableNamespace.getNamespace();
                final String[] fullQualifiedName = new String[namespace.length
                        + referenceName.length];
                System.arraycopy(namespace, 0, fullQualifiedName, 0, namespace.length);
                System.arraycopy(referenceName, 0, fullQualifiedName, namespace.length,
                        referenceName.length);

                final ExternalClassInfo classInfo = new ExternalClassInfo(fullQualifiedName);
                return classInfo;
            }
        }

        // ������Ȃ��ꍇ�́C���O��Ԃ� UNKNOWN �� �O���N���X�����쐬
        final ExternalClassInfo unknownClassInfo = new ExternalClassInfo(
                referenceName[referenceName.length - 1]);
        return unknownClassInfo;
    }

    /**
     * �����ŗ^����ꂽ�^�� List ����O���p�����[�^�� List ���쐬���C�Ԃ�
     * 
     * @param types �^��List
     * @return �O���p�����[�^�� List
     */
    public static List<ParameterInfo> createParameters(final List<TypeInfo> types) {

        if (null == types) {
            throw new NullPointerException();
        }

        final List<ParameterInfo> parameters = new LinkedList<ParameterInfo>();
        for (TypeInfo type : types) {
            final ExternalParameterInfo parameter = new ExternalParameterInfo(type);
            parameters.add(parameter);
        }

        return Collections.unmodifiableList(parameters);
    }

    /**
     * �����ŗ^����ꂽ�N���X�̐e�N���X�ł���C���O���N���X(ExternalClassInfo)�ł�����̂�Ԃ��D �N���X�K�w�I�ɍł����ʂɈʒu����O���N���X��Ԃ��D
     * �Y������N���X�����݂��Ȃ��ꍇ�́C null ��Ԃ��D
     * 
     * @param classInfo �ΏۃN���X
     * @return �����ŗ^����ꂽ�N���X�̐e�N���X�ł���C���N���X�K�w�I�ɍł����ʂɈʒu����O���N���X
     */
    public static ExternalClassInfo getExternalSuperClass(final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        for (final ClassInfo superClassInfo : ReferenceTypeInfo
                .convert(classInfo.getSuperClasses())) {

            if (superClassInfo instanceof ExternalClassInfo) {
                return (ExternalClassInfo) superClassInfo;
            }

            final ExternalClassInfo superSuperClassInfo = NameResolver
                    .getExternalSuperClass((TargetClassInfo) superClassInfo);
            if (null != superSuperClassInfo) {
                return superSuperClassInfo;
            }
        }

        return null;
    }

    /**
     * �����ŗ^����ꂽ�N���X������N���X�Ƃ��Ď��C�ł��O���́i�C���i�[�N���X�łȂ��j�N���X��Ԃ�
     * 
     * @param innerClass �C���i�[�N���X
     * @return �ł��O���̃N���X
     */
    public static TargetClassInfo getOuterstClass(final TargetInnerClassInfo innerClass) {

        if (null == innerClass) {
            throw new NullPointerException();
        }

        final TargetClassInfo outerClass = innerClass.getOuterClass();
        return outerClass instanceof TargetInnerClassInfo ? NameResolver
                .getOuterstClass((TargetInnerClassInfo) outerClass) : outerClass;
    }

    /**
     * �����ŗ^����ꂽ�N���X���̗��p�\�ȓ����N���X�� SortedSet ��Ԃ�
     * 
     * @param classInfo �N���X
     * @return �����ŗ^����ꂽ�N���X���̗��p�\�ȓ����N���X�� SortedSet
     */
    public static SortedSet<TargetInnerClassInfo> getAvailableInnerClasses(
            final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        final SortedSet<TargetInnerClassInfo> innerClasses = new TreeSet<TargetInnerClassInfo>();
        for (final TargetInnerClassInfo innerClass : classInfo.getInnerClasses()) {

            innerClasses.add(innerClass);
            final SortedSet<TargetInnerClassInfo> innerClassesInInnerClass = NameResolver
                    .getAvailableInnerClasses(innerClass);
            innerClasses.addAll(innerClassesInInnerClass);
        }

        return Collections.unmodifiableSortedSet(innerClasses);
    }

    /**
     * �u���݂̃N���X�v�ŗ��p�\�ȃt�B�[���h�ꗗ��Ԃ��D
     * �����ŁC�u���p�\�ȃt�B�[���h�v�Ƃ́C�u���݂̃N���X�v�Œ�`����Ă���t�B�[���h�C�u���݂̃N���X�v�̃C���i�[�N���X�Œ�`����Ă���t�B�[���h�C
     * �y�т��̐e�N���X�Œ�`����Ă���t�B�[���h�̂����q�N���X����A�N�Z�X���\�ȃt�B�[���h�ł���D ���p�\�ȃt�B�[���h�� List �Ɋi�[����Ă���D
     * ���X�g�̐擪����D�揇�ʂ̍����t�B�[���h�i�܂�C �N���X�K�w�ɂ����ĉ��ʂ̃N���X�ɒ�`����Ă���t�B�[���h�j���i�[����Ă���D
     * 
     * @param currentClass ���݂̃N���X
     * @return ���p�\�ȃt�B�[���h�ꗗ
     */
    public static List<TargetFieldInfo> getAvailableFields(final TargetClassInfo currentClass) {

        if (null == currentClass) {
            throw new NullPointerException();
        }

        // �`�F�b�N�����N���X�����邽�߂̃L���b�V���C�L���b�V���ɂ���N���X�͓�x�ڂ̓t�B�[���h�擾���Ȃ��i���[�v�\���΍�j
        final Set<TargetClassInfo> checkedClasses = new HashSet<TargetClassInfo>();

        // ���p�\�ȕϐ��������邽�߂̃��X�g
        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // �ł��O���̃N���X���擾
        final TargetClassInfo outestClass;
        if (currentClass instanceof TargetInnerClassInfo) {
            outestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) currentClass);

            for (TargetClassInfo outerClass = currentClass; !outerClass.equals(outestClass); outerClass = ((TargetInnerClassInfo) outerClass)
                    .getOuterClass()) {

                // ���N���X����сC�O���N���X�Œ�`���ꂽ���\�b�h��ǉ�
                availableFields.addAll(outerClass.getDefinedFields());
                checkedClasses.add(outerClass);
            }

            // �����N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
            for (final TargetInnerClassInfo innerClass : currentClass.getInnerClasses()) {
                final List<TargetFieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                        .getAvailableFieldsDefinedInInnerClasses(innerClass, checkedClasses);
                availableFields.addAll(availableFieldsDefinedInInnerClasses);
            }

            // �e�N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
            for (final ClassInfo superClass : ReferenceTypeInfo.convert(currentClass
                    .getSuperClasses())) {
                if (superClass instanceof TargetClassInfo) {
                    final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                            .getAvailableFieldsDefinedInSuperClasses((TargetClassInfo) superClass,
                                    checkedClasses);
                    availableFields.addAll(availableFieldsDefinedInSuperClasses);
                }
            }

        } else {
            outestClass = currentClass;
        }

        // �ł��O���̃N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        availableFields.addAll(outestClass.getDefinedFields());
        checkedClasses.add(outestClass);

        // �����N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final TargetInnerClassInfo innerClass : outestClass.getInnerClasses()) {
            final List<TargetFieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses(innerClass, checkedClasses);
            availableFields.addAll(availableFieldsDefinedInInnerClasses);
        }

        // �e�N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final ClassInfo superClass : ReferenceTypeInfo.convert(outestClass.getSuperClasses())) {
            if (superClass instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsDefinedInSuperClasses((TargetClassInfo) superClass,
                                checkedClasses);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableFields);
    }

    /**
     * �����ŗ^����ꂽ�N���X�Ƃ��̓����N���X�Œ�`���ꂽ�t�B�[���h�̂����C�O���̃N���X�ŗ��p�\�ȃt�B�[���h�� List ��Ԃ�
     * 
     * @param classInfo �N���X
     * @param checkedClasses ���Ƀ`�F�b�N�����N���X�̃L���b�V��
     * @return �O���̃N���X�ŗ��p�\�ȃt�B�[���h�� List
     */
    public static List<TargetFieldInfo> getAvailableFieldsDefinedInInnerClasses(
            final TargetInnerClassInfo classInfo, final Set<TargetClassInfo> checkedClasses) {

        if ((null == classInfo) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // ���Ƀ`�F�b�N�����N���X�ł���ꍇ�͉��������ɏI������
        if (checkedClasses.contains(classInfo)) {
            return new LinkedList<TargetFieldInfo>();
        }

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // ���N���X�Œ�`����Ă���C���O��ԉ��������t�B�[���h��ǉ�
        // for (final TargetFieldInfo definedField : classInfo.getDefinedFields()) {
        // if (definedField.isNamespaceVisible()) {
        // availableFields.add(definedField);
        // }
        // }
        availableFields.addAll(classInfo.getDefinedFields());
        checkedClasses.add(classInfo);

        // �����N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final TargetInnerClassInfo innerClass : classInfo.getInnerClasses()) {
            final List<TargetFieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses(innerClass, checkedClasses);
            availableFields.addAll(availableFieldsDefinedInInnerClasses);
        }

        // �e�N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final ClassInfo superClass : ReferenceTypeInfo.convert(classInfo.getSuperClasses())) {
            if (superClass instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsDefinedInSuperClasses((TargetClassInfo) superClass,
                                checkedClasses);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableFields);
    }

    /**
     * �����ŗ^����ꂽ�N���X�Ƃ��̐e�N���X�Œ�`���ꂽ�t�B�[���h�̂����C�q�N���X�ŗ��p�\�ȃt�B�[���h�� List ��Ԃ�
     * 
     * @param classInfo �N���X
     * @param checkedClasses ���Ƀ`�F�b�N�����N���X�̃L���b�V��
     * @return �q�N���X�ŗ��p�\�ȃt�B�[���h�� List
     */
    private static List<TargetFieldInfo> getAvailableFieldsDefinedInSuperClasses(
            final TargetClassInfo classInfo, final Set<TargetClassInfo> checkedClasses) {

        if ((null == classInfo) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // ���Ƀ`�F�b�N�����N���X�ł���ꍇ�͉��������ɏI������
        if (checkedClasses.contains(classInfo)) {
            return new LinkedList<TargetFieldInfo>();
        }

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // ���N���X�Œ�`����Ă���C�N���X�K�w���������t�B�[���h��ǉ�
        for (final TargetFieldInfo definedField : classInfo.getDefinedFields()) {
            if (definedField.isInheritanceVisible()) {
                availableFields.add(definedField);
            }
        }
        checkedClasses.add(classInfo);

        // �����N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final TargetInnerClassInfo innerClass : classInfo.getInnerClasses()) {
            final List<TargetFieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses(innerClass, checkedClasses);
            for (final TargetFieldInfo field : availableFieldsDefinedInInnerClasses) {
                if (field.isInheritanceVisible()) {
                    availableFields.add(field);
                }
            }
        }

        // �e�N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final ClassInfo superClass : ReferenceTypeInfo.convert(classInfo.getSuperClasses())) {
            if (superClass instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsDefinedInSuperClasses((TargetClassInfo) superClass,
                                checkedClasses);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableFields);
    }

    /**
     * �u���݂̃N���X�v�ŗ��p�\�ȃ��\�b�h�ꗗ��Ԃ��D
     * �����ŁC�u���p�\�ȃ��\�b�h�v�Ƃ́C�u���݂̃N���X�v�Œ�`����Ă��郁�\�b�h�C�y�т��̐e�N���X�Œ�`����Ă��郁�\�b�h�̂����q�N���X����A�N�Z�X���\�ȃ��\�b�h�ł���D
     * ���p�\�ȃ��\�b�h�� List �Ɋi�[����Ă���D ���X�g�̐擪����D�揇�ʂ̍������\�b�h�i�܂�C�N���X�K�w�ɂ����ĉ��ʂ̃N���X�ɒ�`����Ă��郁�\�b�h�j���i�[����Ă���D
     * 
     * @param thisClass ���݂̃N���X
     * @return ���p�\�ȃ��\�b�h�ꗗ
     */
    private static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo currentClass) {

        if (null == currentClass) {
            throw new NullPointerException();
        }

        // �`�F�b�N�����N���X�����邽�߂̃L���b�V���C�L���b�V���ɂ���N���X�͓�x�ڂ̓t�B�[���h�擾���Ȃ��i���[�v�\���΍�j
        final Set<TargetClassInfo> checkedClasses = new HashSet<TargetClassInfo>();

        // ���p�\�ȕϐ��������邽�߂̃��X�g
        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // �ł��O���̃N���X���擾
        final TargetClassInfo outestClass;
        if (currentClass instanceof TargetInnerClassInfo) {
            outestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) currentClass);

            // ���N���X�Œ�`���ꂽ���\�b�h��ǉ�
            availableMethods.addAll(currentClass.getDefinedMethods());
            checkedClasses.add(currentClass);

            // �����N���X�Œ�`���ꂽ���\�b�h��ǉ�
            for (final TargetInnerClassInfo innerClass : currentClass.getInnerClasses()) {
                final List<TargetMethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                        .getAvailableMethodsDefinedInInnerClasses(innerClass, checkedClasses);
                availableMethods.addAll(availableMethodsDefinedInInnerClasses);
            }

            // �e�N���X�Œ�`���ꂽ���\�b�h��ǉ�
            for (final ClassInfo superClass : ReferenceTypeInfo.convert(currentClass
                    .getSuperClasses())) {
                if (superClass instanceof TargetClassInfo) {
                    final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                            .getAvailableMethodsDefinedInSuperClasses((TargetClassInfo) superClass,
                                    checkedClasses);
                    availableMethods.addAll(availableMethodsDefinedInSuperClasses);
                }
            }

        } else {
            outestClass = currentClass;
        }

        // �ł��O���̃N���X�Œ�`���ꂽ���\�b�h��ǉ�
        availableMethods.addAll(outestClass.getDefinedMethods());
        checkedClasses.add(outestClass);

        // �����N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final TargetInnerClassInfo innerClass : outestClass.getInnerClasses()) {
            final List<TargetMethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                    .getAvailableMethodsDefinedInInnerClasses(innerClass, checkedClasses);
            availableMethods.addAll(availableMethodsDefinedInInnerClasses);
        }

        // �e�N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final ClassInfo superClass : ReferenceTypeInfo.convert(outestClass.getSuperClasses())) {
            if (superClass instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsDefinedInSuperClasses((TargetClassInfo) superClass,
                                checkedClasses);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * �����ŗ^����ꂽ�N���X�Ƃ��̓����N���X�Œ�`���ꂽ���\�b�h�̂����C�O���̃N���X�ŗ��p�\�ȃ��\�b�h�� List ��Ԃ�
     * 
     * @param classInfo �N���X
     * @param checkedClasses ���Ƀ`�F�b�N�����N���X�̃L���b�V��
     * @return �O���̃N���X�ŗ��p�\�ȃ��\�b�h�� List
     */
    private static List<TargetMethodInfo> getAvailableMethodsDefinedInInnerClasses(
            final TargetInnerClassInfo classInfo, final Set<TargetClassInfo> checkedClasses) {

        if ((null == classInfo) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // ���Ƀ`�F�b�N�����N���X�ł���ꍇ�͉��������ɏI������
        if (checkedClasses.contains(classInfo)) {
            return new LinkedList<TargetMethodInfo>();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // ���N���X�Œ�`����Ă���C���O��ԉ����������\�b�h��ǉ�
        // for (final TargetFieldInfo definedField : classInfo.getDefinedFields()) {
        // if (definedField.isNamespaceVisible()) {
        // availableFields.add(definedField);
        // }
        // }
        availableMethods.addAll(classInfo.getDefinedMethods());
        checkedClasses.add(classInfo);

        // �����N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final TargetInnerClassInfo innerClass : classInfo.getInnerClasses()) {
            final List<TargetMethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                    .getAvailableMethodsDefinedInInnerClasses(innerClass, checkedClasses);
            availableMethods.addAll(availableMethodsDefinedInInnerClasses);
        }

        // �e�N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final ClassInfo superClass : ReferenceTypeInfo.convert(classInfo.getSuperClasses())) {
            if (superClass instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsDefinedInSuperClasses((TargetClassInfo) superClass,
                                checkedClasses);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * �����ŗ^����ꂽ�N���X�Ƃ��̐e�N���X�Œ�`���ꂽ���\�b�h�̂����C�q�N���X�ŗ��p�\�ȃ��\�b�h�� List ��Ԃ�
     * 
     * @param classInfo �N���X
     * @param checkedClasses ���Ƀ`�F�b�N�����N���X�̃L���b�V��
     * @return �q�N���X�ŗ��p�\�ȃ��\�b�h�� List
     */
    private static List<TargetMethodInfo> getAvailableMethodsDefinedInSuperClasses(
            final TargetClassInfo classInfo, final Set<TargetClassInfo> checkedClasses) {

        if ((null == classInfo) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // ���Ƀ`�F�b�N�����N���X�ł���ꍇ�͉��������ɏI������
        if (checkedClasses.contains(classInfo)) {
            return new LinkedList<TargetMethodInfo>();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // ���N���X�Œ�`����Ă���C�N���X�K�w�����������\�b�h��ǉ�
        for (final TargetMethodInfo definedMethod : classInfo.getDefinedMethods()) {
            if (definedMethod.isInheritanceVisible()) {
                availableMethods.add(definedMethod);
            }
        }
        checkedClasses.add(classInfo);

        // �����N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final TargetInnerClassInfo innerClass : classInfo.getInnerClasses()) {
            final List<TargetMethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                    .getAvailableMethodsDefinedInInnerClasses(innerClass, checkedClasses);
            for (final TargetMethodInfo method : availableMethodsDefinedInInnerClasses) {
                if (method.isInheritanceVisible()) {
                    availableMethods.add(method);
                }
            }
        }

        // �e�N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final ClassInfo superClass : ReferenceTypeInfo.convert(classInfo.getSuperClasses())) {
            if (superClass instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsDefinedInSuperClasses((TargetClassInfo) superClass,
                                checkedClasses);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * �u�g�p�����N���X�v���u�g�p����N���X�v�ɂ����Ďg�p�����ꍇ�ɁC���p�\�ȃt�B�[���h�ꗗ��Ԃ��D
     * �����ŁC�u���p�\�ȃt�B�[���h�v�Ƃ́C�u�g�p�����N���X�v�Œ�`����Ă���t�B�[���h�C�y�т��̐e�N���X�Œ�`����Ă���t�B�[���h�̂����q�N���X����A�N�Z�X���\�ȃt�B�[���h�ł���D
     * �܂��C�u�g�p�����N���X�v�Ɓu�g�p����N���X�v�̖��O��Ԃ��r���C��萳�m�ɗ��p�\�ȃt�B�[���h���擾����D �q�N���X�ŗ��p�\�ȃt�B�[���h�ꗗ�� List �Ɋi�[����Ă���D
     * ���X�g�̐擪����D�揇�ʂ̍����t�B�[���h�i�܂�C�N���X�K�w�ɂ����ĉ��ʂ̃N���X�ɒ�`����Ă���t�B�[���h�j���i�[����Ă���D
     * 
     * @param usedClass �g�p�����N���X
     * @param usingClass �g�p����N���X
     * @return ���p�\�ȃt�B�[���h�ꗗ
     */
    public static List<TargetFieldInfo> getAvailableFields(final TargetClassInfo usedClass,
            final TargetClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        // �g�p�����N���X�̍ł��O���̃N���X���擾
        final TargetClassInfo usedOutestClass;
        if (usedClass instanceof TargetInnerClassInfo) {
            usedOutestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usedClass);
        } else {
            usedOutestClass = usedClass;
        }

        // �g�p����N���X�̍ł��O���̃N���X���擾
        final TargetClassInfo usingOutestClass;
        if (usingClass instanceof TargetInnerClassInfo) {
            usingOutestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usingClass);
        } else {
            usingOutestClass = usingClass;
        }

        // ���̃N���X�Œ�`����Ă���t�B�[���h�̂����C�g�p����N���X�ŗ��p�\�ȃt�B�[���h���擾����
        // 2�̃N���X�������ꍇ�C�S�Ẵt�B�[���h�����p�\
        if (usedOutestClass.equals(usingOutestClass)) {

            return NameResolver.getAvailableFields(usedClass);

            // 2�̃N���X���������O��Ԃ������Ă���ꍇ
        } else if (usedOutestClass.getNamespace().equals(usingOutestClass.getNamespace())) {

            final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

            // ���O��ԉ������������t�B�[���h�݂̂����p�\
            for (final TargetFieldInfo field : NameResolver.getAvailableFields(usedClass)) {
                if (field.isNamespaceVisible()) {
                    availableFields.add(field);
                }
            }

            return Collections.unmodifiableList(availableFields);

            // �Ⴄ���O��Ԃ������Ă���ꍇ
        } else {

            final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

            // �S���������t�B�[���h�݂̂����p�\
            for (final TargetFieldInfo field : NameResolver.getAvailableFields(usedClass)) {
                if (field.isPublicVisible()) {
                    availableFields.add(field);
                }
            }

            return Collections.unmodifiableList(availableFields);
        }
    }

    /**
     * �u�g�p�����N���X�v���u�g�p����N���X�v�ɂ����Ďg�p�����ꍇ�ɁC���p�\�ȃ��\�b�h�ꗗ��Ԃ��D
     * �����ŁC�u���p�\�ȃ��\�b�h�v�Ƃ́C�u�g�p�����N���X�v�Œ�`����Ă��郁�\�b�h�C�y�т��̐e�N���X�Œ�`����Ă��郁�\�b�h�̂����q�N���X����A�N�Z�X���\�ȃ��\�b�h�ł���D
     * �܂��C�u�g�p�����N���X�v�Ɓu�g�p����N���X�v�̖��O��Ԃ��r���C��萳�m�ɗ��p�\�ȃ��\�b�h���擾����D �q�N���X�ŗ��p�\�ȃ��\�b�h�ꗗ�� List �Ɋi�[����Ă���D
     * ���X�g�̐擪����D�揇�ʂ̍������\�b�h�i�܂�C�N���X�K�w�ɂ����ĉ��ʂ̃N���X�ɒ�`����Ă��郁�\�b�h�j���i�[����Ă���D
     * 
     * @param usedClass �g�p�����N���X
     * @param usingClass �g�p����N���X
     * @return ���p�\�ȃ��\�b�h�ꗗ
     */
    public static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo usedClass,
            final TargetClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        // �g�p�����N���X�̍ł��O���̃N���X���擾
        final TargetClassInfo usedOutestClass;
        if (usedClass instanceof TargetInnerClassInfo) {
            usedOutestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usedClass);
        } else {
            usedOutestClass = usedClass;
        }

        // �g�p����N���X�̍ł��O���̃N���X���擾
        final TargetClassInfo usingOutestClass;
        if (usingClass instanceof TargetInnerClassInfo) {
            usingOutestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usingClass);
        } else {
            usingOutestClass = usingClass;
        }

        // ���̃N���X�Œ�`����Ă��郁�\�b�h�̂����C�g�p����N���X�ŗ��p�\�ȃ��\�b�h���擾����
        // 2�̃N���X�������ꍇ�C�S�Ẵ��\�b�h�����p�\
        if (usedOutestClass.equals(usingOutestClass)) {

            return NameResolver.getAvailableMethods(usedClass);

            // 2�̃N���X���������O��Ԃ������Ă���ꍇ
        } else if (usedOutestClass.getNamespace().equals(usingOutestClass.getNamespace())) {

            final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

            // ���O��ԉ��������������\�b�h�݂̂����p�\
            for (final TargetMethodInfo method : NameResolver.getAvailableMethods(usedClass)) {
                if (method.isNamespaceVisible()) {
                    availableMethods.add(method);
                }
            }

            return Collections.unmodifiableList(availableMethods);

            // �Ⴄ���O��Ԃ������Ă���ꍇ
        } else {

            final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

            // �S�����������\�b�h�݂̂����p�\
            for (final TargetMethodInfo method : NameResolver.getAvailableMethods(usedClass)) {
                if (method.isPublicVisible()) {
                    availableMethods.add(method);
                }
            }

            return Collections.unmodifiableList(availableMethods);
        }
    }

    /**
     * �����ŗ^����ꂽ�N���X�̒��ڂ̃C���i�[�N���X��Ԃ��D�e�N���X�Œ�`���ꂽ�C���i�[�N���X���܂܂��D
     * 
     * @param classInfo �N���X
     * @return �����ŗ^����ꂽ�N���X�̒��ڂ̃C���i�[�N���X�C�e�N���X�Œ�`���ꂽ�C���i�[�N���X���܂܂��D
     */
    public static final SortedSet<TargetInnerClassInfo> getAvailableDirectInnerClasses(
            final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        final SortedSet<TargetInnerClassInfo> availableDirectInnerClasses = new TreeSet<TargetInnerClassInfo>();

        // �����ŗ^����ꂽ�N���X�̒��ڂ̃C���i�[�N���X��ǉ�
        availableDirectInnerClasses.addAll(classInfo.getInnerClasses());

        // �e�N���X�ɑ΂��čċA�I�ɏ���
        for (final ClassInfo superClassInfo : ReferenceTypeInfo
                .convert(classInfo.getSuperClasses())) {

            if (superClassInfo instanceof TargetClassInfo) {
                final SortedSet<TargetInnerClassInfo> availableDirectInnerClassesInSuperClass = NameResolver
                        .getAvailableDirectInnerClasses((TargetClassInfo) superClassInfo);
                availableDirectInnerClasses.addAll(availableDirectInnerClassesInSuperClass);
            }
        }

        return Collections.unmodifiableSortedSet(availableDirectInnerClasses);
    }

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "NameResolver";
        }
    }, MESSAGE_TYPE.ERROR);
}
