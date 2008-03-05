package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Members;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * �������G���e�B�e�B�g�p��ۑ����邽�߂̃N���X�D �������G���e�B�e�B�g�p�Ƃ́C�p�b�P�[�W����N���X���̎Q�� ��\���D
 * 
 * @author higo
 * 
 */
public final class UnresolvedUnknownUsageInfo extends UnresolvedEntityUsageInfo {

    /**
     * �������G���e�B�e�B�g�p�I�u�W�F�N�g���쐬����D
     * 
     * @param availableNamespaces ���p�\�Ȗ��O���
     * @param name �������G���e�B�e�B�g�p��
     */
    public UnresolvedUnknownUsageInfo(final Set<AvailableNamespaceInfo> availableNamespaces,
            final String[] name, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        this.availableNamespaces = availableNamespaces;
        this.name = name;

        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);

        this.resolvedIndo = null;
    }

    /**
     * �������G���e�B�e�B�g�p����������Ă��邩�ǂ�����Ԃ�
     * 
     * @return ��������Ă���ꍇ�� true�C�����łȂ��ꍇ�� false
     */
    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedIndo;
    }

    /**
     * �����ς݃G���e�B�e�B�g�p��Ԃ�
     * 
     * @return �����ς݃G���e�B�e�B�g�p
     * @throws ��������Ă��Ȃ��ꍇ�ɃX���[�����
     */
    @Override
    public EntityUsageInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedIndo;
    }

    @Override
    public EntityUsageInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���ꍇ�́C�L���b�V����Ԃ�
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // �G���e�B�e�B�Q�Ɩ����擾
        final String[] name = this.getName();

        // �ʒu�����擾
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // ���p�\�ȃC���X�^���X�t�B�[���h������G���e�B�e�B��������
        {
            // ���̃N���X�ŗ��p�\�ȃC���X�^���X�t�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFieldsOfThisClass = Members
                    .<TargetFieldInfo> getInstanceMembers(NameResolver
                            .getAvailableFields(usingClass));

            for (final TargetFieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // ��v����t�B�[���h�������������ꍇ
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    // usingMethod.addReferencee(availableFieldOfThisClass);
                    // availableFieldOfThisClass.addReferencer(usingMethod);

                    // �e�̌^�𐶐�
                    final ClassTypeInfo usingClassType = new ClassTypeInfo(usingClass);
                    for (final TypeParameterInfo typeParameter : usingClass.getTypeParameters()) {
                        usingClassType.addTypeArgument(typeParameter);
                    }

                    // availableField.getType() ���玟��word(name[i])�𖼑O����
                    EntityUsageInfo entityUsage = new FieldUsageInfo(usingClassType,
                            availableFieldOfThisClass, true, fromLine, fromColumn, toLine, toColumn);
                    for (int i = 1; i < name.length; i++) {

                        // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                        if (entityUsage.getType() instanceof UnknownTypeInfo) {

                            this.resolvedIndo = new UnknownEntityUsageInfo(fromLine, fromColumn,
                                    toLine, toColumn);
                            return this.resolvedIndo;

                            // �e���N���X�^�̏ꍇ
                        } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                            final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                    .getReferencedClass();

                            // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                            if (ownerClass instanceof TargetClassInfo) {

                                // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                                boolean found = false;
                                {
                                    // ���p�\�ȃC���X�^���X�t�B�[���h�ꗗ���擾
                                    final List<TargetFieldInfo> availableFields = Members
                                            .getInstanceMembers(NameResolver.getAvailableFields(
                                                    (TargetClassInfo) ownerClass, usingClass));

                                    for (final TargetFieldInfo availableField : availableFields) {

                                        // ��v����t�B�[���h�������������ꍇ
                                        if (name[i].equals(availableField.getName())) {
                                            // usingMethod.addReferencee(availableField);
                                            // availableField.addReferencer(usingMethod);

                                            entityUsage = new FieldUsageInfo(entityUsage.getType(),
                                                    availableField, true, fromLine, fromColumn,
                                                    toLine, toColumn);
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
                                // ���̃N���X�̃t�B�[���h���g�p���Ă���Ƃ݂Ȃ�
                                {
                                    if (!found) {

                                        final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();
                                        final ExternalClassInfo externalSuperClass = NameResolver
                                                .getExternalSuperClass((TargetClassInfo) referencedClass);
                                        if (!(referencedClass instanceof TargetInnerClassInfo)
                                                && (null != externalSuperClass)) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], externalSuperClass);

                                            // usingMethod.addReferencee(fieldInfo);
                                            // fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            entityUsage = new FieldUsageInfo(entityUsage.getType(),
                                                    fieldInfo, true, fromLine, fromColumn, toLine,
                                                    toColumn);

                                        } else {
                                            assert false : "Can't resolve entity usage1 : "
                                                    + this.toString();
                                        }
                                    }
                                }

                                // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                            } else if (ownerClass instanceof ExternalClassInfo) {

                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                        ownerClass);

                                // usingMethod.addReferencee(fieldInfo);
                                // fieldInfo.addReferencer(usingMethod);
                                fieldInfoManager.add(fieldInfo);

                                entityUsage = new FieldUsageInfo(entityUsage.getType(), fieldInfo,
                                        true, fromLine, fromColumn, toLine, toColumn);
                            }

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    this.resolvedIndo = entityUsage;
                    return this.resolvedIndo;
                }
            }
        }

        // ���p�\�ȃX�^�e�B�b�N�t�B�[���h������G���e�B�e�B��������
        {
            // ���̃N���X�ŗ��p�\�ȃX�^�e�B�b�N�t�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFieldsOfThisClass = Members
                    .<TargetFieldInfo> getStaticMembers(NameResolver.getAvailableFields(usingClass));

            for (final TargetFieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // ��v����t�B�[���h�������������ꍇ
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    // usingMethod.addReferencee(availableFieldOfThisClass);
                    // availableFieldOfThisClass.addReferencer(usingMethod);

                    // �e�̌^�𐶐�
                    final ClassTypeInfo usingClassType = new ClassTypeInfo(usingClass);
                    for (final TypeParameterInfo typeParameter : usingClass.getTypeParameters()) {
                        usingClassType.addTypeArgument(typeParameter);
                    }

                    // availableField.getType() ���玟��word(name[i])�𖼑O����
                    EntityUsageInfo entityUsage = new FieldUsageInfo(usingClassType,
                            availableFieldOfThisClass, true, fromLine, fromColumn, toLine, toColumn);
                    for (int i = 1; i < name.length; i++) {

                        // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                        if (entityUsage.getType() instanceof UnknownTypeInfo) {

                            this.resolvedIndo = new UnknownEntityUsageInfo(fromLine, fromColumn,
                                    toLine, toColumn);
                            return this.resolvedIndo;

                            // �e���N���X�^�̏ꍇ
                        } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                            final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                    .getReferencedClass();

                            // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                            if (ownerClass instanceof TargetClassInfo) {

                                // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                                boolean found = false;
                                {
                                    // ���p�\�ȃX�^�e�B�b�N�t�B�[���h�ꗗ���擾
                                    final List<TargetFieldInfo> availableFields = Members
                                            .getStaticMembers(NameResolver.getAvailableFields(
                                                    (TargetClassInfo) ownerClass, usingClass));

                                    for (final TargetFieldInfo availableField : availableFields) {

                                        // ��v����t�B�[���h�������������ꍇ
                                        if (name[i].equals(availableField.getName())) {
                                            // usingMethod.addReferencee(availableField);
                                            // availableField.addReferencer(usingMethod);

                                            entityUsage = new FieldUsageInfo(entityUsage.getType(),
                                                    availableField, true, fromLine, fromColumn,
                                                    toLine, toColumn);
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                // �X�^�e�B�b�N�t�B�[���h�Ō�����Ȃ������ꍇ�́C�C���i�[�N���X����T��
                                {
                                    if (!found) {
                                        // �C���i�[�N���X�ꗗ���擾
                                        final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses((TargetClassInfo) ownerClass);
                                        for (final TargetInnerClassInfo innerClass : innerClasses) {

                                            // ��v����N���X�������������ꍇ
                                            if (name[i].equals(innerClass.getClassName())) {
                                                // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                        innerClass);
                                                entityUsage = new ClassReferenceInfo(referenceType,
                                                        fromLine, fromColumn, toLine, toColumn);
                                                found = true;
                                                break;
                                            }
                                        }
                                    }
                                }

                                // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
                                // ���̃N���X�̃t�B�[���h���g�p���Ă���Ƃ݂Ȃ�
                                {
                                    if (!found) {

                                        final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();
                                        final ExternalClassInfo externalSuperClass = NameResolver
                                                .getExternalSuperClass((TargetClassInfo) referencedClass);
                                        if (!(referencedClass instanceof TargetInnerClassInfo)
                                                && (null != externalSuperClass)) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], externalSuperClass);

                                            // usingMethod.addReferencee(fieldInfo);
                                            // fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            entityUsage = new FieldUsageInfo(entityUsage.getType(),
                                                    fieldInfo, true, fromLine, fromColumn, toLine,
                                                    toColumn);

                                        } else {
                                            assert false : "Can't resolve entity usage2 : "
                                                    + this.toString();
                                        }
                                    }
                                }

                                // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                            } else if (ownerClass instanceof ExternalClassInfo) {

                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                        ownerClass);

                                // usingMethod.addReferencee(fieldInfo);
                                // fieldInfo.addReferencer(usingMethod);
                                fieldInfoManager.add(fieldInfo);

                                entityUsage = new FieldUsageInfo(entityUsage.getType(), fieldInfo,
                                        true, fromLine, fromColumn, toLine, toColumn);
                            }

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    this.resolvedIndo = entityUsage;
                    return this.resolvedIndo;
                }
            }
        }

        // �G���e�B�e�B�������S���薼�ł���ꍇ������
        {

            for (int length = 1; length <= name.length; length++) {

                // �������閼�O(String[])���쐬
                final String[] searchingName = new String[length];
                System.arraycopy(name, 0, searchingName, 0, length);

                final ClassInfo searchingClass = classInfoManager.getClassInfo(searchingName);
                if (null != searchingClass) {

                    EntityUsageInfo entityUsage = new ClassReferenceInfo(new ClassTypeInfo(
                            searchingClass), fromLine, fromColumn, toLine, toColumn);
                    for (int i = length; i < name.length; i++) {

                        // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                        if (entityUsage.getType() instanceof UnknownTypeInfo) {

                            this.resolvedIndo = new UnknownEntityUsageInfo(fromLine, fromColumn,
                                    toLine, toColumn);
                            return this.resolvedIndo;

                            // �e���N���X�^�̏ꍇ
                        } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                            final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                    .getReferencedClass();

                            // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                            if (ownerClass instanceof TargetClassInfo) {

                                // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                                boolean found = false;
                                {
                                    // ���p�\�ȃt�B�[���h�ꗗ���擾
                                    final List<TargetFieldInfo> availableFields = Members
                                            .getStaticMembers(NameResolver.getAvailableFields(
                                                    (TargetClassInfo) ownerClass, usingClass));

                                    for (final TargetFieldInfo availableField : availableFields) {

                                        // ��v����t�B�[���h�������������ꍇ
                                        if (name[i].equals(availableField.getName())) {
                                            // usingMethod.addReferencee(availableField);
                                            // availableField.addReferencer(usingMethod);

                                            entityUsage = new FieldUsageInfo(entityUsage.getType(),
                                                    availableField, true, fromLine, fromColumn,
                                                    toLine, toColumn);
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                // �X�^�e�B�b�N�t�B�[���h�Ō�����Ȃ������ꍇ�́C�C���i�[�N���X����T��
                                {
                                    if (!found) {
                                        // �C���i�[�N���X�ꗗ���擾
                                        final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                .getAvailableDirectInnerClasses((TargetClassInfo) ownerClass);
                                        for (final TargetInnerClassInfo innerClass : innerClasses) {

                                            // ��v����N���X�������������ꍇ
                                            if (name[i].equals(innerClass.getClassName())) {
                                                // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                        innerClass);
                                                entityUsage = new ClassReferenceInfo(referenceType,
                                                        fromLine, fromColumn, toLine, toColumn);
                                                found = true;
                                                break;
                                            }
                                        }
                                    }
                                }

                                // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
                                // ���̃N���X�̃t�B�[���h���g�p���Ă���Ƃ݂Ȃ�
                                {
                                    if (!found) {

                                        final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();
                                        final ExternalClassInfo externalSuperClass = NameResolver
                                                .getExternalSuperClass((TargetClassInfo) referencedClass);
                                        if (!(referencedClass instanceof TargetInnerClassInfo)
                                                && (null != externalSuperClass)) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], externalSuperClass);

                                            // usingMethod.addReferencee(fieldInfo);
                                            // fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            entityUsage = new FieldUsageInfo(entityUsage.getType(),
                                                    fieldInfo, true, fromLine, fromColumn, toLine,
                                                    toColumn);

                                        } else {
                                            assert false : "Can't resolve entity usage3 : "
                                                    + this.toString();
                                        }
                                    }
                                }

                                // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                            } else if (ownerClass instanceof ExternalClassInfo) {

                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                        ownerClass);

                                // usingMethod.addReferencee(fieldInfo);
                                // fieldInfo.addReferencer(usingMethod);
                                fieldInfoManager.add(fieldInfo);

                                entityUsage = new FieldUsageInfo(entityUsage.getType(), fieldInfo,
                                        true, fromLine, fromColumn, toLine, toColumn);
                            }

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    this.resolvedIndo = entityUsage;
                    return this.resolvedIndo;
                }
            }
        }

        // ���p�\�ȃN���X������G���e�B�e�B��������
        {

            // �����N���X�����猟��
            {
                final TargetClassInfo outestClass;
                if (usingClass instanceof TargetInnerClassInfo) {
                    outestClass = NameResolver.getOuterstClass((TargetInnerClassInfo) usingClass);
                } else {
                    outestClass = usingClass;
                }

                for (final TargetInnerClassInfo innerClassInfo : NameResolver
                        .getAvailableInnerClasses(outestClass)) {

                    // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                    final String innerClassName = innerClassInfo.getClassName();
                    if (innerClassName.equals(name[0])) {

                        EntityUsageInfo entityUsage = new ClassReferenceInfo(new ClassTypeInfo(
                                innerClassInfo), fromLine, fromColumn, toLine, toColumn);
                        for (int i = 1; i < name.length; i++) {

                            // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                            if (entityUsage.getType() instanceof UnknownTypeInfo) {

                                this.resolvedIndo = new UnknownEntityUsageInfo(fromLine,
                                        fromColumn, toLine, toColumn);
                                return this.resolvedIndo;

                                // �e���N���X�^�̏ꍇ
                            } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                                final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage.getType())
                                        .getReferencedClass();

                                // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                                if (ownerClass instanceof TargetClassInfo) {

                                    // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                                    boolean found = false;
                                    {
                                        // ���p�\�ȃt�B�[���h�ꗗ���擾
                                        final List<TargetFieldInfo> availableFields = NameResolver
                                                .getAvailableFields((TargetClassInfo) ownerClass,
                                                        usingClass);

                                        for (final TargetFieldInfo availableField : availableFields) {

                                            // ��v����t�B�[���h�������������ꍇ
                                            if (name[i].equals(availableField.getName())) {
                                                // usingMethod.addReferencee(availableField);
                                                // availableField.addReferencer(usingMethod);

                                                entityUsage = new FieldUsageInfo(entityUsage
                                                        .getType(), availableField, true, fromLine,
                                                        fromColumn, toLine, toColumn);
                                                found = true;
                                                break;
                                            }
                                        }
                                    }

                                    // �X�^�e�B�b�N�t�B�[���h�Ō�����Ȃ������ꍇ�́C�C���i�[�N���X����T��
                                    {
                                        if (!found) {
                                            // �C���i�[�N���X�ꗗ���擾
                                            final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                    .getAvailableDirectInnerClasses((TargetClassInfo) ownerClass);
                                            for (final TargetInnerClassInfo innerClass : innerClasses) {

                                                // ��v����N���X�������������ꍇ
                                                if (name[i].equals(innerClass.getClassName())) {
                                                    // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                    final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                            innerClassInfo);
                                                    entityUsage = new ClassReferenceInfo(
                                                            referenceType, fromLine, fromColumn,
                                                            toLine, toColumn);
                                                    found = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
                                    // ���̃N���X�̃t�B�[���h���g�p���Ă���Ƃ݂Ȃ�
                                    {
                                        if (!found) {

                                            final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                    .getType()).getReferencedClass();
                                            final ExternalClassInfo externalSuperClass = NameResolver
                                                    .getExternalSuperClass((TargetClassInfo) referencedClass);
                                            if (!(referencedClass instanceof TargetInnerClassInfo)
                                                    && (null != externalSuperClass)) {

                                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                        name[i], externalSuperClass);

                                                // usingMethod.addReferencee(fieldInfo);
                                                // fieldInfo.addReferencer(usingMethod);
                                                fieldInfoManager.add(fieldInfo);

                                                entityUsage = new FieldUsageInfo(entityUsage
                                                        .getType(), fieldInfo, true, fromLine,
                                                        fromColumn, toLine, toColumn);

                                            } else {
                                                assert false : "Can't resolve entity usage3.5 : "
                                                        + this.toString();
                                            }
                                        }
                                    }

                                    // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                } else if (ownerClass instanceof ExternalClassInfo) {

                                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                            name[i], ownerClass);

                                    // usingMethod.addReferencee(fieldInfo);
                                    // fieldInfo.addReferencer(usingMethod);
                                    fieldInfoManager.add(fieldInfo);

                                    entityUsage = new FieldUsageInfo(entityUsage.getType(),
                                            fieldInfo, true, fromLine, fromColumn, toLine, toColumn);
                                }

                            } else {
                                assert false : "Here shouldn't be reached!";
                            }
                        }

                        this.resolvedIndo = entityUsage;
                        return this.resolvedIndo;
                    }
                }
            }

            // ���p�\�Ȗ��O��Ԃ��猟��
            {
                for (final AvailableNamespaceInfo availableNamespace : this
                        .getAvailableNamespaces()) {

                    // ���O��Ԗ�.* �ƂȂ��Ă���ꍇ
                    if (availableNamespace.isAllClasses()) {
                        final String[] namespace = availableNamespace.getNamespace();

                        // ���O��Ԃ̉��ɂ���e�N���X�ɑ΂���
                        for (final ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {
                            final String className = classInfo.getClassName();

                            // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                            if (className.equals(name[0])) {

                                EntityUsageInfo entityUsage = new ClassReferenceInfo(
                                        new ClassTypeInfo(classInfo), fromLine, fromColumn, toLine,
                                        toColumn);
                                for (int i = 1; i < name.length; i++) {

                                    // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                                    if (entityUsage.getType() instanceof UnknownTypeInfo) {

                                        this.resolvedIndo = new UnknownEntityUsageInfo(fromLine,
                                                fromColumn, toLine, toColumn);
                                        return this.resolvedIndo;

                                        // �e���N���X�^�̏ꍇ
                                    } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                                        final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage
                                                .getType()).getReferencedClass();

                                        // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                                        if (ownerClass instanceof TargetClassInfo) {

                                            // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                                            boolean found = false;
                                            {
                                                // ���p�\�ȃt�B�[���h�ꗗ���擾
                                                final List<TargetFieldInfo> availableFields = NameResolver
                                                        .getAvailableFields(
                                                                (TargetClassInfo) ownerClass,
                                                                usingClass);

                                                for (TargetFieldInfo availableField : availableFields) {

                                                    // ��v����t�B�[���h�������������ꍇ
                                                    if (name[i].equals(availableField.getName())) {
                                                        // usingMethod.addReferencee(availableField);
                                                        // availableField.addReferencer(usingMethod);

                                                        entityUsage = new FieldUsageInfo(
                                                                entityUsage.getType(),
                                                                availableField, true, fromLine,
                                                                fromColumn, toLine, toColumn);
                                                        found = true;
                                                        break;
                                                    }
                                                }
                                            }

                                            // �X�^�e�B�b�N�t�B�[���h�Ō�����Ȃ������ꍇ�́C�C���i�[�N���X����T��
                                            {
                                                if (!found) {
                                                    // �C���i�[�N���X�ꗗ���擾
                                                    final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                            .getAvailableDirectInnerClasses((TargetClassInfo) ownerClass);
                                                    for (final TargetInnerClassInfo innerClass : innerClasses) {

                                                        // ��v����N���X�������������ꍇ
                                                        if (name[i].equals(innerClass
                                                                .getClassName())) {
                                                            // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                            final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                                    innerClass);
                                                            entityUsage = new ClassReferenceInfo(
                                                                    referenceType, fromLine,
                                                                    fromColumn, toLine, toColumn);
                                                            found = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }

                                            // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
                                            // ���̃N���X�̃t�B�[���h���g�p���Ă���Ƃ݂Ȃ�
                                            {
                                                if (!found) {

                                                    final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                            .getType()).getReferencedClass();
                                                    final ExternalClassInfo externalSuperClass = NameResolver
                                                            .getExternalSuperClass((TargetClassInfo) referencedClass);
                                                    if (!(referencedClass instanceof TargetInnerClassInfo)
                                                            && (null != externalSuperClass)) {

                                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                                name[i], externalSuperClass);

                                                        // usingMethod.addReferencee(fieldInfo);
                                                        // fieldInfo.addReferencer(usingMethod);
                                                        fieldInfoManager.add(fieldInfo);

                                                        entityUsage = new FieldUsageInfo(
                                                                entityUsage.getType(), fieldInfo,
                                                                true, fromLine, fromColumn, toLine,
                                                                toColumn);

                                                    } else {
                                                        assert false : "Can't resolve entity usage4 : "
                                                                + this.toString();
                                                    }
                                                }
                                            }

                                            // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                        } else if (ownerClass instanceof ExternalClassInfo) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], ownerClass);

                                            // usingMethod.addReferencee(fieldInfo);
                                            // fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            entityUsage = new FieldUsageInfo(entityUsage.getType(),
                                                    fieldInfo, true, fromLine, fromColumn, toLine,
                                                    toColumn);
                                        }

                                    } else {
                                        assert false : "Here shouldn't be reached!";
                                    }
                                }

                                this.resolvedIndo = entityUsage;
                                return this.resolvedIndo;
                            }
                        }

                        // ���O���.�N���X�� �ƂȂ��Ă���ꍇ
                    } else {

                        final String[] importName = availableNamespace.getImportName();

                        // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                        if (importName[importName.length - 1].equals(name[0])) {

                            ClassInfo specifiedClassInfo = classInfoManager
                                    .getClassInfo(importName);
                            if (null == specifiedClassInfo) {
                                specifiedClassInfo = new ExternalClassInfo(importName);
                                classInfoManager.add((ExternalClassInfo) specifiedClassInfo);
                            }

                            EntityUsageInfo entityUsage = new ClassReferenceInfo(new ClassTypeInfo(
                                    specifiedClassInfo), fromLine, fromColumn, toLine, toColumn);
                            for (int i = 1; i < name.length; i++) {

                                // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                                if (entityUsage.getType() instanceof UnknownTypeInfo) {

                                    this.resolvedIndo = new UnknownEntityUsageInfo(fromLine,
                                            fromColumn, toLine, toColumn);
                                    return this.resolvedIndo;

                                    // �e���N���X�^�̏ꍇ
                                } else if (entityUsage.getType() instanceof ClassTypeInfo) {

                                    final ClassInfo ownerClass = ((ClassTypeInfo) entityUsage
                                            .getType()).getReferencedClass();

                                    // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                                    if (ownerClass instanceof TargetClassInfo) {

                                        // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                                        boolean found = false;
                                        {
                                            // ���p�\�ȃt�B�[���h�ꗗ���擾
                                            final List<TargetFieldInfo> availableFields = NameResolver
                                                    .getAvailableFields(
                                                            (TargetClassInfo) ownerClass,
                                                            usingClass);

                                            for (final TargetFieldInfo availableField : availableFields) {

                                                // ��v����t�B�[���h�������������ꍇ
                                                if (name[i].equals(availableField.getName())) {
                                                    // usingMethod.addReferencee(availableField);
                                                    // availableField.addReferencer(usingMethod);

                                                    entityUsage = new FieldUsageInfo(entityUsage
                                                            .getType(), availableField, true,
                                                            fromLine, fromColumn, toLine, toColumn);
                                                    found = true;
                                                    break;
                                                }
                                            }
                                        }

                                        // �X�^�e�B�b�N�t�B�[���h�Ō�����Ȃ������ꍇ�́C�C���i�[�N���X����T��
                                        {
                                            if (!found) {
                                                // �C���i�[�N���X�ꗗ���擾
                                                final SortedSet<TargetInnerClassInfo> innerClasses = NameResolver
                                                        .getAvailableDirectInnerClasses((TargetClassInfo) ownerClass);
                                                for (final TargetInnerClassInfo innerClass : innerClasses) {

                                                    // ��v����N���X�������������ꍇ
                                                    if (name[i].equals(innerClass.getClassName())) {
                                                        // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                        final ClassTypeInfo referenceType = new ClassTypeInfo(
                                                                innerClass);
                                                        entityUsage = new ClassReferenceInfo(
                                                                referenceType, fromLine,
                                                                fromColumn, toLine, toColumn);
                                                        found = true;
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
                                        // ���̃N���X�̃t�B�[���h���g�p���Ă���Ƃ݂Ȃ�
                                        {
                                            if (!found) {

                                                final ClassInfo referencedClass = ((ClassTypeInfo) entityUsage
                                                        .getType()).getReferencedClass();
                                                final ExternalClassInfo externalSuperClass = NameResolver
                                                        .getExternalSuperClass((TargetClassInfo) referencedClass);
                                                if (!(referencedClass instanceof TargetInnerClassInfo)
                                                        && (null != externalSuperClass)) {

                                                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                            name[i], externalSuperClass);

                                                    // usingMethod.addReferencee(fieldInfo);
                                                    // fieldInfo.addReferencer(usingMethod);
                                                    fieldInfoManager.add(fieldInfo);

                                                    entityUsage = new FieldUsageInfo(entityUsage
                                                            .getType(), fieldInfo, true, fromLine,
                                                            fromColumn, toLine, toColumn);

                                                } else {
                                                    assert false : "Can't resolve entity usage5 : "
                                                            + this.toString();
                                                }
                                            }
                                        }

                                        // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                    } else if (ownerClass instanceof ExternalClassInfo) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], ownerClass);

                                        // usingMethod.addReferencee(fieldInfo);
                                        // fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        entityUsage = new FieldUsageInfo(entityUsage.getType(),
                                                fieldInfo, true, fromLine, fromColumn, toLine,
                                                toColumn);
                                    }

                                } else {
                                    assert false : "Here shouldn't be reached!";
                                }
                            }

                            this.resolvedIndo = entityUsage;
                            return this.resolvedIndo;
                        }
                    }
                }
            }
        }

        // java����̏ꍇ�́Cjava��javax�Ŏn�܂�C������3�ȏ��UnknownEntityUsageInfo��JDK���̃N���X�Ƃ݂Ȃ�
        if (Settings.getLanguage().equals(LANGUAGE.JAVA)) {

            if ((name[0].equals("java") || name[0].equals("javax")) && (3 <= name.length)) {
                final ExternalClassInfo externalClass = new ExternalClassInfo(name);
                final ClassTypeInfo externalClassType = new ClassTypeInfo(externalClass);
                this.resolvedIndo = new ClassReferenceInfo(externalClassType, fromLine, fromColumn,
                        toLine, toColumn);
                classInfoManager.add(externalClass);
                return this.resolvedIndo;
            }
        }

        err.println("Remain unresolved \"" + this.toString() + "\"" + " line:" + this.getFromLine()
                + " column:" + this.getFromColumn() + " on \""
                + usingClass.getFullQualifiedName(LANGUAGE.JAVA.getNamespaceDelimiter()));

        // ������Ȃ������������s��
        usingMethod.addUnresolvedUsage(this);

        this.resolvedIndo = new UnknownEntityUsageInfo(fromLine, fromColumn, toLine, toColumn);
        return this.resolvedIndo;
    }

    /**
     * �������G���e�B�e�B�g�p����Ԃ��D
     * 
     * @return �������G���e�B�e�B�g�p��
     */
    public String[] getName() {
        return this.name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.name[0]);
        for (int i = 1; i < this.name.length; i++) {
            sb.append(".");
            sb.append(this.name[i]);
        }
        return sb.toString();
    }

    /**
     * ���̖������G���e�B�e�B�g�p�����p���邱�Ƃ̂ł��閼�O��Ԃ�Ԃ��D
     * 
     * @return ���̖������G���e�B�e�B�g�p�����p���邱�Ƃ̂ł��閼�O���
     */
    public Set<AvailableNamespaceInfo> getAvailableNamespaces() {
        return this.availableNamespaces;
    }

    /**
     * ���̖������G���e�B�e�B�g�p�����p���邱�Ƃ̂ł��閼�O��Ԃ�ۑ����邽�߂̕ϐ�
     */
    private final Set<AvailableNamespaceInfo> availableNamespaces;

    /**
     * ���̖������G���e�B�e�B�g�p����ۑ����邽�߂̕ϐ�
     */
    private final String[] name;

    /**
     * �����ς݃G���e�B�e�B�g�p��ۑ����邽�߂̕ϐ�
     */
    private EntityUsageInfo resolvedIndo;

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "UnresolvedUnknownEntityUsage";
        }
    }, MESSAGE_TYPE.ERROR);
}
