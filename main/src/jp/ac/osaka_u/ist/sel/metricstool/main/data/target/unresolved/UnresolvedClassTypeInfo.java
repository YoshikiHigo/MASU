package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NamespaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������N���X�^��\���N���X
 * 
 * @author higo
 * 
 */
public class UnresolvedClassTypeInfo implements UnresolvedReferenceTypeInfo<ClassTypeInfo> {

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     */
    public UnresolvedClassTypeInfo(
            final List<UnresolvedClassImportStatementInfo> availableNamespaces,
            final String[] referenceName) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.referenceName = Arrays.<String> copyOf(referenceName, referenceName.length);
        this.typeArguments = new LinkedList<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>>();
    }

    /**
     * ���̖������N���X�^�����łɉ����ς݂��ǂ�����Ԃ��D
     * 
     * @return �����ς݂̏ꍇ�� true�C��������Ă��Ȃ��ꍇ�� false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * ���̖������N���X�^�̉����ς݂̌^��Ԃ�
     */
    @Override
    public ClassTypeInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    @Override
    public ClassTypeInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // import ���Ŏw�肳��Ă���N���X���o�^����Ă��Ȃ��Ȃ�C�O���N���X�Ƃ��ēo�^����
        for (final UnresolvedClassImportStatementInfo availableNamespace : this
                .getAvailableNamespaces()) {

            if (!availableNamespace.isAll()) {
                final String[] fullQualifiedName = availableNamespace.getImportName();
                if (!classInfoManager.hasClassInfo(fullQualifiedName)) {
                    final ExternalClassInfo externalClassInfo = new ExternalClassInfo(
                            fullQualifiedName);
                    classInfoManager.add(externalClassInfo);
                }
            }
        }

        final String[] referenceName = this.getReferenceName();
        final Collection<ClassInfo> classInfos = classInfoManager
                .getClassInfos(referenceName[referenceName.length - 1]);
        for (final ClassInfo classInfo : classInfos) {

            final String className = classInfo.getClassName();
            final NamespaceInfo namespace = classInfo.getNamespace();

            //�@�������Q�Ƃ̏ꍇ�́C���S���薼���ǂ����𒲂ׂ�
            if (!this.isMoniminalReference()) {

                final String[] referenceNamespace = Arrays.copyOf(referenceName,
                        referenceName.length - 1);
                if (classInfo.getNamespace().equals(referenceNamespace)) {
                    final ClassTypeInfo classType = new ClassTypeInfo(classInfo);
                    for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                            .getTypeArguments()) {
                        final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                        classType.addTypeArgument(typeArgument);
                    }
                    this.resolvedInfo = classType;
                    return this.resolvedInfo;
                }
            }

            // �P���Q�Ƃ̏ꍇ�́C�f�t�H���g�p�b�P�[�W����N���X������
            if (this.isMoniminalReference()) {

                for (final ClassInfo defaultClassInfo : classInfoManager
                        .getClassInfos(new String[0])) {

                    // �Q�Ƃ���Ă���N���X����������
                    if (referenceName[0].equals(defaultClassInfo.getClassName())) {
                        final ClassTypeInfo classType = new ClassTypeInfo(defaultClassInfo);
                        for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                .getTypeArguments()) {
                            final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                    usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                            classType.addTypeArgument(typeArgument);
                        }
                        this.resolvedInfo = classType;
                        return this.resolvedInfo;
                    }
                }
            }

            // �P�����Z�̏ꍇ�́C�C���|�[�g����Ă���N���X���猟��
            if (this.isMoniminalReference()) {

                for (final UnresolvedClassImportStatementInfo availableNamespace : this
                        .getAvailableNamespaces()) {

                    final String[] importedNamespace = availableNamespace.getNamespace();
                    if (namespace.equals(importedNamespace)) {

                        // import aaa.bbb.*�̏ꍇ (�N���X���̕�����*)
                        if (availableNamespace.isAll()) {

                            Collection<ClassInfo> importedClassInfos = classInfoManager
                                    .getClassInfos(importedNamespace);
                            for (final ClassInfo importedClassInfo : importedClassInfos) {

                                //�N���X����������
                                if (className.equals(importedClassInfo.getClassName())) {
                                    final ClassTypeInfo classType = new ClassTypeInfo(
                                            importedClassInfo);
                                    for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                            .getTypeArguments()) {
                                        final TypeInfo typeArgument = unresolvedTypeArgument
                                                .resolve(usingClass, usingMethod, classInfoManager,
                                                        fieldInfoManager, methodInfoManager);
                                        classType.addTypeArgument(typeArgument);
                                    }
                                    this.resolvedInfo = classType;
                                    return this.resolvedInfo;
                                }
                            }

                            // import aaa.bbb.Ccc �̏ꍇ (�N���X���܂Ŗ����I�ɋL�q����Ă���)
                        } else {

                            ClassInfo importedClassInfo = classInfoManager
                                    .getClassInfo(availableNamespace.getImportName());
                            if (null == importedClassInfo) {
                                importedClassInfo = new ExternalClassInfo(referenceName);
                                classInfoManager.add((ExternalClassInfo) importedClassInfo);
                            }

                            //�N���X����������
                            if (className.equals(importedClassInfo.getClassName())) {
                                final ClassTypeInfo classType = new ClassTypeInfo(importedClassInfo);
                                for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                        .getTypeArguments()) {
                                    final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                            usingClass, usingMethod, classInfoManager,
                                            fieldInfoManager, methodInfoManager);
                                    classType.addTypeArgument(typeArgument);
                                }
                                this.resolvedInfo = classType;
                                return this.resolvedInfo;
                            }
                        }
                    }
                }
            }

            // �������Q�Ƃ̏ꍇ�́Cimport����Ă���N���X�̃C���i�[�N���X�𒲂ׂ�
            if (!this.isMoniminalReference()) {

                for (final UnresolvedClassImportStatementInfo availableNamespace : this
                        .getAvailableNamespaces()) {

                    final String[] importedNamespace = availableNamespace.getNamespace();
                    if (namespace.equals(importedNamespace)) {

                        // import aaa.bbb.*�̏ꍇ (�N���X���̕�����*)
                        if (availableNamespace.isAll()) {

                            final Collection<ClassInfo> importedClassInfos = classInfoManager
                                    .getClassInfos(importedNamespace);
                            for (final ClassInfo importedClassInfo : importedClassInfos) {

                                if (importedClassInfo instanceof TargetClassInfo) {
                                    final SortedSet<ClassInfo> importedInnerClassInfos = TargetClassInfo
                                            .getAccessibleInnerClasses(importedClassInfo);

                                    for (final ClassInfo importedInnerClassInfo : importedInnerClassInfos) {

                                        if (importedInnerClassInfo.equals(classInfo)) {
                                            final ClassTypeInfo classType = new ClassTypeInfo(
                                                    classInfo);
                                            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                                    .getTypeArguments()) {
                                                final TypeInfo typeArgument = unresolvedTypeArgument
                                                        .resolve(usingClass, usingMethod,
                                                                classInfoManager, fieldInfoManager,
                                                                methodInfoManager);
                                                classType.addTypeArgument(typeArgument);
                                            }
                                            this.resolvedInfo = classType;
                                            return this.resolvedInfo;
                                        }
                                    }
                                }
                            }

                            // import aaa.bbb.Ccc �̏ꍇ
                        } else {

                            final String[] importedFullQualifiedName = availableNamespace
                                    .getImportName();
                            final ClassInfo importedClassInfo = classInfoManager
                                    .getClassInfo(importedFullQualifiedName);
                            if (importedClassInfo instanceof TargetClassInfo) {
                                final SortedSet<ClassInfo> importedInnerClassInfos = TargetClassInfo
                                        .getAllInnerClasses(importedClassInfo);

                                for (final ClassInfo importedInnerClassInfo : importedInnerClassInfos) {

                                    if (importedInnerClassInfo.equals(classInfo)) {
                                        final ClassTypeInfo classType = new ClassTypeInfo(classInfo);
                                        for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                                .getTypeArguments()) {
                                            final TypeInfo typeArgument = unresolvedTypeArgument
                                                    .resolve(usingClass, usingMethod,
                                                            classInfoManager, fieldInfoManager,
                                                            methodInfoManager);
                                            classType.addTypeArgument(typeArgument);
                                        }
                                        this.resolvedInfo = classType;
                                        return this.resolvedInfo;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //�����ɂ���̂́C�N���X��������Ȃ������Ƃ�
        if (this.isMoniminalReference()) {

            /*
            { // �P���Q�Ƃ̏ꍇ�́C�^�p�����[�^�̌^���𒲍�
                final List<TypeParameterInfo> availableTypeParameters = null == usingMethod ? NameResolver
                        .getAvailableTypeParameters(usingClass)
                        : NameResolver.getAvailableTypeParameters(usingMethod);
                for (final TypeParameterInfo typeParameter : availableTypeParameters) {
                    if (referenceName[0].equals(typeParameter.getName())) {
                        this.resolvedInfo = new TypeParameterTypeInfo(typeParameter);
                        return this.resolvedInfo;
                    }
                }
            }*/

            final ExternalClassInfo externalClassInfo = new ExternalClassInfo(referenceName[0]);
            final ClassTypeInfo classType = new ClassTypeInfo(externalClassInfo);
            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                    .getTypeArguments()) {
                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                classType.addTypeArgument(typeArgument);
            }
            this.resolvedInfo = classType;

        } else {

            final ExternalClassInfo externalClassInfo = new ExternalClassInfo(referenceName);
            final ClassTypeInfo classType = new ClassTypeInfo(externalClassInfo);
            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                    .getTypeArguments()) {
                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                classType.addTypeArgument(typeArgument);
            }
            this.resolvedInfo = classType;
        }

        return this.resolvedInfo;

        /* ��������ӂ邢����
        //�@�P���Q�Ƃ̏ꍇ
        if (this.isMoniminalReference()) {

            //�@�C���|�[�g����Ă���p�b�P�[�W���̃N���X���猟��
            for (final AvailableNamespaceInfo availableNamespace : this.getAvailableNamespaces()) {

                // import aaa.bbb.*�̏ꍇ (�N���X���̕�����*)
                if (availableNamespace.isAllClasses()) {

                    //�@���p�\�ȃN���X�ꗗ���擾���C�������猟��
                    final String[] namespace = availableNamespace.getNamespace();
                    for (final ClassInfo availableClass : classInfoManager.getClassInfos(namespace)) {

                        //�@�Q�Ƃ���Ă���N���X����������
                        if (this.referenceName[0].equals(availableClass.getClassName())) {
                            this.resolvedInfo = new ClassTypeInfo(availableClass);
                            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                    .getTypeArguments()) {
                                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                        usingClass, usingMethod, classInfoManager,
                                        fieldInfoManager, methodInfoManager);
                                this.resolvedInfo.addTypeArgument(typeArgument);
                            }
                            return this.resolvedInfo;
                        }
                    }

                    // import aaa.bbb.CCC�̏ꍇ�@(�N���X���܂ŋL�q����Ă���)
                } else {

                    ClassInfo importedClass = classInfoManager.getClassInfo(availableNamespace
                            .getImportName());

                    // null �̏ꍇ�͊O���N���X�̎Q�ƂƂ݂Ȃ�
                    if (null == importedClass) {
                        importedClass = new ExternalClassInfo(availableNamespace.getImportName());
                        classInfoManager.add((ExternalClassInfo) importedClass);
                    }

                    // import �̃N���X���Ƃ��̎Q�Ƃ���Ă���N���X������v����ꍇ�́C���̃N���X�̎Q�ƂƂ݂Ȃ�
                    final String importedClassName = importedClass.getClassName();
                    if (this.referenceName[0].equals(importedClassName)) {
                        this.resolvedInfo = new ClassTypeInfo(importedClass);
                        for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                .getTypeArguments()) {
                            final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                    usingClass, usingMethod, classInfoManager, fieldInfoManager,
                                    methodInfoManager);
                            this.resolvedInfo.addTypeArgument(typeArgument);
                        }
                        return this.resolvedInfo;
                    }
                }
            }

            // �f�t�H���g�p�b�P�[�W����N���X������
            for (final ClassInfo availableClass : classInfoManager.getClassInfos(new String[0])) {

                // �Q�Ƃ���Ă���N���X����������
                if (this.referenceName[0].equals(availableClass.getClassName())) {
                    this.resolvedInfo = new ClassTypeInfo(availableClass);
                    for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                            .getTypeArguments()) {
                        final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                        this.resolvedInfo.addTypeArgument(typeArgument);
                    }
                    return this.resolvedInfo;
                }
            }

            // �s���ȃN���X�^�ł���
            final ExternalClassInfo unknownReferencedClass = new ExternalClassInfo(
                    this.referenceName[0]);
            this.resolvedInfo = new ClassTypeInfo(unknownReferencedClass);
            return this.resolvedInfo;

            // �������Q�Ƃ̏ꍇ
        } else {

            //�@�C���|�[�g����Ă���N���X�̎q�N���X���猟��
            AVAILABLENAMESPACE: for (final AvailableNamespaceInfo availableNamespace : this
                    .getAvailableNamespaces()) {

                // import aaa.bbb.*�̏ꍇ (�N���X���̕�����*)
                if (availableNamespace.isAllClasses()) {

                    // ���p�\�ȃN���X�ꗗ���擾���C�������猟��
                    final String[] namespace = availableNamespace.getNamespace();
                    for (final ClassInfo availableClass : classInfoManager.getClassInfos(namespace)) {

                        //�@�Q�Ƃ���Ă���N���X����������
                        if (this.referenceName[0].equals(availableClass.getClassName())) {

                            // �ΏۃN���X�łȂ��ꍇ�͓����N���X���͂킩��Ȃ��̂ŃX�L�b�v
                            if (!(availableClass instanceof TargetClassInfo)) {
                                continue AVAILABLENAMESPACE;
                            }

                            // �ΏۃN���X�̏ꍇ�́C���ɓ����N���X�����ǂ��čs��
                            TargetClassInfo currentClass = (TargetClassInfo) availableClass;
                            INDEX: for (int index = 1; index < this.referenceName.length; index++) {
                                final SortedSet<TargetInnerClassInfo> innerClasses = currentClass
                                        .getInnerClasses();
                                for (final TargetInnerClassInfo innerClass : innerClasses) {

                                    if (this.referenceName[index].equals(innerClass.getClassName())) {
                                        currentClass = innerClass;
                                        continue INDEX;
                                    }

                                    // �����ɓ��B����̂́C�N���X��������Ȃ������ꍇ
                                    final ExternalClassInfo unknownReferencedClass = new ExternalClassInfo(
                                            this.referenceName[this.referenceName.length - 1]);
                                    this.resolvedInfo = new ClassTypeInfo(unknownReferencedClass);
                                    for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                            .getTypeArguments()) {
                                        final TypeInfo typeArgument = unresolvedTypeArgument
                                                .resolve(usingClass, usingMethod, classInfoManager,
                                                        fieldInfoManager, methodInfoManager);
                                        this.resolvedInfo.addTypeArgument(typeArgument);
                                    }
                                    return this.resolvedInfo;
                                }
                            }

                            //�@�����ɓ��B����̂́C�N���X�����������ꍇ
                            this.resolvedInfo = new ClassTypeInfo(currentClass);
                            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                    .getTypeArguments()) {
                                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                        usingClass, usingMethod, classInfoManager,
                                        fieldInfoManager, methodInfoManager);
                                this.resolvedInfo.addTypeArgument(typeArgument);
                            }
                            return this.resolvedInfo;
                        }
                    }

                    // import aaa.bbb.CCC�̏ꍇ (�N���X���܂ŋL�q����Ă���)
                } else {

                    // �Q�Ɩ������S���薼�Ƃ���N���X�����邩���`�F�b�N
                    {
                        final ClassInfo referencedClass = classInfoManager
                                .getClassInfo(this.referenceName);
                        if (null != referencedClass) {
                            this.resolvedInfo = new ClassTypeInfo(referencedClass);
                            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                                    .getTypeArguments()) {
                                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(
                                        usingClass, usingMethod, classInfoManager,
                                        fieldInfoManager, methodInfoManager);
                                this.resolvedInfo.addTypeArgument(typeArgument);
                            }
                            return this.resolvedInfo;
                        }
                    }

                    ClassInfo importClass = classInfoManager.getClassInfo(availableNamespace
                            .getImportName());

                    //�@null �̏ꍇ�͂���(�O��)�N���X��\���I�u�W�F�N�g���쐬 
                    if (null == importClass) {
                        importClass = new ExternalClassInfo(availableNamespace.getImportName());
                        classInfoManager.add((ExternalClassInfo) importClass);
                    }

                    // importClass���ΏۃN���X�łȂ��ꍇ�͓����N���X��񂪂킩��Ȃ��̂ŃX�L�b�v
                    if (!(importClass instanceof TargetClassInfo)) {
                        continue AVAILABLENAMESPACE;
                    }

                    //�@�ΏۃN���X�̏ꍇ�́C�Q�Ɩ��ƈ�v���Ă��邩���`�F�b�N
                    // �Q�Ɩ����C���|�[�g�������Z���ꍇ�͊Y�����Ȃ�
                    final String[] importFullQualifiedName = importClass.getFullQualifiedName();
                    if (this.referenceName.length < importFullQualifiedName.length) {
                        continue AVAILABLENAMESPACE;
                    }

                    // �Q�Ɩ����C���|�[�g���Ɠ��������C�������͂�蒷���ꍇ�͏ڂ������ׂ�
                    int index = 0;
                    for (; index < importFullQualifiedName.length; index++) {
                        if (!importFullQualifiedName[index].equals(this.referenceName[index])) {
                            continue AVAILABLENAMESPACE;
                        }
                    }

                    // �Q�Ɩ��̕��������̂ŁC�C���|�[�g�N���X�̓����N���X�����ǂ��Ĉ�v������̂����邩�𒲂ׂ�
                    TargetClassInfo currentClass = (TargetClassInfo) importClass;
                    INDEX: for (; index < this.referenceName.length; index++) {

                        for (final TargetInnerClassInfo innerClass : currentClass.getInnerClasses()) {

                            if (this.referenceName[index].equals(innerClass.getClassName())) {
                                currentClass = innerClass;
                                continue INDEX;
                            }

                            // �����ɂ���̂́C�N���X��������Ȃ������ꍇ
                            continue AVAILABLENAMESPACE;
                        }

                    }

                    //�@�����ɓ��B����̂́C�N���X�����������ꍇ
                    this.resolvedInfo = new ClassTypeInfo(currentClass);
                    for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                            .getTypeArguments()) {
                        final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                        this.resolvedInfo.addTypeArgument(typeArgument);
                    }
                    return this.resolvedInfo;
                }
            }

            // �s���ȃN���X�^�ł���
            final ExternalClassInfo unknownReferencedClass = new ExternalClassInfo(
                    this.referenceName[this.referenceName.length - 1]);
            this.resolvedInfo = new ClassTypeInfo(unknownReferencedClass);
            for (final UnresolvedTypeInfo<? extends ReferenceTypeInfo> unresolvedTypeArgument : this
                    .getTypeArguments()) {
                final TypeInfo typeArgument = unresolvedTypeArgument.resolve(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                this.resolvedInfo.addTypeArgument(typeArgument);
            }
            return this.resolvedInfo;
        }
        */
    }

    /**
     * ���p�\�Ȗ��O��ԁC�^�̊��S�C������^���ď�����
     * @param referenceName �^�̊��S�C����
     */
    public UnresolvedClassTypeInfo(final String[] referenceName) {
        this(new LinkedList<UnresolvedClassImportStatementInfo>(), referenceName);
    }

    /**
     * �^�p�����[�^�g�p��ǉ�����
     * 
     * @param typeParameterUsage �ǉ�����^�p�����[�^�g�p
     */
    public final void addTypeArgument(
            final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> typeParameterUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeParameterUsage);
    }

    /**
     * ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List ��Ԃ�
     * 
     * @return ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List
     */
    public final List<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * ���̎Q�ƌ^�̖��O��Ԃ�
     * 
     * @return ���̎Q�ƌ^�̖��O��Ԃ�
     */
    public final String getTypeName() {
        return this.referenceName[this.referenceName.length - 1];
    }

    /**
     * ���̎Q�ƌ^�̎Q�Ɩ���Ԃ�
     * 
     * @return ���̎Q�ƌ^�̎Q�Ɩ���Ԃ�
     */
    public final String[] getReferenceName() {
        return Arrays.<String> copyOf(this.referenceName, this.referenceName.length);
    }

    /**
     * ���̎Q�ƌ^�̎Q�Ɩ��������ŗ^����ꂽ�����Ō������ĕԂ�
     * 
     * @param delimiter �����ɗp���镶��
     * @return ���̎Q�ƌ^�̎Q�Ɩ��������ŗ^����ꂽ�����Ō�������������
     */
    public final String getReferenceName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        final StringBuilder sb = new StringBuilder(this.referenceName[0]);
        for (int i = 1; i < this.referenceName.length; i++) {
            sb.append(delimiter);
            sb.append(this.referenceName[i]);
        }

        return sb.toString();
    }

    /**
     * ���̎Q�ƌ^�̊��S���薼�Ƃ��ĉ\���̂��閼�O��Ԗ��̈ꗗ��Ԃ�
     * 
     * @return ���̎Q�ƌ^�̊��S���薼�Ƃ��ĉ\���̂��閼�O��Ԗ��̈ꗗ
     */
    public final List<UnresolvedClassImportStatementInfo> getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * ���̎Q�Ƃ��P�����ǂ�����Ԃ�
     * 
     * @return�@�P���ł���ꍇ��true�C�����łȂ��ꍇ��false
     */
    public final boolean isMoniminalReference() {
        return 1 == this.referenceName.length;
    }

    /**
     * �������N���X��^����ƁC���̖������Q�ƌ^��Ԃ�
     * 
     * @param referencedClass �������N���X
     * @return �^����ꂽ�������N���X�̖������Q�ƌ^
     */
    public final static UnresolvedClassTypeInfo getInstance(UnresolvedClassInfo referencedClass) {
        return new UnresolvedClassTypeInfo(referencedClass.getFullQualifiedName());
    }

    /**
     * ���̖������Q�ƌ^���\���������N���X�Q�Ƃ�Ԃ�
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     * @return ���̖������Q�ƌ^���\���������N���X�Q��
     */
    public final UnresolvedClassReferenceInfo getUsage(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        UnresolvedClassReferenceInfo usage = new UnresolvedClassReferenceInfo(
                this.availableNamespaces, this.referenceName);
        usage.setFromLine(fromLine);
        usage.setFromColumn(fromColumn);
        usage.setToLine(toLine);
        usage.setToColumn(toColumn);

        for (UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> typeArgument : this.typeArguments) {
            usage.addTypeArgument(typeArgument);
        }
        return usage;
    }

    /**
     * ���p�\�Ȗ��O��Ԗ���ۑ����邽�߂̕ϐ��C���O���������̍ۂɗp����
     */
    private final List<UnresolvedClassImportStatementInfo> availableNamespaces;

    /**
     * �Q�Ɩ���ۑ�����ϐ�
     */
    private final String[] referenceName;

    /**
     * �^������ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo>> typeArguments;

    private ClassTypeInfo resolvedInfo;

}
