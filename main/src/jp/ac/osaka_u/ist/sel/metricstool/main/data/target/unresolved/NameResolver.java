package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AnonymousClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
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

    /**
     * �g�p����N���X�Ǝg�p�����N���X��^���邱�Ƃɂ��C���p�\�ȃ��\�b�h��List��Ԃ�
     * 
     * @param usedClass �g�p�����N���X
     * @param usingClass �g�p����N���X
     * @return
     */
    public static synchronized List<MethodInfo> getAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final boolean hasCache = METHOD_CACHE.hasCash(usedClass, usingClass);
        if (hasCache) {
            return METHOD_CACHE.getCache(usedClass, usingClass);
        } else {
            final List<MethodInfo> methods = getAvailableMethods(usedClass, usingClass,
                    new HashSet<ClassInfo>());
            METHOD_CACHE.putCache(usedClass, usingClass, methods);
            return methods;
        }
    }

    /**
     * �g�p����N���X�Ǝg�p�����N���X��^���邱�Ƃɂ��C���p�\�ȃt�B�[���h��List��Ԃ�
     * 
     * @param usedClass �g�p�����N���X
     * @param usingClass �g�p����N���X
     * @return
     */
    public static synchronized List<FieldInfo> getAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final boolean hasCache = FIELD_CACHE.hasCash(usedClass, usingClass);
        if (hasCache) {
            return FIELD_CACHE.getCache(usedClass, usingClass);
        } else {
            final List<FieldInfo> fields = getAvailableFields(usedClass, usingClass,
                    new HashSet<ClassInfo>());
            FIELD_CACHE.putCache(usedClass, usingClass, fields);
            return fields;
        }
    }

    private static List<MethodInfo> getAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass, final Set<ClassInfo> checkedClasses) {

        // ���łɃ`�F�b�N���Ă���N���X�ł���Ή��������ɔ�����
        if (checkedClasses.contains(usedClass)) {
            return Collections.<MethodInfo> emptyList();
        }

        // �`�F�b�N�ς݃N���X�ɒǉ�
        checkedClasses.add(usedClass);

        // used�ɒ�`����Ă��郁�\�b�h�̂����C���p�\�Ȃ��̂�ǉ�
        final List<MethodInfo> availableMethods = new ArrayList<MethodInfo>();
        availableMethods.addAll(extractAvailableMethods(usedClass, usingClass));

        // used�̊O�N���X���`�F�b�N
        if (usedClass instanceof InnerClassInfo) {
            final ClassInfo outerClass = ((InnerClassInfo) usedClass).getOuterClass();
            availableMethods.addAll(getAvailableMethods(outerClass, usingClass, checkedClasses));
        }

        // �e�N���X���`�F�b�N
        for (final ClassTypeInfo superClassType : usedClass.getSuperClasses()) {
            final ClassInfo superClass = superClassType.getReferencedClass();
            availableMethods.addAll(getAvailableMethods(superClass, usingClass, checkedClasses));
        }

        return availableMethods;
    }

    private static List<FieldInfo> getAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass, final Set<ClassInfo> checkedClasses) {

        // ���łɃ`�F�b�N���Ă���N���X�ł���Ή��������ɔ�����
        if (checkedClasses.contains(usedClass)) {
            return Collections.<FieldInfo> emptyList();
        }

        // �`�F�b�N�ς݃N���X�ɒǉ�
        checkedClasses.add(usedClass);

        // used�ɒ�`����Ă��郁�\�b�h�̂����C���p�\�Ȃ��̂�ǉ�
        final List<FieldInfo> availableFields = new ArrayList<FieldInfo>();
        availableFields.addAll(extractAvailableFields(usedClass, usingClass));

        // used�̊O�N���X���`�F�b�N
        if (usedClass instanceof InnerClassInfo) {
            final ClassInfo outerClass = ((InnerClassInfo) usedClass).getOuterClass();
            availableFields.addAll(getAvailableFields(outerClass, usingClass, checkedClasses));
        }

        // �e�N���X���`�F�b�N
        for (final ClassTypeInfo superClassType : usedClass.getSuperClasses()) {
            final ClassInfo superClass = superClassType.getReferencedClass();
            availableFields.addAll(getAvailableFields(superClass, usingClass, checkedClasses));
        }

        return availableFields;
    }

    private static List<MethodInfo> extractAvailableMethods(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final List<MethodInfo> availableMethods = new ArrayList<MethodInfo>();

        // using��used���������ꍇ�́C���ׂẴ��\�b�h���g�p�\
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                availableMethods.addAll(usedClass.getDefinedMethods());
            }
        }

        // using��used�Ɠ����p�b�P�[�W�ł���΁Cprivate �ȊO�̃��\�b�h���g�p�\
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                for (final MethodInfo method : usedClass.getDefinedMethods()) {
                    if (method.isNamespaceVisible()) {
                        availableMethods.add(method);
                    }
                }
            }
        }

        // using��used�̃T�u�N���X�ł����,protected�ȊO�̃��\�b�h���g�p�\
        if (usingClass.isSubClass(usedClass)) {
            for (final MethodInfo method : usedClass.getDefinedMethods()) {
                if (method.isInheritanceVisible()) {
                    availableMethods.add(method);
                }
            }
        }

        // using��used�Ɗ֌W�̂Ȃ��N���X�ł���΁Cpublic�̃��\�b�h�����p�\
        for (final MethodInfo method : usedClass.getDefinedMethods()) {
            if (method.isPublicVisible()) {
                availableMethods.add(method);
            }
        }

        return availableMethods;
    }

    private static List<FieldInfo> extractAvailableFields(final ClassInfo usedClass,
            final ClassInfo usingClass) {

        final List<FieldInfo> availableFields = new ArrayList<FieldInfo>();

        // using��used���������ꍇ�́C���ׂẴt�B�[���h���g�p�\
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                availableFields.addAll(usedClass.getDefinedFields());
            }
        }

        // using��used�Ɠ����p�b�P�[�W�ł���΁Cprivate �ȊO�̃t�B�[���h���g�p�\
        {
            final ClassInfo tmpUsingClass = usingClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usingClass)
                    : usingClass;
            final ClassInfo tmpUsedClass = usedClass instanceof InnerClassInfo ? TargetInnerClassInfo
                    .getOutestClass((InnerClassInfo) usedClass)
                    : usedClass;
            if (tmpUsingClass.getNamespace().equals(tmpUsedClass.getNamespace())) {
                for (final FieldInfo field : usedClass.getDefinedFields()) {
                    if (field.isNamespaceVisible()) {
                        availableFields.add(field);
                    }
                }
            }
        }

        // using��used�̃T�u�N���X�ł����,protected�ȊO�̃t�B�[���h���g�p�\
        if (usingClass.isSubClass(usedClass)) {
            for (final FieldInfo field : usedClass.getDefinedFields()) {
                if (field.isInheritanceVisible()) {
                    availableFields.add(field);
                }
            }
        }

        // using��used�Ɗ֌W�̂Ȃ��N���X�ł���΁Cpublic�̃t�B�[���h�����p�\
        for (final FieldInfo field : usedClass.getDefinedFields()) {
            if (field.isPublicVisible()) {
                availableFields.add(field);
            }
        }

        return availableFields;
    }

    private static final Cache<MethodInfo> METHOD_CACHE = new Cache<MethodInfo>();

    private static final Cache<FieldInfo> FIELD_CACHE = new Cache<FieldInfo>();

    /**
     * �g�p����N���X�Ǝg�p�����N���X�̊֌W���痘�p�\�ȃ����o�[�̃L���b�V����~���Ă������߂̃N���X
     * 
     * @author higo
     *
     * @param <T>
     */
    static class Cache<T> {

        private final ConcurrentMap<ClassInfo, ConcurrentMap<ClassInfo, List<T>>> firstCache;

        Cache() {
            this.firstCache = new ConcurrentHashMap<ClassInfo, ConcurrentMap<ClassInfo, List<T>>>();
        }

        boolean hasCash(final ClassInfo usedClass, final ClassInfo usingClass) {

            final boolean hasSecondCache = this.firstCache.containsKey(usedClass);
            if (!hasSecondCache) {
                return false;
            }

            final ConcurrentMap<ClassInfo, List<T>> secondCache = this.firstCache.get(usedClass);
            final boolean hasThirdCache = secondCache.containsKey(usingClass);
            return hasThirdCache;
        }

        List<T> getCache(final ClassInfo usedClass, final ClassInfo usingClass) {

            final ConcurrentMap<ClassInfo, List<T>> secondCache = this.firstCache.get(usedClass);
            if (null == secondCache) {
                return null;
            }

            return secondCache.get(usingClass);
        }

        void putCache(final ClassInfo usedClass, final ClassInfo usingClass, final List<T> cache) {

            ConcurrentMap<ClassInfo, List<T>> secondCache = this.firstCache.get(usedClass);
            if (null == secondCache) {
                secondCache = new ConcurrentHashMap<ClassInfo, List<T>>();
                this.firstCache.put(usedClass, secondCache);
            }

            secondCache.put(usingClass, cache);
        }
    }
}
