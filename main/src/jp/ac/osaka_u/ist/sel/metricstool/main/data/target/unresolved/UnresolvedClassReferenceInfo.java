package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������Q�ƌ^��\���N���X
 * 
 * @author higo
 * 
 */
public class UnresolvedClassReferenceInfo implements UnresolvedEntityUsageInfo {

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     */
    public UnresolvedClassReferenceInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] referenceName) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName)) {
            throw new NullPointerException();
        }

        this.availableNamespaceSet = availableNamespaces;
        this.referenceName = referenceName;
        this.fullReferenceName = referenceName;
        this.ownerUsage = null;
        this.typeParameterUsages = new LinkedList<UnresolvedTypeParameterUsageInfo>();
    }

    /**
     * ���p�\�Ȗ��O��Ԗ��C�Q�Ɩ���^���ď�����
     * 
     * @param availableNamespaces ���O��Ԗ�
     * @param referenceName �Q�Ɩ�
     * @param ownerUsage �e�Q��
     */
    public UnresolvedClassReferenceInfo(final AvailableNamespaceInfoSet availableNamespaces,
            final String[] referenceName, final UnresolvedClassReferenceInfo ownerUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == availableNamespaces) || (null == referenceName) || (null == ownerUsage)) {
            throw new NullPointerException();
        }

        this.availableNamespaceSet = availableNamespaces;
        String[] ownerReferenceName = ownerUsage.getFullReferenceName();
        String[] fullReferenceName = new String[referenceName.length + ownerReferenceName.length];
        System.arraycopy(ownerReferenceName, 0, fullReferenceName, 0, ownerReferenceName.length);
        System.arraycopy(referenceName, 0, fullReferenceName, ownerReferenceName.length,
                referenceName.length);
        this.fullReferenceName = fullReferenceName;
        this.referenceName = referenceName;
        this.ownerUsage = ownerUsage;
        this.typeParameterUsages = new LinkedList<UnresolvedTypeParameterUsageInfo>();
    }

    /**
     * ���ɉ����ς݂��ǂ�����Ԃ��D
     * 
     * @return �����ς݂ł���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * �����ς݃N���X�Q�Ƃ�Ԃ�
     * 
     * @return �����ς݃N���X�Q��
     * @throws NotResolvedException ��������Ă��Ȃ��ꍇ�ɃX���[�����
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        final String[] referenceName = this.getReferenceName();

        if (this.hasOwnerReference()) {

            final UnresolvedClassReferenceInfo unresolvedClassReference = this.getOwnerUsage();
            EntityUsageInfo classReference = unresolvedClassReference.resolveEntityUsage(
                    usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            assert null != classReference : "null is returned!";

            NEXT_NAME: for (int i = 0; i < referenceName.length; i++) {

                // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                if (classReference.getType() instanceof UnknownTypeInfo) {

                    this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                    return this.resolvedInfo;

                    // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                } else if (classReference.getType() instanceof TargetClassInfo) {

                    // �C���i�[�N���X����T���̂ňꗗ���擾
                    final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                            .getAvailableDirectInnerClasses((TargetClassInfo) classReference
                                    .getType());
                    for (final TargetInnerClassInfo innerClass : innerClasses) {

                        // ��v����N���X�������������ꍇ
                        if (referenceName[i].equals(innerClass.getClassName())) {
                            // TODO ���p�֌W���\�z����R�[�h���K�v�H

                            // TODO �^�p�����[�^����ǋL���鏈�����K�v
                            final ReferenceTypeInfo reference = new ReferenceTypeInfo(innerClass);
                            classReference = new ClassReferenceInfo(reference);
                            continue NEXT_NAME;
                        }
                    }

                    assert false : "Here shouldn't be reached!";

                    // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                } else if (classReference.getType() instanceof ExternalClassInfo) {

                    classReference = UnknownEntityUsageInfo.getInstance();
                    continue NEXT_NAME;
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
                final ReferenceTypeInfo reference = new ReferenceTypeInfo(classInfo);
                this.resolvedInfo = new ClassReferenceInfo(reference);
                return this.resolvedInfo;
            }

            // �Q�Ɩ������S���薼�ł���Ƃ��Č���
            {
                final ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null != classInfo) {

                    // TODO�@�^�p�����[�^����ǋL���鏈�����K�v
                    final ReferenceTypeInfo reference = new ReferenceTypeInfo(classInfo);
                    this.resolvedInfo = new ClassReferenceInfo(reference);
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
                        ReferenceTypeInfo reference = new ReferenceTypeInfo(innerClassInfo);
                        EntityUsageInfo classReference = new ClassReferenceInfo(reference);
                        NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                            // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                            if (classReference.getType() instanceof UnknownTypeInfo) {

                                this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                                return this.resolvedInfo;

                                // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                            } else if (classReference.getType() instanceof TargetClassInfo) {

                                // �C���i�[�N���X����T���̂ňꗗ���擾
                                final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                        .getAvailableDirectInnerClasses((TargetClassInfo) classReference
                                                .getType());
                                for (final TargetInnerClassInfo innerClass : innerClasses) {

                                    // ��v����N���X�������������ꍇ
                                    if (referenceName[i].equals(innerClass.getClassName())) {
                                        // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                        // TODO�@�^�p�����[�^�����i�[���鏈�����K�v
                                        reference = new ReferenceTypeInfo(innerClass);
                                        classReference = new ClassReferenceInfo(reference);
                                        continue NEXT_NAME;
                                    }
                                }

                                assert false : "Here shouldn't be reached!";

                                // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                            } else if (classReference.getType() instanceof ExternalClassInfo) {

                                classReference = UnknownEntityUsageInfo.getInstance();
                                continue NEXT_NAME;
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
                                ReferenceTypeInfo reference = new ReferenceTypeInfo(classInfo);
                                EntityUsageInfo classReference = new ClassReferenceInfo(reference);
                                NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                                    // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                                    if (classReference.getType() instanceof UnknownTypeInfo) {

                                        this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                                        return this.resolvedInfo;

                                        // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                                    } else if (classReference.getType() instanceof TargetClassInfo) {

                                        // �C���i�[�N���X����T���̂ňꗗ���擾
                                        final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses((TargetClassInfo) classReference
                                                        .getType());
                                        for (final TargetInnerClassInfo innerClass : innerClasses) {

                                            // ��v����N���X�������������ꍇ
                                            if (referenceName[i].equals(innerClass.getClassName())) {
                                                // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                // TODO �^�p�����[�^�����i�[���鏈�����K�v
                                                reference = new ReferenceTypeInfo(innerClass);
                                                classReference = new ClassReferenceInfo(reference);
                                                continue NEXT_NAME;
                                            }
                                        }

                                        // ������Ȃ������̂� null ��Ԃ��D
                                        // ���݂̑z��ł́C���̕����ɓ���������̂͌p���֌W�̖��O���������S�ɏI����Ă��Ȃ��i�K�݂̂̂͂��D
                                        return null;

                                        // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                    } else if (classReference.getType() instanceof ExternalClassInfo) {

                                        classReference = UnknownEntityUsageInfo.getInstance();
                                        continue NEXT_NAME;
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
                            ReferenceTypeInfo reference = new ReferenceTypeInfo(specifiedClassInfo);
                            EntityUsageInfo classReference = new ClassReferenceInfo(reference);
                            NEXT_NAME: for (int i = 1; i < referenceName.length; i++) {

                                // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                                if (classReference.getType() instanceof UnknownTypeInfo) {

                                    this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                                    return this.resolvedInfo;

                                    // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                                } else if (classReference.getType() instanceof TargetClassInfo) {

                                    // �C���i�[�N���X�ꗗ���擾
                                    final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                            .getAvailableDirectInnerClasses((TargetClassInfo) classReference
                                                    .getType());
                                    for (final TargetInnerClassInfo innerClass : innerClasses) {

                                        // ��v����N���X�������������ꍇ
                                        if (referenceName[i].equals(innerClass.getClassName())) {
                                            // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                            // TODO �^�p�����[�^�����i�[���鏈�����K�v
                                            reference = new ReferenceTypeInfo(innerClass);
                                            classReference = new ClassReferenceInfo(reference);
                                            continue NEXT_NAME;
                                        }
                                    }

                                    // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                } else if (classReference.getType() instanceof ExternalClassInfo) {

                                    classReference = UnknownEntityUsageInfo.getInstance();
                                    continue NEXT_NAME;
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
        this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
        return this.resolvedInfo;
    }

    /**
     * �^�p�����[�^�g�p��ǉ�����
     * 
     * @param typeParameterUsage �ǉ�����^�p�����[�^�g�p
     */
    public final void addTypeParameterUsage(
            final UnresolvedTypeParameterUsageInfo typeParameterUsage) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeParameterUsages.add(typeParameterUsage);
    }

    /**
     * ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List ��Ԃ�
     * 
     * @return ���̃N���X�Q�ƂŎg�p����Ă���^�p�����[�^�� List
     */
    public final List<UnresolvedTypeParameterUsageInfo> getTypeParameterUsages() {
        return Collections.unmodifiableList(this.typeParameterUsages);
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
    public final UnresolvedClassReferenceInfo getOwnerUsage() {
        return this.ownerUsage;
    }

    /**
     * ���̎Q�ƌ^���C���̎Q�ƌ^�ɂ������Ă��邩�ǂ�����Ԃ�
     * 
     * @return �������Ă���ꍇ�� true�C�������Ă��Ȃ��ꍇ�� false
     */
    public final boolean hasOwnerReference() {
        return null != this.ownerUsage;
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
    public final AvailableNamespaceInfoSet getAvailableNamespaces() {
        return this.availableNamespaceSet;
    }

    /**
     * ���p�\�Ȗ��O��Ԗ���ۑ����邽�߂̕ϐ��C���O���������̍ۂɗp����
     */
    private final AvailableNamespaceInfoSet availableNamespaceSet;

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
    private final UnresolvedClassReferenceInfo ownerUsage;

    /**
     * �������^�p�����[�^�g�p��ۑ����邽�߂̕ϐ�
     */
    private final List<UnresolvedTypeParameterUsageInfo> typeParameterUsages;

    /**
     * �����ς݃N���X�Q�Ƃ�ۑ����邽�߂̕ϐ�
     */
    private EntityUsageInfo resolvedInfo;
}