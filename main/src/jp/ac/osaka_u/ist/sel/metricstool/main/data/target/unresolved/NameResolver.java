package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Members;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetInnerClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;


/**
 * �������^�����������邽�߂̃��[�e�B���e�B�N���X
 * 
 * @author y-higo
 * 
 */
public final class NameResolver {

    /**
     * �������^���iUnresolvedTypeInfo�j��������ς݌^���iTypeInfo�j��Ԃ��D �Ή���������ς݌^��񂪂Ȃ��ꍇ�� UnknownTypeInfo ��Ԃ��D
     * 
     * @param unresolvedTypeInfo ���O�����������^���
     * @param usingClass ���̖������^�����݂��Ă���N���X
     * @param usingMethod ���̖������^�����݂��Ă��郁�\�b�h�C���\�b�h�O�ł���ꍇ�� null ��^����
     * @param classInfoManager �^�����ɗp����N���X���f�[�^�x�[�X
     * @param fieldInfoManager �^�����ɗp����t�B�[���h���f�[�^�x�[�X
     * @param methodInfoManager �^�����ɗp���郁�\�b�h���f�[�^�x�[�X
     * @param resolvCache �����ς�UnresolvedTypeInfo�̃L���b�V��
     * @return ���O�������ꂽ�^���
     */
    public static TypeInfo resolveTypeInfo(final UnresolvedTypeInfo unresolvedTypeInfo,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == unresolvedTypeInfo) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���΁C��������^���擾
        if ((null != resolvedCache) && resolvedCache.containsKey(unresolvedTypeInfo)) {
            final TypeInfo type = resolvedCache.get(unresolvedTypeInfo);
            return type;
        }

        // �������v���~�e�B�u�^�̏ꍇ
        if (unresolvedTypeInfo instanceof PrimitiveTypeInfo) {
            return (PrimitiveTypeInfo) unresolvedTypeInfo;

            // ������void�^�̏ꍇ
        } else if (unresolvedTypeInfo instanceof VoidTypeInfo) {
            return (VoidTypeInfo) unresolvedTypeInfo;

        } else if (unresolvedTypeInfo instanceof NullTypeInfo) {
            return (NullTypeInfo) unresolvedTypeInfo;

            // �������Q�ƌ^�̏ꍇ
        } else if (unresolvedTypeInfo instanceof UnresolvedReferenceTypeInfo) {

            final String[] referenceName = ((UnresolvedReferenceTypeInfo) unresolvedTypeInfo)
                    .getReferenceName();

            // �Q�Ɩ������S���薼�ł���Ƃ��Č���
            {
                final ClassInfo classInfo = classInfoManager.getClassInfo(referenceName);
                if (null != classInfo) {
                    return classInfo;
                }
            }

            // ���p�\�Ȗ��O��Ԃ���C�^����T��
            {
                for (AvailableNamespaceInfo availableNamespace : ((UnresolvedReferenceTypeInfo) unresolvedTypeInfo)
                        .getAvailableNamespaces()) {

                    // ���O��Ԗ�.* �ƂȂ��Ă���ꍇ
                    if (availableNamespace.isAllClasses()) {
                        final String[] namespace = availableNamespace.getNamespace();

                        // ���O��Ԃ̉��ɂ���e�N���X�ɑ΂���
                        for (ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {
                            final String className = classInfo.getClassName();

                            // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                            if (className.equals(referenceName[0])) {

                                // �L���b�V���p�n�b�V���e�[�u������ꍇ�̓L���b�V����ǉ�
                                if (null != resolvedCache) {
                                    resolvedCache.put(unresolvedTypeInfo, classInfo);
                                }

                                return classInfo;
                            }
                        }

                        // ���O���.�N���X�� �ƂȂ��Ă���ꍇ
                    } else {

                        final String[] importName = availableNamespace.getImportName();

                        // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                        if (importName[importName.length - 1].equals(referenceName[0])) {

                            final String[] namespace = availableNamespace.getNamespace();
                            final String[] fullQualifiedName = new String[namespace.length
                                    + referenceName.length];
                            System.arraycopy(namespace, 0, fullQualifiedName, 0, namespace.length);
                            System.arraycopy(referenceName, 0, fullQualifiedName, namespace.length,
                                    referenceName.length);
                            final ClassInfo classInfo = classInfoManager
                                    .getClassInfo(fullQualifiedName);

                            // �N���X�����������ꍇ�́C�����Ԃ�
                            if (null != classInfo) {
                                return classInfo;
                            }
                        }
                    }
                }
            }

            // ������Ȃ������ꍇ�́CUknownTypeInfo ��Ԃ�
            return UnknownTypeInfo.getInstance();

