package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������N���X�Q�Ƃ�\���N���X
 * 
 * @author higo
 * 
 */
public class UnresolvedClassReferenceInfo extends UnresolvedEntityUsageInfo<EntityUsageInfo> {

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     */
    public UnresolvedClassReferenceInfo(final List<AvailableNamespaceInfo> availableNamespaces,
            final String[] referenceName) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        this.referenceName = referenceName;
        this.fullReferenceName = referenceName;
        this.qualifierUsage = null;
        this.typeArguments = new LinkedList<UnresolvedReferenceTypeInfo<?>>();
    }

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     * @param ownerUsage �e�Q��
     */
    public UnresolvedClassReferenceInfo(final List<AvailableNamespaceInfo> availableNamespaces,
            final String[] referenceName, final UnresolvedClassReferenceInfo ownerUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName) || (null == ownerUsage)) {
            throw new NullPointerException();
        }

        this.availableNamespaces = availableNamespaces;
        String[] ownerReferenceName = ownerUsage.getFullReferenceName();
        String[] fullReferenceName = new String[referenceName.length + ownerReferenceName.length];
        System.arraycopy(ownerReferenceName, 0, fullReferenceName, 0, ownerReferenceName.length);
        System.arraycopy(referenceName, 0, fullReferenceName, ownerReferenceName.length,
                referenceName.length);
        this.fullReferenceName = fullReferenceName;
        this.referenceName = referenceName;
        this.qualifierUsage = ownerUsage;
        this.typeArguments = new LinkedList<UnresolvedReferenceTypeInfo<?>>();
    }

    @Override
    public EntityUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        //�@�ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // �v�f�g�p�̃I�[�i�[�v�f��Ԃ�
        final UnresolvedExecutableElementInfo<?> unresolvedOwnerExecutableElement = this
                .getOwnerExecutableElement();
        final ExecutableElementInfo ownerExecutableElement = unresolvedOwnerExecutableElement
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);

        final String[] referenceName = this.getReferenceName();

        if (this.hasOwnerReference()) {

            final UnresolvedClassReferenceInfo unresolvedClassReference = this.getQualifierUsage();
            EntityUsageInfo classReference = unresolvedClassReference.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            assert null != classReference : "null is returned!";

            NEXT_NAME: for (int i = 0; i < referenceName.length; i++) {

                // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                if (classReference.getType() instanceof UnknownTypeInfo) {

                    this.resolvedInfo = new UnknownEntityUsageInfo(ownerExecutableElement,
                            fromLine, fromColumn, toLine, toColumn);
                    return this.resolvedInfo;

                    // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                } else if (classReference.getType() instanceof ClassTypeInfo) {

                    final ClassInfo ownerClass = ((ClassTypeInfo) classReference.getType())
                            .getReferencedClass();
                    if (ownerClass instanceof TargetClassInfo) {

                        // �C���i�[�N���X����T���̂ňꗗ���擾
                        final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                .getAvailableDirectInnerClasses((TargetClassInfo) ((ClassTypeInfo) classReference
                                        .getType()).getReferencedClass());
                        for (final TargetInnerClassInfo innerClass : innerClasses) {

                            // ��v����N���X�������������ꍇ
                            if (referenceName[i].equals(innerClass.getClassName())) {
                                // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                // TODO �^�p�����[�^����ǋL���鏈�����K�v
                                final ClassTypeInfo reference = new ClassTypeInfo(innerClass);
                                classReference = new ClassReferenceInfo(ownerExecutableElement,
                                        reference, fromLine, fromColumn, toLine, toColumn);
                                continue NEXT_NAME;
                            }
                        }

                        assert false : "Here shouldn't be reached!";

                        // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                    } else if (ownerClass instanceof ExternalClassInfo) {

                        classReference = new UnknownEntityUsageInfo(ownerExecutableElement,
                                fromLine, fromColumn, toLine, toColumn);
                        continue NEXT_NAME;
                    }
                }

                assert false : "Here shouldn't be reached!";
            }

            this.resolvedInfo = classReference;
            return this.resolvedInfo;

        } else {

            // �������Q�ƌ^�� UnresolvedFullQualifiedNameReferenceTypeInfo �Ȃ�΁C���S���薼�Q�Ƃł���Ɣ��f�ł���
            if (this instanceof UnresolvedFullQualifiedNameClassReferenceInfo) {

                ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null == classInfo) {
                    classInfo = new ExternalClassInfo(referenceName);
                    classInfoManager.add((ExternalClassInfo) classInfo);
                }

                // TODO �^�p�����[�^����ǋL���鏈�����K�v
                final ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                this.resolvedInfo = new ClassReferenceInfo(ownerExecutableElement, reference,
                        fromLine, fromColumn, toLine, toColumn);
                return this.resolvedInfo;
            }

            // �Q�Ɩ������S���薼�ł���Ƃ��Č���
            {
                final ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null != classInfo) {

                    // TODO�@�^�p�����[�^����ǋL���鏈�����K�v
                    final ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                    this.resolvedInfo = new ClassReferenceInfo(ownerExecutableElement, reference,
                            fromLine, fromColumn, toLine, toColumn);
                    return this.resolvedInfo;
                }
            }

            // ���p�\�ȃC���i�[�N���X������T��
            {
                final TargetClassInfo outestClass;
                if (usingClass instanceof TargetInnerClassInfo) {
                    outestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usingClass);
                } else {
                    outestClass = usingClass;
                }

                for (final TargetInnerClassInfo innerClassInfo : NameResolver
                        .getAvailableInnerClasses(outestClass)) {

                    if (innerClassInfo.getClassName().equals(referenceName[0])) {

                        // availableField.getType() ���玟��word(name[i])�𖼑O����
                        // TODO �^�p�����[�^�����i�[���鏈�����K�v
                        ClassTypeInfo reference = new ClassTypeInfo(innerClassInfo);
                        EntityUsageInfo classReference = new ClassReferenceInfo(
                                ownerExecutableElement, reference, fromLine, fromColumn, toLine,
                                toColumn);
                        NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                            // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                            if (classReference.getType() instanceof UnknownTypeInfo) {

                                this.resolvedInfo = new UnknownEntityUsageInfo(
                                        ownerExecutableElement, fromLine, fromColumn, toLine,
                                        toColumn);
                                return this.resolvedInfo;

                                // �e���N���X�^�̏ꍇ
                            } else if (classReference.getType() instanceof ClassTypeInfo) {

                                final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                        .getType()).getReferencedClass();

                                // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                                if (ownerClass instanceof TargetClassInfo) {

                                    // �C���i�[�N���X����T���̂ňꗗ���擾
                                    final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                            .getAvailableDirectInnerClasses((TargetClassInfo) ((ClassTypeInfo) classReference
                                                    .getType()).getReferencedClass());
                                    for (final TargetInnerClassInfo innerClass : innerClasses) {

                                        // ��v����N���X�������������ꍇ
                                        if (referenceName[i].equals(innerClass.getClassName())) {
                                            // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                            // TODO�@�^�p�����[�^�����i�[���鏈�����K�v
                                            reference = new ClassTypeInfo(innerClass);
                                            classReference = new ClassReferenceInfo(
                                                    ownerExecutableElement, reference, fromLine,
                                                    fromColumn, toLine, toColumn);
                                            continue NEXT_NAME;
                                        }
                                    }

                                    assert false : "Here shouldn't be reached!";

                                    // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                } else if (ownerClass instanceof ExternalClassInfo) {

                                    classReference = new UnknownEntityUsageInfo(
                                            ownerExecutableElement, fromLine, fromColumn, toLine,
                                            toColumn);
                                    continue NEXT_NAME;
                                }
                            }

                            assert false : "Here shouldn't be reached!";
                        }

                        this.resolvedInfo = classReference;
                        return this.resolvedInfo;
                    }
                }
            }

            // ���p�\�Ȗ��O��Ԃ���^����T��
            {
                for (final AvailableNamespaceInfo availableNamespace : this
                        .getAvailableNamespaces()) {

                    // ���O��Ԗ�.* �ƂȂ��Ă���ꍇ
                    if (availableNamespace.isAllClasses()) {
                        final String[] namespace = availableNamespace.getNamespace();

                        // ���O��Ԃ̉��ɂ���e�N���X�ɑ΂���
                        for (final ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {

                            // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                            final String className = classInfo.getClassName();
                            if (className.equals(referenceName[0])) {

                                // availableField.getType() ���玟��word(name[i])�𖼑O����
                                // TODO �^�p�����[�^�����i�[���鏈�����K�v
                                ClassTypeInfo reference = new ClassTypeInfo(classInfo);
                                EntityUsageInfo classReference = new ClassReferenceInfo(
                                        ownerExecutableElement, reference, fromLine, fromColumn,
                                        toLine, toColumn);
                                NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                                    // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                                    if (classReference.getType() instanceof UnknownTypeInfo) {

                                        this.resolvedInfo = new UnknownEntityUsageInfo(
                                                ownerExecutableElement, fromLine, fromColumn,
                                                toLine, toColumn);
                                        return this.resolvedInfo;

                                        // �e���N���X�^�̏ꍇ
                                    } else if (classReference.getType() instanceof ClassTypeInfo) {

                                        final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                                .getType()).getReferencedClass();

                                        // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ                                         
                                        if (ownerClass instanceof TargetClassInfo) {

                                            // �C���i�[�N���X����T���̂ňꗗ���擾
                                            final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                    .getAvailableDirectInnerClasses((TargetClassInfo) ((ClassTypeInfo) classReference
                                                            .getType()).getReferencedClass());
                                            for (final TargetInnerClassInfo innerClass : innerClasses) {

                                                // ��v����N���X�������������ꍇ
                                                if (referenceName[i].equals(innerClass
                                                        .getClassName())) {
                                                    // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                    // TODO �^�p�����[�^�����i�[���鏈�����K�v
                                                    reference = new ClassTypeInfo(innerClass);
                                                    classReference = new ClassReferenceInfo(
                                                            ownerExecutableElement, reference,
                                                            fromLine, fromColumn, toLine, toColumn);
                                                    continue NEXT_NAME;
                                                }
                                            }

                                            // ������Ȃ������̂� null ��Ԃ��D
                                            // ���݂̑z��ł́C���̕����ɓ���������̂͌p���֌W�̖��O���������S�ɏI����Ă��Ȃ��i�K�݂̂̂͂��D
                                            assert false : "Here shouldn't be reached!";
                                            return null;

                                            // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                        } else if (ownerClass instanceof ExternalClassInfo) {

                                            classReference = new UnknownEntityUsageInfo(
                                                    ownerExecutableElement, fromLine, fromColumn,
                                                    toLine, toColumn);
                                            continue NEXT_NAME;
                                        }
                                    }

                                    assert false : "Here shouldn't be reached!";
                                }

                                this.resolvedInfo = classReference;
                                return this.resolvedInfo;
                            }
                        }

                        // ���O���.�N���X�� �ƂȂ��Ă���ꍇ
                    } else {

                        final String[] importName = availableNamespace.getImportName();

                        // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                        if (importName[importName.length - 1].equals(referenceName[0])) {

                            ClassInfo specifiedClassInfo = classInfoManager
                                    .getClassInfo(importName);
                            if (null == specifiedClassInfo) {
                                specifiedClassInfo = new ExternalClassInfo(importName);
                                classInfoManager.add((ExternalClassInfo) specifiedClassInfo);
                            }

                            // TODO �^�p�����[�^�����i�[���鏈�����K�v
                            ClassTypeInfo reference = new ClassTypeInfo(specifiedClassInfo);
                            EntityUsageInfo classReference = new ClassReferenceInfo(
                                    ownerExecutableElement, reference, fromLine, fromColumn,
                                    toLine, toColumn);
                            NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                                // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                                if (classReference.getType() instanceof UnknownTypeInfo) {

                                    this.resolvedInfo = new UnknownEntityUsageInfo(
                                            ownerExecutableElement, fromLine, fromColumn, toLine,
                                            toColumn);
                                    return this.resolvedInfo;

                                    // �e���N���X�^�̏ꍇ
                                } else if (classReference.getType() instanceof ClassTypeInfo) {

                                    final ClassInfo ownerClass = ((ClassTypeInfo) classReference
                                            .getType()).getReferencedClass();

                                    // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                                    if (ownerClass instanceof TargetClassInfo) {

                                        // �C���i�[�N���X�ꗗ���擾
                                        final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses((TargetClassInfo) ((ClassTypeInfo) classReference
                                                        .getType()).getReferencedClass());
                                        for (final TargetInnerClassInfo innerClass : innerClasses) {

                                            // ��v����N���X�������������ꍇ
                                            if (referenceName[i].equals(innerClass.getClassName())) {
                                                // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                // TODO �^�p�����[�^�����i�[���鏈�����K�v
                                                reference = new ClassTypeInfo(innerClass);
                                                classReference = new ClassReferenceInfo(
                                                        ownerExecutableElement, reference,
                                                        fromLine, fromColumn, toLine, toColumn);
                                                continue NEXT_NAME;
                                            }
                                        }

                                        // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                    } else if (ownerClass instanceof ExternalClassInfo) {

                                        classReference = new UnknownEntityUsageInfo(
                                                ownerExecutableElement, fromLine, fromColumn,
                                                toLine, toColumn);
                                        continue NEXT_NAME;
                                    }
                                }

                                assert false : "Here shouldn't be reached!";
                            }

                            this.resolvedInfo = classReference;
                            return this.resolvedInfo;
                        }
                    }
                }
            }
        }

        /*
         * if (null == usingMethod) { err.println("Remain unresolved \"" +
         * reference.getReferenceName(Settings.getLanguage().getNamespaceDelimiter()) + "\"" + " on
         * \"" + usingClass.getFullQualifiedtName(LANGUAGE.JAVA.getNamespaceDelimiter())); } else {
         * err.println("Remain unresolved \"" +
         * reference.getReferenceName(Settings.getLanguage().getNamespaceDelimiter()) + "\"" + " on
         * \"" + usingClass.getFullQualifiedtName(LANGUAGE.JAVA.getNamespaceDelimiter()) + "#" +
         * usingMethod.getMethodName() + "\"."); }
         */

        // ������Ȃ������ꍇ�́CUknownTypeInfo ��Ԃ�
        this.resolvedInfo = new UnknownEntityUsageInfo(ownerExecutableElement, fromLine,
                fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * �^�p�����[�^�g�p��ǉ�����
     * 
     * @param typeArgument �ǉ�����^�p�����[�^�g�p
     */
    public final void addTypeArgument(final UnresolvedReferenceTypeInfo<?> typeArgument) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeArgument) {
            throw new NullPointerException();
        }

        this.typeArguments.add(typeArgument);
    }

    /**
     * ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List ��Ԃ�
     * 
     * @return ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List
     */
    public final List<UnresolvedReferenceTypeInfo<?>> getTypeArguments() {
        return Collections.unmodifiableList(this.typeArguments);
    }

    /**
     * ���̎Q�ƌ^��owner���܂߂��Q�Ɩ���Ԃ�
     * 
     * @return ���̎Q�ƌ^��owner���܂߂��Q�Ɩ���Ԃ�
     */
    public final String[] getFullReferenceName() {
        return this.fullReferenceName;
    }

    /**
     * ���̎Q�ƌ^�̎Q�Ɩ���Ԃ�
     * 
     * @return ���̎Q�ƌ^�̎Q�Ɩ���Ԃ�
     */
    public final String[] getReferenceName() {
        return this.referenceName;
    }

    /**
     * ���̎Q�ƌ^���������Ă��関�����Q�ƌ^��Ԃ�
     * 
     * @return ���̎Q�ƌ^���������Ă��関�����Q�ƌ^
     */
    public final UnresolvedClassReferenceInfo getQualifierUsage() {
        return this.qualifierUsage;
    }

    /**
     * ���̎Q�ƌ^���C���̎Q�ƌ^�ɂ������Ă��邩�ǂ�����Ԃ�
     * 
     * @return �������Ă���ꍇ�� true�C�������Ă��Ȃ��ꍇ�� false
     */
    public final boolean hasOwnerReference() {
        return null != this.qualifierUsage;
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
    public final List<AvailableNamespaceInfo> getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * �������Q�ƌ^��^����ƁC���̖������N���X�Q�Ƃ�Ԃ�
     * 
     * @param referenceType �������Q�ƌ^
     * @return �������N���X�Q��
     */
    public final static UnresolvedClassReferenceInfo createClassReference(
            final UnresolvedClassTypeInfo referenceType, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        final UnresolvedClassReferenceInfo reference = new UnresolvedClassReferenceInfo(
                referenceType.getAvailableNamespaces(), referenceType.getReferenceName());
        reference.setFromLine(fromLine);
        reference.setFromColumn(fromColumn);
        reference.setToLine(toLine);
        reference.setToColumn(toColumn);

        return reference;
    }

    /**
     * ���p�\�Ȗ��O��Ԗ���ۑ����邽�߂̕ϐ��C���O���������̍ۂɗp����
     */
    private final List<AvailableNamespaceInfo> availableNamespaces;

    /**
     * �Q�Ɩ���ۑ�����ϐ�
     */
    private final String[] referenceName;

    /**
     * owner���܂߂��Q�Ɩ���ۑ�����ϐ�
     */
    private final String[] fullReferenceName;

    /**
     * ���̎Q�Ƃ��������Ă��関�����Q�ƌ^��ۑ�����ϐ�
     */
    private final UnresolvedClassReferenceInfo qualifierUsage;

    /**
     * �������^�p�����[�^�g�p��ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedReferenceTypeInfo<?>> typeArguments;

}
