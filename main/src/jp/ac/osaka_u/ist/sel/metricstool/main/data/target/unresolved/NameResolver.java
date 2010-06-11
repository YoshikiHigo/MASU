package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AnonymousClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterizable;


/**
 * �������^�����������邽�߂̃��[�e�B���e�B�N���X
 * 
 * @author higo
 * 
 */
public final class NameResolver {

    /**
     * �����ŗ^����ꂽ�N���X�̐e�N���X�ł���C���O���N���X(ExternalClassInfo)�ł�����̂�Ԃ��D �N���X�K�w�I�ɍł����ʂɈʒu����O���N���X��Ԃ��D
     * �Y������N���X�����݂��Ȃ��ꍇ�́C null ��Ԃ��D
     * 
     * @param classInfo �ΏۃN���X
     * @return �����ŗ^����ꂽ�N���X�̐e�N���X�ł���C���N���X�K�w�I�ɍł����ʂɈʒu����O���N���X
     */
    public static ExternalClassInfo getExternalSuperClass(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            if (superClassInfo instanceof ExternalClassInfo) {
                return (ExternalClassInfo) superClassInfo;
            }

            final ExternalClassInfo superSuperClassInfo = NameResolver
                    .getExternalSuperClass(superClassInfo);
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
    public static ClassInfo getOuterstClass(final InnerClassInfo innerClass) {

        if (null == innerClass) {
            throw new IllegalArgumentException();
        }
        
        final ClassInfo outerClass = innerClass.getOuterClass();
        return outerClass instanceof InnerClassInfo ? NameResolver
                .getOuterstClass((InnerClassInfo) outerClass) : outerClass;
    }

    /**
     * �����ŗ^����ꂽ�N���X���̗��p�\�ȓ����N���X�� SortedSet ��Ԃ�
     * 
     * @param classInfo �N���X
     * @return �����ŗ^����ꂽ�N���X���̗��p�\�ȓ����N���X�� SortedSet
     */
    public static SortedSet<InnerClassInfo> getAvailableInnerClasses(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        final SortedSet<InnerClassInfo> innerClasses = new TreeSet<InnerClassInfo>();
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {

            innerClasses.add(innerClass);
            final SortedSet<InnerClassInfo> innerClassesInInnerClass = NameResolver
                    .getAvailableInnerClasses((ClassInfo) innerClass);
            innerClasses.addAll(innerClassesInInnerClass);
        }

        return Collections.unmodifiableSortedSet(innerClasses);
    }

    /**
     * �����ŗ^����ꂽ�N���X�ŗ��p�\�ȃN���X�́@List�@��Ԃ�
     * 
     * @param classInfo �N���X
     * @return�@�����ŗ^����ꂽ�N���X�ŗ��p�\�ȃN���X�́@List
     */
    public static List<ClassInfo> getAvailableClasses(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        // ���p�\�ȕϐ��������邽�߂̃��X�g
        final List<ClassInfo> availableClasses = new LinkedList<ClassInfo>();

        // �ł��O���̃N���X���擾
        final ClassInfo outestClass;
        if (classInfo instanceof InnerClassInfo) {

            outestClass = NameResolver.getOuterstClass((InnerClassInfo) classInfo);

            // �O������ъO���N���X�̐e�N���X��ǉ�
            for (ClassInfo outerClass = classInfo; !outerClass.equals(outestClass); outerClass = ((InnerClassInfo) outerClass)
                    .getOuterClass()) {

                availableClasses.add(outerClass);
                NameResolver.getAvailableSuperClasses(classInfo, outerClass, availableClasses);
            }

        } else {
            outestClass = classInfo;
        }

        //�@�ł��O������т����Ƃ��O���̃N���X�̐e�N���X��ǉ�
        availableClasses.add(outestClass);
        for (final ClassInfo superClass : ClassTypeInfo.convert(outestClass.getSuperClasses())) {
            NameResolver.getAvailableSuperClasses(outestClass, superClass, availableClasses);
            availableClasses.add(superClass);
        }
        NameResolver.getAvailableSuperClasses(classInfo, outestClass, availableClasses);

        // �����N���X��ǉ�
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {
            NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
        }

        return Collections.unmodifiableList(availableClasses);
    }

    public static void getAvailableSuperClasses(final ClassInfo subClass,
            final ClassInfo superClass, final List<ClassInfo> availableClasses) {

        if ((null == subClass) || (null == superClass) || (null == availableClasses)) {
            throw new NullPointerException();
        }

        // ���Ƀ`�F�b�N�����N���X�ł���ꍇ�͉��������ɏI������
        if (availableClasses.contains(superClass)) {
            return;
        }

        // ���N���X��ǉ�
        // �q�N���X�Ɛe�N���X�̖��O��Ԃ������ꍇ�́C���O��ԉ��������͌p����������΂悢
        if (subClass.getNamespace().equals(superClass.getNamespace())) {

            if (superClass.isInheritanceVisible() || superClass.isNamespaceVisible()) {
                availableClasses.add(superClass);
                for (final InnerClassInfo innerClass : superClass.getInnerClasses()) {
                    NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
                }
            }

            //�q�N���X�Ɛe�N���X�̖��O��Ԃ��Ⴄ�ꍇ�́C�p����������΂悢
        } else {

            if (superClass.isInheritanceVisible()) {
                availableClasses.add(superClass);
                for (final InnerClassInfo innerClass : superClass.getInnerClasses()) {
                    NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
                }
            }
        }

        // �e�N���X��ǉ�
        for (final ClassInfo superSuperClass : ClassTypeInfo.convert(superClass.getSuperClasses())) {
            NameResolver.getAvailableSuperClasses(subClass, superSuperClass, availableClasses);
        }
    }

    public static void getAvailableInnerClasses(final ClassInfo classInfo,
            final List<ClassInfo> availableClasses) {

        if ((null == classInfo) || (null == availableClasses)) {
            throw new NullPointerException();
        }

        // ���Ƀ`�F�b�N�����N���X�ł���ꍇ�͉��������ɏI������
        if (availableClasses.contains(classInfo)) {
            return;
        }

        // �����C���i�[�N���X�̏ꍇ�͒ǉ������ɏI������
        if (classInfo instanceof AnonymousClassInfo) {
            return;
        }

        availableClasses.add(classInfo);

        // �����N���X��ǉ�
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {
            NameResolver.getAvailableInnerClasses((ClassInfo) innerClass, availableClasses);
        }

        return;
    }

    /**
     * �u���݂̃N���X�v�ŗ��p�\�ȃt�B�[���h�ꗗ��Ԃ��D
     * �����ŁC�u���p�\�ȃt�B�[���h�v�Ƃ́C�u���݂̃N���X�v�Œ�`����Ă���t�B�[���h�C�u���݂̃N���X�v�̃C���i�[�N���X�Œ�`����Ă���t�B�[���h�C
     * �y�т��̐e�N���X�Œ�`����Ă���t�B�[���h�̂����q�N���X����A�N�Z�X���\�ȃt�B�[���h�ł���D ���p�\�ȃt�B�[���h�� List �Ɋi�[����Ă���D
     * ���X�g�̐擪����D�揇�ʂ̍����t�B�[���h�i�܂�C �N���X�K�w�ɂ����ĉ��ʂ̃N���X�ɒ�`����Ă���t�B�[���h�j���i�[����Ă���D
     * 
     * TODO �X�^�e�B�b�N�C���|�[�g�����ǉ����Ȃ��Ƃ����Ȃ�
     * 
     * @param currentClass ���݂̃N���X
     * @return ���p�\�ȃt�B�[���h�ꗗ
     */
    public static List<FieldInfo> getAvailableFields(final ClassInfo currentClass) {

        if (null == currentClass) {
            throw new IllegalArgumentException();
        }

        // �`�F�b�N�����N���X�����邽�߂̃L���b�V���C�L���b�V���ɂ���N���X�͓�x�ڂ̓t�B�[���h�擾���Ȃ��i���[�v�\���΍�j
        final Set<ClassInfo> checkedClasses = new HashSet<ClassInfo>();

        // ���p�\�ȕϐ��������邽�߂̃��X�g
        final List<FieldInfo> availableFields = new LinkedList<FieldInfo>();

        // �ł��O���̃N���X���擾
        final ClassInfo outestClass;
        if (currentClass instanceof InnerClassInfo) {

            outestClass = NameResolver.getOuterstClass((InnerClassInfo) currentClass);

            for (ClassInfo outerClass = currentClass; !outerClass.equals(outestClass); outerClass = ((InnerClassInfo) outerClass)
                    .getOuterClass()) {

                // ���N���X����сC�O���N���X�Œ�`���ꂽ���\�b�h��ǉ�
                availableFields.addAll(outerClass.getDefinedFields());
                checkedClasses.add(outerClass);
            }

            // �����N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
            for (final InnerClassInfo innerClass : currentClass.getInnerClasses()) {
                final List<FieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                        .getAvailableFieldsDefinedInInnerClasses((ClassInfo) innerClass,
                                checkedClasses);
                availableFields.addAll(availableFieldsDefinedInInnerClasses);
            }

            // �e�N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
            for (final ClassInfo superClass : ClassTypeInfo.convert(currentClass.getSuperClasses())) {
                final List<FieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsDefinedInSuperClasses(currentClass, superClass,
                                checkedClasses);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }

        } else {
            outestClass = currentClass;
        }

        // �ł��O���̃N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        availableFields.addAll(outestClass.getDefinedFields());
        checkedClasses.add(outestClass);

        // �����N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final InnerClassInfo innerClass : outestClass.getInnerClasses()) {
            final List<FieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses((ClassInfo) innerClass, checkedClasses);
            availableFields.addAll(availableFieldsDefinedInInnerClasses);
        }

        // �e�N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final ClassInfo superClass : ClassTypeInfo.convert(outestClass.getSuperClasses())) {
            final List<FieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                    .getAvailableFieldsDefinedInSuperClasses(outestClass, superClass,
                            checkedClasses);
            availableFields.addAll(availableFieldsDefinedInSuperClasses);
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
    public static List<FieldInfo> getAvailableFieldsDefinedInInnerClasses(
            final ClassInfo classInfo, final Set<ClassInfo> checkedClasses) {

        if ((null == classInfo) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // ���Ƀ`�F�b�N�����N���X�ł���ꍇ�͉��������ɏI������
        if (checkedClasses.contains(classInfo)) {
            return new LinkedList<FieldInfo>();
        }

        // �����N���X�ł���Ή��������ɏI������
        if (classInfo instanceof AnonymousClassInfo) {
            return new LinkedList<FieldInfo>();
        }

        final List<FieldInfo> availableFields = new LinkedList<FieldInfo>();

        // ���N���X�Œ�`����Ă���C���O��ԉ��������t�B�[���h��ǉ�
        // for (final TargetFieldInfo definedField : classInfo.getDefinedFields()) {
        // if (definedField.isNamespaceVisible()) {
        // availableFields.add(definedField);
        // }
        // }
        availableFields.addAll(classInfo.getDefinedFields());
        checkedClasses.add(classInfo);

        // �����N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {
            final List<FieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses((ClassInfo) innerClass, checkedClasses);
            availableFields.addAll(availableFieldsDefinedInInnerClasses);
        }

        // �e�N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final ClassInfo superClass : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            final List<FieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                    .getAvailableFieldsDefinedInSuperClasses(classInfo, superClass, checkedClasses);
            availableFields.addAll(availableFieldsDefinedInSuperClasses);
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
    private static List<FieldInfo> getAvailableFieldsDefinedInSuperClasses(
            final ClassInfo subClass, final ClassInfo superClass,
            final Set<ClassInfo> checkedClasses) {

        if ((null == subClass) || (null == superClass) || (null == checkedClasses)) {
            throw new NullPointerException();
        }

        // ���Ƀ`�F�b�N�����N���X�ł���ꍇ�͉��������ɏI������
        if (checkedClasses.contains(superClass)) {
            return new LinkedList<FieldInfo>();
        }

        final List<FieldInfo> availableFields = new LinkedList<FieldInfo>();

        // ���N���X�Œ�`����Ă���C�N���X�K�w���������t�B�[���h��ǉ�
        for (final FieldInfo definedField : superClass.getDefinedFields()) {

            // �q�N���X�Ɛe�N���X�̖��O��Ԃ������ꍇ�́C���O��ԉ��������͌p����������΂悢
            if (subClass.getNamespace().equals(superClass.getNamespace())) {

                if (definedField.isInheritanceVisible() || definedField.isNamespaceVisible()) {
                    availableFields.add(definedField);
                }

                //�q�N���X�Ɛe�N���X�̖��O��Ԃ��Ⴄ�ꍇ�́C�p����������΂悢
            } else {
                if (definedField.isInheritanceVisible()) {
                    availableFields.add(definedField);
                }
            }
        }
        checkedClasses.add(superClass);

        // �����N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final InnerClassInfo innerClass : superClass.getInnerClasses()) {
            final List<FieldInfo> availableFieldsDefinedInInnerClasses = NameResolver
                    .getAvailableFieldsDefinedInInnerClasses((ClassInfo) innerClass, checkedClasses);
            for (final FieldInfo field : availableFieldsDefinedInInnerClasses) {

                // �q�N���X�Ɛe�N���X�̖��O��Ԃ������ꍇ�́C���O��ԉ��������͌p����������΂悢
                if (subClass.getNamespace().equals(superClass.getNamespace())) {

                    if (field.isInheritanceVisible() || field.isNamespaceVisible()) {
                        availableFields.add(field);
                    }

                } else {

                    //�q�N���X�Ɛe�N���X�̖��O��Ԃ��Ⴄ�ꍇ�́C�p����������΂悢
                    if (field.isInheritanceVisible()) {
                        availableFields.add(field);
                    }
                }
            }
        }

        // �e�N���X�Œ�`���ꂽ�t�B�[���h��ǉ�
        for (final ClassInfo superSuperClass : ClassTypeInfo.convert(superClass.getSuperClasses())) {
            final List<FieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                    .getAvailableFieldsDefinedInSuperClasses(subClass, superSuperClass,
                            checkedClasses);
            availableFields.addAll(availableFieldsDefinedInSuperClasses);
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
    private static List<MethodInfo> getAvailableMethods(final ClassInfo currentClass) {

        if (null == currentClass) {
            throw new IllegalArgumentException();
        }

        // �`�F�b�N�����N���X�����邽�߂̃L���b�V���C�L���b�V���ɂ���N���X�͓�x�ڂ̓t�B�[���h�擾���Ȃ��i���[�v�\���΍�j
        final Set<ClassInfo> checkedClasses = new HashSet<ClassInfo>();

        // ���p�\�ȕϐ��������邽�߂̃��X�g
        final List<MethodInfo> availableMethods = new LinkedList<MethodInfo>();

        // �ł��O���̃N���X���擾
        final ClassInfo outestClass;
        if (currentClass instanceof InnerClassInfo) {
            outestClass = NameResolver.getOuterstClass((InnerClassInfo) currentClass);

            // ���N���X�Œ�`���ꂽ���\�b�h��ǉ�
            availableMethods.addAll(currentClass.getDefinedMethods());
            checkedClasses.add(currentClass);

            // �����N���X�Œ�`���ꂽ���\�b�h��ǉ�
            for (final InnerClassInfo innerClass : currentClass.getInnerClasses()) {
                final List<MethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                        .getAvailableMethodsDefinedInInnerClasses((ClassInfo) innerClass,
                                checkedClasses);
                availableMethods.addAll(availableMethodsDefinedInInnerClasses);
            }

            // �e�N���X�Œ�`���ꂽ���\�b�h��ǉ�
            for (final ClassInfo superClass : ClassTypeInfo.convert(currentClass.getSuperClasses())) {
                final List<MethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsDefinedInSuperClasses(outestClass, superClass,
                                checkedClasses);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }

        } else {
            outestClass = currentClass;
        }

        // �ł��O���̃N���X�Œ�`���ꂽ���\�b�h��ǉ�
        availableMethods.addAll(outestClass.getDefinedMethods());
        checkedClasses.add(outestClass);

        // �����N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final InnerClassInfo innerClass : outestClass.getInnerClasses()) {
            final List<MethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                    .getAvailableMethodsDefinedInInnerClasses((ClassInfo) innerClass,
                            checkedClasses);
            availableMethods.addAll(availableMethodsDefinedInInnerClasses);
        }

        // �e�N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final ClassInfo superClass : ClassTypeInfo.convert(outestClass.getSuperClasses())) {
            final List<MethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                    .getAvailableMethodsDefinedInSuperClasses(outestClass, superClass,
                            checkedClasses);
            availableMethods.addAll(availableMethodsDefinedInSuperClasses);
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * �����ŗ^����ꂽ�N���X�Ƃ��̓����N���X�Œ�`���ꂽ���\�b�h�̂����C�O���̃N���X�ŗ��p�\�ȃ��\�b�h�� List ��Ԃ�
     * 
     * @param innerClassInfo �N���X
     * @param checkedClasses ���Ƀ`�F�b�N�����N���X�̃L���b�V��
     * @return �O���̃N���X�ŗ��p�\�ȃ��\�b�h�� List
     */
    private static List<MethodInfo> getAvailableMethodsDefinedInInnerClasses(
            final ClassInfo innerClassInfo, final Set<ClassInfo> checkedClasses) {

        if ((null == innerClassInfo) || (null == checkedClasses)) {
            throw new IllegalArgumentException();
        }

        // ���Ƀ`�F�b�N�����N���X�ł���ꍇ�͉��������ɏI������
        if (checkedClasses.contains(innerClassInfo)) {
            return new LinkedList<MethodInfo>();
        }

        // �����N���X�ł���Ή��������ɏI������
        if (innerClassInfo instanceof AnonymousClassInfo) {
            return new LinkedList<MethodInfo>();
        }

        final List<MethodInfo> availableMethods = new LinkedList<MethodInfo>();

        final ClassInfo classInfo = (ClassInfo) innerClassInfo;

        availableMethods.addAll(classInfo.getDefinedMethods());
        checkedClasses.add(classInfo);

        // �����N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final InnerClassInfo innerClass : classInfo.getInnerClasses()) {
            final List<MethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                    .getAvailableMethodsDefinedInInnerClasses((ClassInfo) innerClass,
                            checkedClasses);
            availableMethods.addAll(availableMethodsDefinedInInnerClasses);
        }

        // �e�N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final ClassInfo superClass : ClassTypeInfo.convert(classInfo.getSuperClasses())) {
            final List<MethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                    .getAvailableMethodsDefinedInSuperClasses(classInfo, superClass, checkedClasses);
            availableMethods.addAll(availableMethodsDefinedInSuperClasses);
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
    private static List<MethodInfo> getAvailableMethodsDefinedInSuperClasses(
            final ClassInfo subClass, final ClassInfo superClass,
            final Set<ClassInfo> checkedClasses) {

        if ((null == subClass) || (null == superClass) || (null == checkedClasses)) {
            throw new IllegalArgumentException();
        }

        // ���Ƀ`�F�b�N�����N���X�ł���ꍇ�͉��������ɏI������
        if (checkedClasses.contains(superClass)) {
            return new LinkedList<MethodInfo>();
        }

        final List<MethodInfo> availableMethods = new LinkedList<MethodInfo>();

        // ���N���X�Œ�`����Ă���C�N���X�K�w�����������\�b�h��ǉ�
        for (final MethodInfo definedMethod : superClass.getDefinedMethods()) {

            // �q�N���X�Ɛe�N���X�̖��O��Ԃ������ꍇ�́C���O��ԉ��������͌p����������΂悢
            if (subClass.getNamespace().equals(superClass.getNamespace())) {

                if (definedMethod.isInheritanceVisible() || definedMethod.isNamespaceVisible()) {
                    availableMethods.add(definedMethod);
                }

                // �q�N���X�Ɛe�N���X�̖��O��Ԃ��قȂ�ꍇ�́C�p����������΂悢
            } else {

                if (definedMethod.isInheritanceVisible()) {
                    availableMethods.add(definedMethod);
                }
            }
        }
        checkedClasses.add(superClass);

        // �����N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final InnerClassInfo innerClass : superClass.getInnerClasses()) {
            final List<MethodInfo> availableMethodsDefinedInInnerClasses = NameResolver
                    .getAvailableMethodsDefinedInInnerClasses((ClassInfo) innerClass,
                            checkedClasses);
            for (final MethodInfo method : availableMethodsDefinedInInnerClasses) {

                // �q�N���X�Ɛe�N���X�̖��O��Ԃ������ꍇ�́C���O��ԉ��������͌p����������΂悢
                if (subClass.getNamespace().equals(superClass.getNamespace())) {

                    if (method.isInheritanceVisible() || method.isNamespaceVisible()) {
                        availableMethods.add(method);
                    }

                    // �q�N���X�Ɛe�N���X�̖��O��Ԃ��قȂ�ꍇ�́C�p����������΂悢
                } else {

                    if (method.isInheritanceVisible()) {
                        availableMethods.add(method);
                    }
                }
            }
        }

        // �e�N���X�Œ�`���ꂽ���\�b�h��ǉ�
        for (final ClassInfo superSuperClass : ClassTypeInfo.convert(superClass.getSuperClasses())) {
            final List<MethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                    .getAvailableMethodsDefinedInSuperClasses(subClass, superSuperClass,
                            checkedClasses);
            availableMethods.addAll(availableMethodsDefinedInSuperClasses);
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
    public static List<FieldInfo> getAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        // �g�p�����N���X�̍ł��O���̃N���X���擾
        final ClassInfo usedOutestClass;
        if (usedClass instanceof InnerClassInfo) {
            usedOutestClass = NameResolver.getOuterstClass((InnerClassInfo) usedClass);
        } else {
            usedOutestClass = usedClass;
        }

        // �g�p����N���X�̍ł��O���̃N���X���擾
        final ClassInfo usingOutestClass;
        if (usingClass instanceof InnerClassInfo) {
            usingOutestClass = NameResolver.getOuterstClass((InnerClassInfo) usingClass);
        } else {
            usingOutestClass = usingClass;
        }

        // ���̃N���X�Œ�`����Ă���t�B�[���h�̂����C�g�p����N���X�ŗ��p�\�ȃt�B�[���h���擾����
        // 2�̃N���X�������ꍇ�C�S�Ẵt�B�[���h�����p�\
        if (usedOutestClass.equals(usingOutestClass)) {

            return NameResolver.getAvailableFields(usedClass);

            // 2�̃N���X���������O��Ԃ������Ă���ꍇ
        } else if (usedOutestClass.getNamespace().equals(usingOutestClass.getNamespace())) {

            final List<FieldInfo> availableFields = new LinkedList<FieldInfo>();

            // ���O��ԉ������������t�B�[���h�݂̂����p�\
            for (final FieldInfo field : NameResolver.getAvailableFields(usedClass)) {
                if (field.isNamespaceVisible()) {
                    availableFields.add(field);
                }
            }

            return Collections.unmodifiableList(availableFields);

            // �Ⴄ���O��Ԃ������Ă���ꍇ
        } else {

            final List<FieldInfo> availableFields = new LinkedList<FieldInfo>();

            // �S���������t�B�[���h�݂̂����p�\
            for (final FieldInfo field : NameResolver.getAvailableFields(usedClass)) {
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
    public static List<MethodInfo> getAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new IllegalArgumentException();
        }

        // �g�p�����N���X�̍ł��O���̃N���X���擾
        final ClassInfo usedOutestClass;
        if (usedClass instanceof InnerClassInfo) {
            usedOutestClass = NameResolver.getOuterstClass((InnerClassInfo) usedClass);
        } else {
            usedOutestClass = usedClass;
        }

        // �g�p����N���X�̍ł��O���̃N���X���擾
        final ClassInfo usingOutestClass;
        if (usingClass instanceof InnerClassInfo) {
            usingOutestClass = NameResolver.getOuterstClass((InnerClassInfo) usingClass);
        } else {
            usingOutestClass = usingClass;
        }

        // ���̃N���X�Œ�`����Ă��郁�\�b�h�̂����C�g�p����N���X�ŗ��p�\�ȃ��\�b�h���擾����
        // 2�̃N���X�������ꍇ�C�S�Ẵ��\�b�h�����p�\
        if (usedOutestClass.equals(usingOutestClass)) {

            return NameResolver.getAvailableMethods(usedClass);

            // 2�̃N���X���������O��Ԃ������Ă���ꍇ
        } else if (usedOutestClass.getNamespace().equals(usingOutestClass.getNamespace())) {

            final List<MethodInfo> availableMethods = new LinkedList<MethodInfo>();

            // ���O��ԉ��������������\�b�h�݂̂����p�\
            for (final MethodInfo method : NameResolver.getAvailableMethods(usedClass)) {
                if (method.isNamespaceVisible()) {
                    availableMethods.add(method);
                }
            }

            return Collections.unmodifiableList(availableMethods);

            // �Ⴄ���O��Ԃ������Ă���ꍇ
        } else {

            final List<MethodInfo> availableMethods = new LinkedList<MethodInfo>();

            // �S�����������\�b�h�݂̂����p�\
            for (final MethodInfo method : NameResolver.getAvailableMethods(usedClass)) {
                if (method.isPublicVisible()) {
                    availableMethods.add(method);
                }
            }

            return Collections.unmodifiableList(availableMethods);
        }
    }

    /**
     * �����ŗ^����ꂽ�N���X�^�ŌĂяo���\�ȃR���X�g���N�^��List��Ԃ�
     * 
     * @param classType
     * @return
     */
    public static final List<ConstructorInfo> getAvailableConstructors(final ClassTypeInfo classType) {

        final List<ConstructorInfo> constructors = new LinkedList<ConstructorInfo>();
        final ClassInfo classInfo = classType.getReferencedClass();

        constructors.addAll(classInfo.getDefinedConstructors());

        for (final ClassTypeInfo superClassType : classInfo.getSuperClasses()) {
            final List<ConstructorInfo> superConstructors = NameResolver
                    .getAvailableConstructors(superClassType);
            constructors.addAll(superConstructors);
        }

        return constructors;
    }

    /**
     * �����ŗ^����ꂽ�N���X�̒��ڂ̃C���i�[�N���X��Ԃ��D�e�N���X�Œ�`���ꂽ�C���i�[�N���X���܂܂��D
     * 
     * @param classInfo �N���X
     * @return �����ŗ^����ꂽ�N���X�̒��ڂ̃C���i�[�N���X�C�e�N���X�Œ�`���ꂽ�C���i�[�N���X���܂܂��D
     */
    public static final SortedSet<InnerClassInfo> getAvailableDirectInnerClasses(
            final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final SortedSet<InnerClassInfo> availableDirectInnerClasses = new TreeSet<InnerClassInfo>();

        // �����ŗ^����ꂽ�N���X�̒��ڂ̃C���i�[�N���X��ǉ�
        availableDirectInnerClasses.addAll(classInfo.getInnerClasses());

        // �e�N���X�ɑ΂��čċA�I�ɏ���
        for (final ClassInfo superClassInfo : ClassTypeInfo.convert(classInfo.getSuperClasses())) {

            final SortedSet<InnerClassInfo> availableDirectInnerClassesInSuperClass = NameResolver
                    .getAvailableDirectInnerClasses((ClassInfo) superClassInfo);
            availableDirectInnerClasses.addAll(availableDirectInnerClassesInSuperClass);
        }

        return Collections.unmodifiableSortedSet(availableDirectInnerClasses);
    }

    public static final List<TypeParameterInfo> getAvailableTypeParameters(
            final TypeParameterizable unit) {

        if (null == unit) {
            throw new IllegalArgumentException();
        }

        final List<TypeParameterInfo> typeParameters = new LinkedList<TypeParameterInfo>();

        typeParameters.addAll(unit.getTypeParameters());
        final TypeParameterizable outerUnit = unit.getOuterTypeParameterizableUnit();
        if (null != outerUnit) {
            typeParameters.addAll(getAvailableTypeParameters(outerUnit));
        }

        return Collections.unmodifiableList(typeParameters);
    }
}