            // �������z��^�̏ꍇ
        } else if (unresolvedTypeInfo instanceof UnresolvedArrayTypeInfo) {

            final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedTypeInfo)
                    .getElementType();
            final int dimension = ((UnresolvedArrayTypeInfo) unresolvedTypeInfo).getDimension();

            final TypeInfo elementType = NameResolver.resolveTypeInfo(unresolvedElementType,
                    usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager,
                    resolvedCache);
            assert elementType != null : "resolveTypeInfo returned null!";

            // �v�f�̌^���s���̂Ƃ��� UnnownTypeInfo ��Ԃ�
            if (elementType instanceof UnknownTypeInfo) {
                return UnknownTypeInfo.getInstance();

                // �v�f�̌^�������ł����ꍇ�͂��̔z��^���쐬���Ԃ�
            } else {
                final ArrayTypeInfo arrayType = ArrayTypeInfo.getType(elementType, dimension);
                return arrayType;
            }

            // �������N���X���̏ꍇ
        } else if (unresolvedTypeInfo instanceof UnresolvedClassInfo) {

            final TypeInfo classInfo = (ClassInfo) ((UnresolvedClassInfo) unresolvedTypeInfo)
                    .getResolvedInfo();
            return classInfo;

            // �������t�B�[���h�g�p�̏ꍇ
        } else if (unresolvedTypeInfo instanceof UnresolvedFieldUsage) {

            final TypeInfo classInfo = NameResolver.resolveFieldReference(
                    (UnresolvedFieldUsage) unresolvedTypeInfo, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
            return classInfo;

            // ���������\�b�h�Ăяo���̏ꍇ
        } else if (unresolvedTypeInfo instanceof UnresolvedMethodCall) {

            // (c)�̃N���X��`���擾
            final TypeInfo classInfo = NameResolver.resolveMethodCall(
                    (UnresolvedMethodCall) unresolvedTypeInfo, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
            return classInfo;

            // �������G���e�B�e�B�g�p�̏ꍇ
        } else if (unresolvedTypeInfo instanceof UnresolvedEntityUsage) {

            // �G���e�B�e�B�̃N���X��`���擾
            final TypeInfo classInfo = NameResolver.resolveEntityUsage(
                    (UnresolvedEntityUsage) unresolvedTypeInfo, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
            return classInfo;

            // �������z��g�p�̏ꍇ
        } else if (unresolvedTypeInfo instanceof UnresolvedArrayElementUsage) {

            final TypeInfo classInfo = NameResolver.resolveArrayElementUsage(
                    (UnresolvedArrayElementUsage) unresolvedTypeInfo, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
            return classInfo;

            // ����ȊO�̌^�̏ꍇ�̓G���[
        } else {
            throw new IllegalArgumentException(unresolvedTypeInfo.toString()
                    + " is a wrong object!");
        }
    }

    /**
     * �������t�B�[���h�Q�Ƃ��������C�t�B�[���h�Q�Ƃ��s���Ă��郁�\�b�h�ɓo�^����D�܂��C�t�B�[���h�̌^��Ԃ��D
     * 
     * @param fieldReference �������t�B�[���h�Q��
     * @param usingClass �t�B�[���h�Q�Ƃ��s���Ă���N���X
     * @param usingMethod �t�B�[���h�Q�Ƃ��s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @param resolvedCache �����ς�UnresolvedTypeInfo�̃L���b�V��
     * @return �����ς݃t�B�[���h�Q�Ƃ̌^�i�܂�C�t�B�[���h�̌^�j
     */
    public static TypeInfo resolveFieldReference(final UnresolvedFieldUsage fieldReference,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == fieldReference) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == fieldInfoManager)
                || (null == methodInfoManager) || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���΁C��������^���擾
        if (resolvedCache.containsKey(fieldReference)) {
            final TypeInfo type = resolvedCache.get(fieldReference);
            return type;
        }

        // �t�B�[���h�����擾
        final String fieldName = fieldReference.getFieldName();

        // �e�̌^������
        final UnresolvedTypeInfo unresolvedFieldOwnerClassType = fieldReference.getOwnerClassType();
        final TypeInfo fieldOwnerClassType = NameResolver.resolveTypeInfo(
                unresolvedFieldOwnerClassType, usingClass, usingMethod, classInfoManager,
                fieldInfoManager, methodInfoManager, resolvedCache);
        assert fieldOwnerClassType != null : "resolveTypeInfo returned null!";

        // -----��������e��TypeInfo �ɉ����ď����𕪊�
        // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
        if (fieldOwnerClassType instanceof UnknownTypeInfo) {

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(fieldReference);

            // �����ς݃L���b�V���ɓo�^
            resolvedCache.put(fieldReference, UnknownTypeInfo.getInstance());

            return UnknownTypeInfo.getInstance();

            // �e���ΏۃN���X(TargetClassInfo)�������ꍇ
        } else if (fieldOwnerClassType instanceof TargetClassInfo) {

            // �܂��͗��p�\�ȃt�B�[���h���猟��
            {
                // ���p�\�ȃt�B�[���h�ꗗ���擾
                final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                        (TargetClassInfo) fieldOwnerClassType, usingClass);

                // ���p�\�ȃt�B�[���h���C�������t�B�[���h���Ō���
                for (TargetFieldInfo availableField : availableFields) {

                    // ��v����t�B�[���h�������������ꍇ
                    if (fieldName.equals(availableField.getName())) {
                        usingMethod.addReferencee(availableField);
                        availableField.addReferencer(usingMethod);

                        // �����ς݃L���b�V���ɓo�^
                        resolvedCache.put(fieldReference, availableField.getType());

                        return availableField.getType();
                    }
                }
            }

            // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂�
            // ���̃N���X�̕ϐ����g�p���Ă���Ƃ݂Ȃ�
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass((TargetClassInfo) fieldOwnerClassType);
                if (!(fieldOwnerClassType instanceof TargetInnerClassInfo)
                        && (null != externalSuperClass)) {

                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                            externalSuperClass);
                    usingMethod.addReferencee(fieldInfo);
                    fieldInfo.addReferencer(usingMethod);
                    fieldInfoManager.add(fieldInfo);

                    // �����ς݃L���b�V���ɓo�^
                    resolvedCache.put(fieldReference, fieldInfo.getType());

                    // �O���N���X�ɐV�K�ŊO���ϐ�(ExternalFieldInfo)��ǉ������̂Ō^�͕s���D
                    return fieldInfo.getType();
                }
            }

            // ������Ȃ������������s��
            {
                err.println("Can't resolve field reference : " + fieldReference.getFieldName());

                usingMethod.addUnresolvedUsage(fieldReference);

                // �����ς݃L���b�V���ɓo�^
                resolvedCache.put(fieldReference, UnknownTypeInfo.getInstance());

                return UnknownTypeInfo.getInstance();
            }

            // �e���O���N���X�iExternalClassInfo�j�������ꍇ
        } else if (fieldOwnerClassType instanceof ExternalClassInfo) {

            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                    (ExternalClassInfo) fieldOwnerClassType);
            usingMethod.addReferencee(fieldInfo);
            fieldInfo.addReferencer(usingMethod);
            fieldInfoManager.add(fieldInfo);

            // �����ς݃L���b�V���ɓo�^
            resolvedCache.put(fieldReference, fieldInfo.getType());

            // �O���N���X�ɐV�K�ŊO���ϐ�(ExternalFieldInfo)��ǉ������̂Ō^�͕s���D
            return fieldInfo.getType();

        } else if (fieldOwnerClassType instanceof ArrayTypeInfo) {

            // TODO �����͌���ˑ��ɂ��邵���Ȃ��̂��H �z��.length �Ȃ�

            // Java ����� �t�B�[���h���� length �������ꍇ�� int �^��Ԃ�
            if (Settings.getLanguage().equals(LANGUAGE.JAVA) && fieldName.equals("length")) {

                resolvedCache.put(fieldReference, PrimitiveTypeInfo.INT);
                return PrimitiveTypeInfo.INT;
            }
        }

        assert false : "Here shouldn't be reached!";
        return UnknownTypeInfo.getInstance();
    }

    /**
     * �������t�B�[���h������������C�t�B�[���h������s���Ă��郁�\�b�h�ɓo�^����D�܂��C�t�B�[���h�̌^��Ԃ��D
     * 
     * @param fieldAssignment �������t�B�[���h���
     * @param usingClass �t�B�[���h������s���Ă���N���X
     * @param usingMethod �t�B�[���h������s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @param resolvedCache �����ς�UnresolvedTypeInfo�̃L���b�V��
     * @return �����ς݃t�B�[���h����̌^�i�܂�C�t�B�[���h�̌^�j
     */
    public static TypeInfo resolveFieldAssignment(final UnresolvedFieldUsage fieldAssignment,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == fieldAssignment) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == fieldInfoManager)
                || (null == methodInfoManager) || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���΁C��������^���擾
        if (resolvedCache.containsKey(fieldAssignment)) {
            final TypeInfo type = resolvedCache.get(fieldAssignment);
            return type;
        }

        // �t�B�[���h�����擾
        final String fieldName = fieldAssignment.getFieldName();

        // �e�̌^������
        final UnresolvedTypeInfo unresolvedFieldOwnerClassType = fieldAssignment
                .getOwnerClassType();
        final TypeInfo fieldOwnerClassType = NameResolver.resolveTypeInfo(
                unresolvedFieldOwnerClassType, usingClass, usingMethod, classInfoManager,
                fieldInfoManager, methodInfoManager, resolvedCache);
        assert fieldOwnerClassType != null : "resolveTypeInfo returned null!";

        // -----��������e��TypeInfo �ɉ����ď����𕪊�
        // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
        if (fieldOwnerClassType instanceof UnknownTypeInfo) {

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(fieldAssignment);

            // �����ς݃L���b�V���ɓo�^
            resolvedCache.put(fieldAssignment, UnknownTypeInfo.getInstance());

            return UnknownTypeInfo.getInstance();

            // �e���ΏۃN���X(TargetClassInfo)�������ꍇ
        } else if (fieldOwnerClassType instanceof TargetClassInfo) {

            // �܂��͗��p�\�ȃt�B�[���h���猟��
            {
                // ���p�\�ȃt�B�[���h�ꗗ���擾
                final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                        (TargetClassInfo) fieldOwnerClassType, usingClass);

                // ���p�\�ȃt�B�[���h�ꗗ���C�������t�B�[���h���Ō���
                for (TargetFieldInfo availableField : availableFields) {

                    // ��v����t�B�[���h�������������ꍇ
                    if (fieldName.equals(availableField.getName())) {
                        usingMethod.addAssignmentee(availableField);
                        availableField.addAssignmenter(usingMethod);

                        // �����ς݃L���b�V���ɂɓo�^
                        resolvedCache.put(fieldAssignment, availableField.getType());

                        return availableField.getType();
                    }
                }
            }

            // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
            // ���̃N���X�̕ϐ����g�p���Ă���Ƃ݂Ȃ�
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass((TargetClassInfo) fieldOwnerClassType);
                if (!(fieldOwnerClassType instanceof TargetInnerClassInfo)
                        && (null != externalSuperClass)) {

                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                            externalSuperClass);
                    usingMethod.addAssignmentee(fieldInfo);
                    fieldInfo.addAssignmenter(usingMethod);
                    fieldInfoManager.add(fieldInfo);

                    // �����ς݃L���b�V���ɓo�^
                    resolvedCache.put(fieldAssignment, fieldInfo.getType());

                    // �O���N���X�ɐV�K�ŊO���ϐ��iExternalFieldInfo�j��ǉ������̂Ō^�͕s��
                    return fieldInfo.getType();
                }
            }

            // ������Ȃ������������s��
            {
                err.println("Can't resolve field assignment : " + fieldAssignment.getFieldName());

                usingMethod.addUnresolvedUsage(fieldAssignment);

                // �����ς݃L���b�V���ɓo�^
                resolvedCache.put(fieldAssignment, UnknownTypeInfo.getInstance());

                return UnknownTypeInfo.getInstance();
            }

            // �e���O���N���X�iExternalClassInfo�j�������ꍇ
        } else if (fieldOwnerClassType instanceof ExternalClassInfo) {

            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                    (ExternalClassInfo) fieldOwnerClassType);
            usingMethod.addAssignmentee(fieldInfo);
            fieldInfo.addAssignmenter(usingMethod);
            fieldInfoManager.add(fieldInfo);

            // �����ς݃L���b�V���ɓo�^
            resolvedCache.put(fieldAssignment, fieldInfo.getType());

            // �O���N���X�ɐV�K�ŊO���ϐ�(ExternalFieldInfo)��ǉ������̂Ō^�͕s���D
            return fieldInfo.getType();
        }

        assert false : "Here shouldn't be reached!";
        return UnknownTypeInfo.getInstance();
    }

    /**
     * ���������\�b�h�Ăяo�������������C���\�b�h�Ăяo���������s���Ă��郁�\�b�h�ɓo�^����D�܂��C���\�b�h�̕Ԃ�l�̌^��Ԃ��D
     * 
     * @param methodCall ���\�b�h�Ăяo�����
     * @param usingClass ���\�b�h�Ăяo�����s���Ă���N���X
     * @param usingMethod ���\�b�h�Ăяo�����s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @param resolvedCache �����ς�UnresolvedTypeInfo�̃L���b�V��
     * @return ���\�b�h�Ăяo�����ɑΉ����� MethodInfo
     */
    public static TypeInfo resolveMethodCall(final UnresolvedMethodCall methodCall,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodCall) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == methodInfoManager)
                || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���΁C��������^���擾
        if (resolvedCache.containsKey(methodCall)) {
            final TypeInfo type = resolvedCache.get(methodCall);
            return type;
        }

        // ���\�b�h�̃V�O�l�`�����擾
        final String methodName = methodCall.getMethodName();
        final boolean constructor = methodCall.isConstructor();
        final List<UnresolvedTypeInfo> unresolvedParameterTypes = methodCall.getParameterTypes();

        // ���\�b�h�̖���������������
        final List<TypeInfo> parameterTypes = new LinkedList<TypeInfo>();
        for (UnresolvedTypeInfo unresolvedParameterType : unresolvedParameterTypes) {
            TypeInfo parameterType = NameResolver.resolveTypeInfo(unresolvedParameterType,
                    usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager,
                    resolvedCache);
            assert parameterType != null : "resolveTypeInfo returned null!";
            if (parameterType instanceof UnknownTypeInfo) {
                if (unresolvedParameterType instanceof UnresolvedReferenceTypeInfo) {
                    parameterType = NameResolver
                            .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedParameterType);
                    classInfoManager.add((ExternalClassInfo) parameterType);
                } else if (unresolvedParameterType instanceof UnresolvedArrayTypeInfo) {
                    final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedParameterType)
                            .getElementType();
                    final int dimension = ((UnresolvedArrayTypeInfo) unresolvedParameterType)
                            .getDimension();
                    final TypeInfo elementType = NameResolver
                            .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedElementType);
                    classInfoManager.add((ExternalClassInfo) elementType);
                    parameterType = ArrayTypeInfo.getType(elementType, dimension);
                }
            }
            parameterTypes.add(parameterType);
        }

        // �e�̌^������
        final UnresolvedTypeInfo unresolvedMethodOwnerClassType = methodCall.getOwnerClassType();
        final TypeInfo methodOwnerClassType = NameResolver.resolveTypeInfo(
                unresolvedMethodOwnerClassType, usingClass, usingMethod, classInfoManager,
                fieldInfoManager, methodInfoManager, resolvedCache);
        assert methodOwnerClassType != null : "resolveTypeInfo returned null!";

        // -----��������e��TypeInfo �ɉ����ď����𕪊�
        // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
        if (methodOwnerClassType instanceof UnknownTypeInfo) {

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(methodCall);

            // �����ς݃L���b�V���ɓo�^
            resolvedCache.put(methodCall, UnknownTypeInfo.getInstance());

            return UnknownTypeInfo.getInstance();

            // �e���ΏۃN���X(TargetClassInfo)�������ꍇ
        } else if (methodOwnerClassType instanceof TargetClassInfo) {

            // �܂��͗��p�\�ȃ��\�b�h���猟��
            {
                // ���p�\�ȃ��\�b�h�ꗗ���擾
                final List<TargetMethodInfo> availableMethods = NameResolver.getAvailableMethods(
                        (TargetClassInfo) methodOwnerClassType, usingClass);

                // ���p�\�ȃ��\�b�h����C���������\�b�h�ƈ�v������̂�����
                // ���\�b�h���C�����̌^�̃��X�g��p���āC���̃��\�b�h�̌Ăяo���ł��邩�ǂ����𔻒�
                for (TargetMethodInfo availableMethod : availableMethods) {

                    // �Ăяo���\�ȃ��\�b�h�����������ꍇ
                    if (availableMethod.canCalledWith(methodName, parameterTypes)) {
                        usingMethod.addCallee(availableMethod);
                        availableMethod.addCaller(usingMethod);

                        // �����ς݃L���b�V���ɂɓo�^
                        resolvedCache.put(methodCall, availableMethod.getReturnType());

                        return availableMethod.getReturnType();
                    }
                }
            }

            // ���p�\�ȃ��\�b�h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
            // ���̃N���X�̃��\�b�h���g�p���Ă���Ƃ݂Ȃ�
            {
                final ExternalClassInfo externalSuperClass = NameResolver
                        .getExternalSuperClass((TargetClassInfo) methodOwnerClassType);
                if (!(methodOwnerClassType instanceof TargetInnerClassInfo)
                        && (null != externalSuperClass)) {

                    final ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName,
                            externalSuperClass, constructor);
                    final List<ParameterInfo> parameters = NameResolver
                            .createParameters(parameterTypes);
                    methodInfo.addParameters(parameters);

                    usingMethod.addCallee(methodInfo);
                    methodInfo.addCaller(usingMethod);
                    methodInfoManager.add(methodInfo);

                    // �����ς݃L���b�V���ɓo�^
                    resolvedCache.put(methodCall, methodInfo.getReturnType());

                    // �O���N���X�ɐV�K�ŊO���ϐ��iExternalFieldInfo�j��ǉ������̂Ō^�͕s��
                    return methodInfo.getReturnType();
                }
            }

            // ������Ȃ������������s��
            {
                err.println("Can't resolve method Call : " + methodCall.getMethodName());

                usingMethod.addUnresolvedUsage(methodCall);

                // �����ς݃L���b�V���ɓo�^
                resolvedCache.put(methodCall, UnknownTypeInfo.getInstance());

                return UnknownTypeInfo.getInstance();
            }

            // �e���O���N���X�iExternalClassInfo�j�������ꍇ
        } else if (methodOwnerClassType instanceof ExternalClassInfo) {

            final ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName,
                    (ExternalClassInfo) methodOwnerClassType, constructor);
            final List<ParameterInfo> parameters = NameResolver.createParameters(parameterTypes);
            methodInfo.addParameters(parameters);

            usingMethod.addCallee(methodInfo);
            methodInfo.addCaller(usingMethod);
            methodInfoManager.add(methodInfo);

            // �����ς݃L���b�V���ɓo�^
            resolvedCache.put(methodCall, methodInfo.getReturnType());

            // �O���N���X�ɐV�K�ŊO�����\�b�h(ExternalMethodInfo)��ǉ������̂Ō^�͕s���D
            return methodInfo.getReturnType();

            // �e���z�񂾂����ꍇ
        } else if (methodOwnerClassType instanceof ArrayTypeInfo) {

            // Java ����ł���΁C java.lang.Object �ɑ΂���Ăяo��
            if (Settings.getLanguage().equals(LANGUAGE.JAVA)) {
                final ClassInfo ownerClass = classInfoManager.getClassInfo(new String[] { "java",
                        "lang", "Object" });
                final ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName,
                        (ExternalClassInfo) ownerClass, false);
                final List<ParameterInfo> parameters = NameResolver
                        .createParameters(parameterTypes);
                methodInfo.addParameters(parameters);

                usingMethod.addCallee(methodInfo);
                methodInfo.addCaller(usingMethod);
                methodInfoManager.add(methodInfo);

                // �����ς݃L���b�V���ɓo�^
                resolvedCache.put(methodCall, methodInfo.getReturnType());

                // �O���N���X�ɐV�K�ŊO�����\�b�h��ǉ������̂Ō^�͕s��
                return methodInfo.getReturnType();
            }
        }

        assert false : "Here shouldn't be reached!";
        return UnknownTypeInfo.getInstance();
    }

    /**
     * �������z��^�t�B�[���h�̗v�f�g�p���������C�z��^�t�B�[���h�̗v�f�g�p���s���Ă��郁�\�b�h�ɓo�^����D�܂��C�t�B�[���h�̌^��Ԃ��D
     * 
     * @param arrayElement �������z��^�t�B�[���h�̗v�f�g�p
     * @param usingClass �t�B�[���h������s���Ă���N���X
     * @param usingMethod �t�B�[���h������s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @param resolvedCache �����ς�UnresolvedTypeInfo�̃L���b�V��
     * @return �����ς݃t�B�[���h����̌^�i�܂�C�t�B�[���h�̌^�j
     */
    public static TypeInfo resolveArrayElementUsage(final UnresolvedArrayElementUsage arrayElement,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == arrayElement) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == fieldInfoManager)
                || (null == methodInfoManager) || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���΁C��������^���擾
        if (resolvedCache.containsKey(arrayElement)) {
            final TypeInfo type = resolvedCache.get(arrayElement);
            return type;
        }

        // �v�f�g�p���������Ă��関��`�^���擾
        final UnresolvedTypeInfo unresolvedOwnerType = arrayElement.getOwnerArrayType();
        TypeInfo ownerArrayType = NameResolver.resolveTypeInfo(unresolvedOwnerType, usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
        assert ownerArrayType != null : "resolveTypeInfo returned null!";

        // ����`�^�̖��O�������ł��Ȃ������ꍇ
        if (ownerArrayType instanceof UnknownTypeInfo) {
            if (unresolvedOwnerType instanceof UnresolvedArrayTypeInfo) {
                final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedOwnerType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedOwnerType)
                        .getDimension();
                final TypeInfo elementType = NameResolver
                        .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedElementType);
                classInfoManager.add((ExternalClassInfo) elementType);
                ownerArrayType = ArrayTypeInfo.getType(elementType, dimension);
            } else {

                err.println("Can't resolve array type : " + arrayElement.getTypeName());

                usingMethod.addUnresolvedUsage(arrayElement);
                resolvedCache.put(arrayElement, UnknownTypeInfo.getInstance());
                return UnknownTypeInfo.getInstance();
            }
        }

        // �z��̎����ɉ����Č^�𐶐�
        final int ownerArrayDimension = ((ArrayTypeInfo) ownerArrayType).getDimension();
        final TypeInfo ownerElementType = ((ArrayTypeInfo) ownerArrayType).getElementType();

        // �z�񂪓񎟌��ȏ�̏ꍇ�́C����������Ƃ����z���Ԃ�
        if (1 < ownerArrayDimension) {

            final TypeInfo type = ArrayTypeInfo.getType(ownerElementType, ownerArrayDimension - 1);
            resolvedCache.put(arrayElement, type);
            return type;

            // �z�񂪈ꎟ���̏ꍇ�́C�v�f�̌^��Ԃ�
        } else {

            resolvedCache.put(arrayElement, ownerElementType);
            return ownerElementType;
        }
    }

    /**
     * �������G���e�B�e�B�g�p�����������C�G���e�B�e�B�g�p�������s���Ă��郁�\�b�h�ɓo�^����D�܂��C�G���e�B�e�B�̉����ς݌^��Ԃ��D
     * 
     * @param entityUsage �������G���e�B�e�B�g�p
     * @param usingClass ���\�b�h�Ăяo�����s���Ă���N���X
     * @param usingMethod ���\�b�h�Ăяo�����s���Ă��郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @param resolvedCache �����ς�UnresolvedTypeInfo�̃L���b�V��
     * @return ���\�b�h�Ăяo�����ɑΉ����� MethodInfo
     */
    public static TypeInfo resolveEntityUsage(final UnresolvedEntityUsage entityUsage,
            final TargetClassInfo usingClass, final TargetMethodInfo usingMethod,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager,
            final MethodInfoManager methodInfoManager,
            final Map<UnresolvedTypeInfo, TypeInfo> resolvedCache) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == entityUsage) || (null == usingClass) || (null == usingMethod)
                || (null == classInfoManager) || (null == methodInfoManager)
                || (null == resolvedCache)) {
            throw new NullPointerException();
        }

        // ���ɉ����ς݂ł���΁C��������^���擾
        if (resolvedCache.containsKey(entityUsage)) {
            final TypeInfo type = resolvedCache.get(entityUsage);
            assert null != type : "resolveEntityUsage returned null!";
            return type;
        }

        // �G���e�B�e�B�Q�Ɩ����擾
        final String[] name = entityUsage.getName();

        // ���p�\�ȃC���X�^���X�t�B�[���h������G���e�B�e�B��������
        {
            // ���̃N���X�ŗ��p�\�ȃC���X�^���X�t�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFieldsOfThisClass = Members
                    .<TargetFieldInfo> getInstanceMembers(NameResolver
                            .getAvailableFields(usingClass));

            for (TargetFieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // ��v����t�B�[���h�������������ꍇ
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    usingMethod.addReferencee(availableFieldOfThisClass);
                    availableFieldOfThisClass.addReferencer(usingMethod);

                    // availableField.getType() ���玟��word(name[i])�𖼑O����
                    TypeInfo ownerTypeInfo = availableFieldOfThisClass.getType();
                    for (int i = 1; i < name.length; i++) {

                        // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                        if (ownerTypeInfo instanceof UnknownTypeInfo) {

                            // �����ς݃L���b�V���ɓo�^
                            resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

                            return UnknownTypeInfo.getInstance();

                            // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                        } else if (ownerTypeInfo instanceof TargetClassInfo) {

                            // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                            boolean found = false;
                            {
                                // ���p�\�ȃC���X�^���X�t�B�[���h�ꗗ���擾
                                final List<TargetFieldInfo> availableFields = Members
                                        .getInstanceMembers(NameResolver.getAvailableFields(
                                                (TargetClassInfo) ownerTypeInfo, usingClass));

                                for (TargetFieldInfo availableField : availableFields) {

                                    // ��v����t�B�[���h�������������ꍇ
                                    if (name[i].equals(availableField.getName())) {
                                        usingMethod.addReferencee(availableField);
                                        availableField.addReferencer(usingMethod);

                                        ownerTypeInfo = availableField.getType();
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            // ���p�\�ȃt�B�[���h��������Ȃ������ꍇ�́C�O���N���X�ł���e�N���X������͂��D
                            // ���̃N���X�̃t�B�[���h���g�p���Ă���Ƃ݂Ȃ�
                            {
                                if (!found) {

                                    final ExternalClassInfo externalSuperClass = NameResolver
                                            .getExternalSuperClass((TargetClassInfo) ownerTypeInfo);
                                    if (!(ownerTypeInfo instanceof TargetInnerClassInfo)
                                            && (null != externalSuperClass)) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], externalSuperClass);

                                        usingMethod.addReferencee(fieldInfo);
                                        fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        ownerTypeInfo = fieldInfo.getType();

                                    } else {
                                        err.println("Can't resolve entity usage1 : "
                                                + entityUsage.getTypeName());
                                    }
                                }
                            }

                            // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                        } else if (ownerTypeInfo instanceof ExternalClassInfo) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                    (ExternalClassInfo) ownerTypeInfo);

                            usingMethod.addReferencee(fieldInfo);
                            fieldInfo.addReferencer(usingMethod);
                            fieldInfoManager.add(fieldInfo);

                            ownerTypeInfo = fieldInfo.getType();

                        } else {
                            err.println("here shouldn't be reached!");
                            assert false;
                        }
                    }

                    // �����ς݃L���b�V���ɓo�^
                    resolvedCache.put(entityUsage, ownerTypeInfo);
                    assert null != ownerTypeInfo : "resolveEntityUsage returned null!";
                    return ownerTypeInfo;
                }
            }
        }

        // ���p�\�ȃX�^�e�B�b�N�t�B�[���h������G���e�B�e�B��������
        {
            // ���̃N���X�ŗ��p�\�ȃX�^�e�B�b�N�t�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFieldsOfThisClass = Members
                    .<TargetFieldInfo> getStaticMembers(NameResolver.getAvailableFields(usingClass));

            for (TargetFieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // ��v����t�B�[���h�������������ꍇ
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    usingMethod.addReferencee(availableFieldOfThisClass);
                    availableFieldOfThisClass.addReferencer(usingMethod);

                    // availableField.getType() ���玟��word(name[i])�𖼑O����
                    TypeInfo ownerTypeInfo = availableFieldOfThisClass.getType();
                    for (int i = 1; i < name.length; i++) {

                        // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                        if (ownerTypeInfo instanceof UnknownTypeInfo) {

                            // �����ς݃L���b�V���ɓo�^
                            resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

                            return UnknownTypeInfo.getInstance();

                            // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                        } else if (ownerTypeInfo instanceof TargetClassInfo) {

                            // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                            boolean found = false;
                            {
                                // ���p�\�ȃX�^�e�B�b�N�t�B�[���h�ꗗ���擾
                                final List<TargetFieldInfo> availableFields = Members
                                        .getStaticMembers(NameResolver.getAvailableFields(
                                                (TargetClassInfo) ownerTypeInfo, usingClass));

                                for (TargetFieldInfo availableField : availableFields) {

                                    // ��v����t�B�[���h�������������ꍇ
                                    if (name[i].equals(availableField.getName())) {
                                        usingMethod.addReferencee(availableField);
                                        availableField.addReferencer(usingMethod);

                                        ownerTypeInfo = availableField.getType();
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            // �X�^�e�B�b�N�t�B�[���h�Ō�����Ȃ������ꍇ�́C�C���i�[�N���X����T��
                            {
                                if (!found) {
                                    // �C���i�[�N���X�ꗗ���擾
                                    final SortedSet<TargetInnerClassInfo> innerClasses = ((TargetClassInfo) ownerTypeInfo)
                                            .getInnerClasses();

                                    for (TargetInnerClassInfo innerClass : innerClasses) {

                                        // ��v����N���X�������������ꍇ
                                        if (name[i].equals(innerClass.getClassName())) {
                                            // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                            ownerTypeInfo = innerClass;
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

                                    final ExternalClassInfo externalSuperClass = NameResolver
                                            .getExternalSuperClass((TargetClassInfo) ownerTypeInfo);
                                    if (!(ownerTypeInfo instanceof TargetInnerClassInfo)
                                            && (null != externalSuperClass)) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], externalSuperClass);

                                        usingMethod.addReferencee(fieldInfo);
                                        fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        ownerTypeInfo = fieldInfo.getType();

                                    } else {
                                        err.println("Can't resolve entity usage2 : "
                                                + entityUsage.getTypeName());
                                    }
                                }
                            }

                            // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                        } else if (ownerTypeInfo instanceof ExternalClassInfo) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                    (ExternalClassInfo) ownerTypeInfo);

                            usingMethod.addReferencee(fieldInfo);
                            fieldInfo.addReferencer(usingMethod);
                            fieldInfoManager.add(fieldInfo);

                            ownerTypeInfo = fieldInfo.getType();

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    // �����ς݃L���b�V���ɓo�^
                    resolvedCache.put(entityUsage, ownerTypeInfo);
                    assert null != ownerTypeInfo : "resolveEntityUsage returned null!";
                    return ownerTypeInfo;
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

                    TypeInfo ownerTypeInfo = searchingClass;
                    for (int i = length; i < name.length; i++) {

                        // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                        if (ownerTypeInfo instanceof UnknownTypeInfo) {

                            // �����ς݃L���b�V���ɓo�^
                            resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

                            return UnknownTypeInfo.getInstance();

                            // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                        } else if (ownerTypeInfo instanceof TargetClassInfo) {

                            // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                            boolean found = false;
                            {
                                // ���p�\�ȃt�B�[���h�ꗗ���擾
                                final List<TargetFieldInfo> availableFields = Members
                                        .getStaticMembers(NameResolver.getAvailableFields(
                                                (TargetClassInfo) ownerTypeInfo, usingClass));

                                for (TargetFieldInfo availableField : availableFields) {

                                    // ��v����t�B�[���h�������������ꍇ
                                    if (name[i].equals(availableField.getName())) {
                                        usingMethod.addReferencee(availableField);
                                        availableField.addReferencer(usingMethod);

                                        ownerTypeInfo = availableField.getType();
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            // �X�^�e�B�b�N�t�B�[���h�Ō�����Ȃ������ꍇ�́C�C���i�[�N���X����T��
                            {
                                if (!found) {
                                    // �C���i�[�N���X�ꗗ���擾
                                    final SortedSet<TargetInnerClassInfo> innerClasses = ((TargetClassInfo) ownerTypeInfo)
                                            .getInnerClasses();

                                    for (TargetInnerClassInfo innerClass : innerClasses) {

                                        // ��v����N���X�������������ꍇ
                                        if (name[i].equals(innerClass.getClassName())) {
                                            // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                            ownerTypeInfo = innerClass;
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

                                    final ExternalClassInfo externalSuperClass = NameResolver
                                            .getExternalSuperClass((TargetClassInfo) ownerTypeInfo);
                                    if (!(ownerTypeInfo instanceof TargetInnerClassInfo)
                                            && (null != externalSuperClass)) {

                                        final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                name[i], externalSuperClass);

                                        usingMethod.addReferencee(fieldInfo);
                                        fieldInfo.addReferencer(usingMethod);
                                        fieldInfoManager.add(fieldInfo);

                                        ownerTypeInfo = fieldInfo.getType();

                                    } else {
                                        err.println("Can't resolve entity usage3 : "
                                                + entityUsage.getTypeName());
                                    }
                                }
                            }

                            // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                        } else if (ownerTypeInfo instanceof ExternalClassInfo) {

                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                    (ExternalClassInfo) ownerTypeInfo);

                            usingMethod.addReferencee(fieldInfo);
                            fieldInfo.addReferencer(usingMethod);
                            fieldInfoManager.add(fieldInfo);

                            ownerTypeInfo = fieldInfo.getType();

                        } else {
                            assert false : "Here shouldn't be reached!";
                        }
                    }

                    // �����ς݃L���b�V���ɓo�^
                    resolvedCache.put(entityUsage, ownerTypeInfo);
                    assert null != ownerTypeInfo : "resolveEntityUsage returned null!";
                    return ownerTypeInfo;
                }
            }
        }

        // ���p�\�ȃN���X������G���e�B�e�B��������
        {
            for (AvailableNamespaceInfo availableNamespace : entityUsage.getAvailableNamespaces()) {

                // ���O��Ԗ�.* �ƂȂ��Ă���ꍇ
                if (availableNamespace.isAllClasses()) {
                    final String[] namespace = availableNamespace.getNamespace();

                    // ���O��Ԃ̉��ɂ���e�N���X�ɑ΂���
                    for (ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {
                        final String className = classInfo.getClassName();

                        // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                        if (className.equals(name[0])) {

                            TypeInfo ownerTypeInfo = classInfo;
                            for (int i = 1; i < name.length; i++) {

                                // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                                if (ownerTypeInfo instanceof UnknownTypeInfo) {

                                    // �����ς݃L���b�V���ɓo�^
                                    resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

                                    return UnknownTypeInfo.getInstance();

                                    // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                                } else if (ownerTypeInfo instanceof TargetClassInfo) {

                                    // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                                    boolean found = false;
                                    {
                                        // ���p�\�ȃt�B�[���h�ꗗ���擾
                                        final List<TargetFieldInfo> availableFields = NameResolver
                                                .getAvailableFields(
                                                        (TargetClassInfo) ownerTypeInfo, usingClass);

                                        for (TargetFieldInfo availableField : availableFields) {

                                            // ��v����t�B�[���h�������������ꍇ
                                            if (name[i].equals(availableField.getName())) {
                                                usingMethod.addReferencee(availableField);
                                                availableField.addReferencer(usingMethod);

                                                ownerTypeInfo = availableField.getType();
                                                found = true;
                                                break;
                                            }
                                        }
                                    }

                                    // �X�^�e�B�b�N�t�B�[���h�Ō�����Ȃ������ꍇ�́C�C���i�[�N���X����T��
                                    {
                                        if (!found) {
                                            // �C���i�[�N���X�ꗗ���擾
                                            final SortedSet<TargetInnerClassInfo> innerClasses = ((TargetClassInfo) ownerTypeInfo)
                                                    .getInnerClasses();

                                            for (TargetInnerClassInfo innerClass : innerClasses) {

                                                // ��v����N���X�������������ꍇ
                                                if (name[i].equals(innerClass.getClassName())) {
                                                    // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                    ownerTypeInfo = innerClass;
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

                                            final ExternalClassInfo externalSuperClass = NameResolver
                                                    .getExternalSuperClass((TargetClassInfo) ownerTypeInfo);
                                            if (!(ownerTypeInfo instanceof TargetInnerClassInfo)
                                                    && (null != externalSuperClass)) {

                                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                        name[i], externalSuperClass);

                                                usingMethod.addReferencee(fieldInfo);
                                                fieldInfo.addReferencer(usingMethod);
                                                fieldInfoManager.add(fieldInfo);

                                                ownerTypeInfo = fieldInfo.getType();

                                            } else {
                                                err.println("Can't resolve entity usage4 : "
                                                        + entityUsage.getTypeName());
                                            }
                                        }
                                    }

                                    // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                                } else if (ownerTypeInfo instanceof ExternalClassInfo) {

                                    final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                            name[i], (ExternalClassInfo) ownerTypeInfo);

                                    usingMethod.addReferencee(fieldInfo);
                                    fieldInfo.addReferencer(usingMethod);
                                    fieldInfoManager.add(fieldInfo);

                                    ownerTypeInfo = fieldInfo.getType();

                                } else {
                                    assert false : "Here should be reached!";
                                }
                            }

                            // �����ς݃L���b�V���ɓo�^
                            resolvedCache.put(entityUsage, ownerTypeInfo);
                            assert null != ownerTypeInfo : "resolveEntityUsage returned null!";
                            return ownerTypeInfo;
                        }
                    }

                    // ���O���.�N���X�� �ƂȂ��Ă���ꍇ
                } else {

                    final String[] importName = availableNamespace.getImportName();

                    // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                    if (importName[importName.length - 1].equals(name[0])) {

                        ClassInfo specifiedClassInfo = classInfoManager.getClassInfo(importName);
                        if (null == specifiedClassInfo) {
                            specifiedClassInfo = new ExternalClassInfo(importName);
                            classInfoManager.add((ExternalClassInfo) specifiedClassInfo);
                        }

                        TypeInfo ownerTypeInfo = specifiedClassInfo;
                        for (int i = 1; i < name.length; i++) {

                            // �e�� UnknownTypeInfo ��������C�ǂ����悤���Ȃ�
                            if (ownerTypeInfo instanceof UnknownTypeInfo) {

                                // �����ς݃L���b�V���ɓo�^
                                resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

                                return UnknownTypeInfo.getInstance();

                                // �e���ΏۃN���X(TargetClassInfo)�̏ꍇ
                            } else if (ownerTypeInfo instanceof TargetClassInfo) {

                                // �܂��͗��p�\�ȃt�B�[���h�ꗗ���擾
                                boolean found = false;
                                {
                                    // ���p�\�ȃt�B�[���h�ꗗ���擾
                                    final List<TargetFieldInfo> availableFields = NameResolver
                                            .getAvailableFields((TargetClassInfo) ownerTypeInfo,
                                                    usingClass);

                                    for (TargetFieldInfo availableField : availableFields) {

                                        // ��v����t�B�[���h�������������ꍇ
                                        if (name[i].equals(availableField.getName())) {
                                            usingMethod.addReferencee(availableField);
                                            availableField.addReferencer(usingMethod);

                                            ownerTypeInfo = availableField.getType();
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                // �X�^�e�B�b�N�t�B�[���h�Ō�����Ȃ������ꍇ�́C�C���i�[�N���X����T��
                                {
                                    if (!found) {
                                        // �C���i�[�N���X�ꗗ���擾
                                        final SortedSet<TargetInnerClassInfo> innerClasses = ((TargetClassInfo) ownerTypeInfo)
                                                .getInnerClasses();

                                        for (TargetInnerClassInfo innerClass : innerClasses) {

                                            // ��v����N���X�������������ꍇ
                                            if (name[i].equals(innerClass.getClassName())) {
                                                // TODO ���p�֌W���\�z����R�[�h���K�v�H

                                                ownerTypeInfo = innerClass;
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

                                        final ExternalClassInfo externalSuperClass = NameResolver
                                                .getExternalSuperClass((TargetClassInfo) ownerTypeInfo);
                                        if (!(ownerTypeInfo instanceof TargetInnerClassInfo)
                                                && (null != externalSuperClass)) {

                                            final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(
                                                    name[i], externalSuperClass);

                                            usingMethod.addReferencee(fieldInfo);
                                            fieldInfo.addReferencer(usingMethod);
                                            fieldInfoManager.add(fieldInfo);

                                            ownerTypeInfo = fieldInfo.getType();

                                        } else {
                                            err.println("Can't resolve entity usage5 : "
                                                    + entityUsage.getTypeName());
                                        }
                                    }
                                }

                                // �e���O���N���X(ExternalClassInfo)�̏ꍇ
                            } else if (ownerTypeInfo instanceof ExternalClassInfo) {

                                final ExternalFieldInfo fieldInfo = new ExternalFieldInfo(name[i],
                                        (ExternalClassInfo) ownerTypeInfo);

                                usingMethod.addReferencee(fieldInfo);
                                fieldInfo.addReferencer(usingMethod);
                                fieldInfoManager.add(fieldInfo);

                                ownerTypeInfo = fieldInfo.getType();

                            }else {
                                assert false : "Here shouldn't be reached!";
                            }
                        }

                        // �����ς݃L���b�V���ɓo�^
                        resolvedCache.put(entityUsage, ownerTypeInfo);
                        assert null != ownerTypeInfo : "resolveEntityUsage returned null!";
                        return ownerTypeInfo;
                    }
                }
            }
        }

        err.println("Can't resolve entity usage6 : " + entityUsage.getTypeName());

        // ������Ȃ������������s��
        usingMethod.addUnresolvedUsage(entityUsage);

        // �����ς݃L���b�V���ɓo�^
        resolvedCache.put(entityUsage, UnknownTypeInfo.getInstance());

        return UnknownTypeInfo.getInstance();
    }

    /**
     * �����ŗ^����ꂽ�������^����\�������ς݌^���N���X�𐶐�����D �����ň����Ƃ��ė^������̂́C�\�[�X�R�[�h���p�[�X����Ă��Ȃ��^�ł���̂ŁC������������ς݌^���N���X��
     * ExternalClassInfo �ƂȂ�D
     * 
     * @param unresolvedReferenceType �������^���
     * @return �����ς݌^���
     */
    public static ExternalClassInfo createExternalClassInfo(
            final UnresolvedReferenceTypeInfo unresolvedReferenceType) {

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

                // ���O���.�N���X�� �ƂȂ��Ă���ꍇ
            } else {

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
    private static ExternalClassInfo getExternalSuperClass(final TargetClassInfo classInfo) {

        for (ClassInfo superClassInfo : classInfo.getSuperClasses()) {

            if (superClassInfo instanceof ExternalClassInfo) {
                return (ExternalClassInfo) superClassInfo;
            }

            NameResolver.getExternalSuperClass((TargetClassInfo) superClassInfo);
        }

        return null;
    }

    /**
     * �u���݂̃N���X�v�ŗ��p�\�ȃt�B�[���h�ꗗ��Ԃ��D
     * �����ŁC�u���p�\�ȃt�B�[���h�v�Ƃ́C�u���݂̃N���X�v�Œ�`����Ă���t�B�[���h�C�y�т��̐e�N���X�Œ�`����Ă���t�B�[���h�̂����q�N���X����A�N�Z�X���\�ȃt�B�[���h�ł���D
     * ���p�\�ȃt�B�[���h�� List �Ɋi�[����Ă���D ���X�g�̐擪����D�揇�ʂ̍����t�B�[���h�i�܂�C�N���X�K�w�ɂ����ĉ��ʂ̃N���X�ɒ�`����Ă���t�B�[���h�j���i�[����Ă���D
     * 
     * @param thisClass ���݂̃N���X
     * @return ���p�\�ȃt�B�[���h�ꗗ
     */
    private static List<TargetFieldInfo> getAvailableFields(final TargetClassInfo thisClass) {

        if (null == thisClass) {
            throw new NullPointerException();
        }

        return NameResolver.getAvailableFields(thisClass, thisClass);
    }

    /**
     * �u���݂̃N���X�v�ŗ��p�\�ȃ��\�b�h�ꗗ��Ԃ��D
     * �����ŁC�u���p�\�ȃ��\�b�h�v�Ƃ́C�u���݂̃N���X�v�Œ�`����Ă��郁�\�b�h�C�y�т��̐e�N���X�Œ�`����Ă��郁�\�b�h�̂����q�N���X����A�N�Z�X���\�ȃ��\�b�h�ł���D
     * ���p�\�ȃ��\�b�h�� List �Ɋi�[����Ă���D ���X�g�̐擪����D�揇�ʂ̍������\�b�h�i�܂�C�N���X�K�w�ɂ����ĉ��ʂ̃N���X�ɒ�`����Ă��郁�\�b�h�j���i�[����Ă���D
     * 
     * @param thisClass ���݂̃N���X
     * @return ���p�\�ȃ��\�b�h�ꗗ
     */
    private static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo thisClass) {

        if (null == thisClass) {
            throw new NullPointerException();
        }

        return NameResolver.getAvailableMethods(thisClass, thisClass);
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
    private static List<TargetFieldInfo> getAvailableFields(final TargetClassInfo usedClass,
            final TargetClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // ���̃N���X�Œ�`����Ă���t�B�[���h�̂����C�g�p����N���X�ŗ��p�\�ȃt�B�[���h���擾����
        // 2�̃N���X�������ꍇ
        if (usedClass.equals(usingClass)) {

            availableFields.addAll(usedClass.getDefinedFields());

            // 2�̃N���X���������O��Ԃ������Ă���ꍇ
        } else if (usedClass.getNamespace().equals(usingClass.getNamespace())) {

            for (TargetFieldInfo field : usedClass.getDefinedFields()) {
                if (field.isNamespaceVisible()) {
                    availableFields.add(field);
                }
            }

            // �Ⴄ���O��Ԃ������Ă���ꍇ
        } else {
            for (TargetFieldInfo field : usedClass.getDefinedFields()) {
                if (field.isPublicVisible()) {
                    availableFields.add(field);
                }
            }
        }

        // �e�N���X�Œ�`����Ă���C�q�N���X����A�N�Z�X���\�ȃt�B�[���h���擾
        // List �ɓ����̂ŁC�e�N���X�̃t�B�[���h�̌�� add ���Ȃ���΂Ȃ�Ȃ�
        for (ClassInfo superClassInfo : usedClass.getSuperClasses()) {

            if (superClassInfo instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsInSubClasses((TargetClassInfo) superClassInfo);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableFields);
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
    private static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo usedClass,
            final TargetClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // ���̃N���X�Œ�`����Ă��郁�\�b�h�̂����C�g�p����N���X�ŗ��p�\�ȃ��\�b�h���擾����
        // 2�̃N���X�������ꍇ
        if (usedClass.equals(usingClass)) {

            availableMethods.addAll(usedClass.getDefinedMethods());

            // 2�̃N���X���������O��Ԃ������Ă���ꍇ
        } else if (usedClass.getNamespace().equals(usingClass.getNamespace())) {

            for (TargetMethodInfo method : usedClass.getDefinedMethods()) {
                if (method.isNamespaceVisible()) {
                    availableMethods.add(method);
                }
            }

            // �Ⴄ���O��Ԃ������Ă���ꍇ
        } else {
            for (TargetMethodInfo method : usedClass.getDefinedMethods()) {
                if (method.isPublicVisible()) {
                    availableMethods.add(method);
                }
            }
        }

        // �e�N���X�Œ�`����Ă���C�q�N���X����A�N�Z�X���\�ȃ��\�b�h���擾
        // List �ɓ����̂ŁC�e�N���X�̃��\�b�h�̌�� add ���Ȃ���΂Ȃ�Ȃ�
        for (ClassInfo superClassInfo : usedClass.getSuperClasses()) {

            if (superClassInfo instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsInSubClasses((TargetClassInfo) superClassInfo);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * �u�g�p�����N���X�v�̎q�N���X�g�p�����ꍇ�ɁC���p�\�ȃt�B�[���h�ꗗ��Ԃ��D
     * �����ŁC�u���p�\�ȃt�B�[���h�v�Ƃ́C�u�g�p�����N���X�v�������͂��̐e�N���X�Œ�`����Ă���t�B�[���h�̂����C�q�N���X����A�N�Z�X���\�ȃt�B�[���h�ł���D
     * �q�N���X�ŗ��p�\�ȃt�B�[���h�ꗗ�� List �Ɋi�[����Ă���D
     * ���X�g�̐擪����D�揇�ʂ̍����t�B�[���h�i�܂�C�N���X�K�w�ɂ����ĉ��ʂ̃N���X�ɒ�`����Ă���t�B�[���h�j���i�[����Ă���D
     * 
     * @param usedClass �g�p�����N���X
     * @return ���p�\�ȃt�B�[���h�ꗗ
     */
    private static List<TargetFieldInfo> getAvailableFieldsInSubClasses(
            final TargetClassInfo usedClass) {

        if (null == usedClass) {
            throw new NullPointerException();
        }

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // ���̃N���X�Œ�`����Ă���C�q�N���X����A�N�Z�X�\�ȃt�B�[���h���擾
        for (TargetFieldInfo field : usedClass.getDefinedFields()) {
            if (field.isInheritanceVisible()) {
                availableFields.add(field);
            }
        }

        // �e�N���X�Œ�`����Ă���C�q�N���X����A�N�Z�X���\�ȃt�B�[���h���擾
        // List �ɓ����̂ŁC�e�N���X�̃t�B�[���h�̌�� add ���Ȃ���΂Ȃ�Ȃ�
        for (ClassInfo superClassInfo : usedClass.getSuperClasses()) {

            if (superClassInfo instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsInSubClasses((TargetClassInfo) superClassInfo);
                availableFields.addAll(availableFieldsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableFields);
    }

    /**
     * �u�g�p�����N���X�v�̎q�N���X�g�p�����ꍇ�ɁC���p�\�ȃ��\�b�h�ꗗ��Ԃ��D
     * �����ŁC�u���p�\�ȃ��\�b�h�v�Ƃ́C�u�g�p����郁�\�b�h�v�������͂��̐e�N���X�Œ�`����Ă��郁�\�b�h�̂����C�q�N���X����A�N�Z�X���\�ȃ��\�b�h�ł���D
     * �q�N���X�ŗ��p�\�ȃ��\�b�h�ꗗ�� List �Ɋi�[����Ă���D
     * ���X�g�̐擪����D�揇�ʂ̍������\�b�h�i�܂�C�N���X�K�w�ɂ����ĉ��ʂ̃N���X�ɒ�`����Ă��郁�\�b�h�j���i�[����Ă���D
     * 
     * @param usedClass �g�p�����N���X
     * @return ���p�\�ȃ��\�b�h�ꗗ
     */
    private static List<TargetMethodInfo> getAvailableMethodsInSubClasses(
            final TargetClassInfo usedClass) {

        if (null == usedClass) {
            throw new NullPointerException();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // ���̃N���X�Œ�`����Ă���C�q�N���X����A�N�Z�X�\�ȃ��\�b�h���擾
        for (TargetMethodInfo method : usedClass.getDefinedMethods()) {
            if (method.isInheritanceVisible()) {
                availableMethods.add(method);
            }
        }

        // �e�N���X�Œ�`����Ă���C�q�N���X����A�N�Z�X���\�ȃ��\�b�h���擾
        // List �ɓ����̂ŁC�e�N���X�̃��\�b�h�̌�� add ���Ȃ���΂Ȃ�Ȃ�
        for (ClassInfo superClassInfo : usedClass.getSuperClasses()) {

            if (superClassInfo instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsInSubClasses((TargetClassInfo) superClassInfo);
                availableMethods.addAll(availableMethodsDefinedInSuperClasses);
            }
        }

        return Collections.unmodifiableList(availableMethods);
    }

    /**
     * �o�̓��b�Z�[�W�o�͗p�̃v�����^
     */
    private static final MessagePrinter out = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "NameResolver";
        }
    }, MESSAGE_TYPE.OUT);

    /**
     * �G���[���b�Z�[�W�o�͗p�̃v�����^
     */
    private static final MessagePrinter err = new DefaultMessagePrinter(new MessageSource() {
        public String getMessageSourceName() {
            return "NameResolver";
        }
    }, MESSAGE_TYPE.ERROR);
}
