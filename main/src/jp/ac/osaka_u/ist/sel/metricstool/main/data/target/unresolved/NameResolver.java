package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolved * Info ���� * Info �𓾂邽�߂̖��O�������[�e�B���e�B�N���X
 * 
 * @author y-higo
 * 
 */
public final class NameResolver {

    /**
     * �������^���iUnresolvedTypeInfo�j�ɉ����ς݌^���iTypeInfo�j��Ԃ��D
     * ���ɂ�������
     * �Q�Ƃ���Ă���TypeInfo��classInfoManager�Ɋ܂܂�Ă��Ȃ��ꍇ�͐V�K�Ő������ǉ�����D
     * 
     * @param unresolvedTypeInfo ���O�����������^���
     * @param classInfoManager �Q�ƌ^�̉����ɗp����f�[�^�x�[�X
     * @return ���O�������ꂽ�^���
     */
    public static TypeInfo resolveTypeInfo(final UnresolvedTypeInfo unresolvedTypeInfo,
            final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == unresolvedTypeInfo) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // �������v���~�e�B�u�^�̏ꍇ
        if (unresolvedTypeInfo instanceof PrimitiveTypeInfo) {
            return (PrimitiveTypeInfo) unresolvedTypeInfo;

            // ������void�^�̏ꍇ
        } else if (unresolvedTypeInfo instanceof VoidTypeInfo) {
            return (VoidTypeInfo) unresolvedTypeInfo;

            // �������Q�ƌ^�̏ꍇ
        } else if (unresolvedTypeInfo instanceof UnresolvedReferenceTypeInfo) {

            // ���p�\�Ȗ��O��Ԃ���C�^����T��
            final String[] referenceName = ((UnresolvedReferenceTypeInfo) unresolvedTypeInfo)
                    .getReferenceName();
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
                        final ClassInfo specifiedClassInfo = classInfoManager
                                .getClassInfo(fullQualifiedName);
                        // �N���X��������Ȃ������ꍇ�� null ���Ԃ����
                        return specifiedClassInfo;
                    }
                }
            }

            return null;

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

        // �t�B�[���h���C�y�уt�B�[���h�Q�Ƃ��������Ă��関��`�^���擾
        final String fieldName = fieldReference.getFieldName();
        final UnresolvedTypeInfo unresolvedFieldOwnerClassType = fieldReference.getOwnerClassType();

        // �t�B�[���h�Q��(a)���t�B�[���h�Q��(b)�ɂ������Ă���ꍇ (b.a)
        if (unresolvedFieldOwnerClassType instanceof UnresolvedFieldUsage) {

            // (b)�̃N���X��`���擾
            final TypeInfo fieldOwnerClassType = NameResolver.resolveFieldReference(
                    (UnresolvedFieldUsage) unresolvedFieldOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
            if (null == fieldOwnerClassType){
                
                // ������Ȃ������������s��
                usingMethod.addUnresolvedUsage(fieldReference);
                return null;
            }
            
            // ���p�\�ȃt�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                    (TargetClassInfo) fieldOwnerClassType, usingClass);

            // ���p�\�ȃt�B�[���h���C�������t�B�[���h���Ō���
            for (TargetFieldInfo availableField : availableFields) {

                // ��v����t�B�[���h�������������ꍇ
                if (fieldName.equals(availableField.getName())) {
                    usingMethod.addReferencee(availableField);
                    availableField.addReferencer(usingMethod);

                    // �����ς݃L���b�V���ɂɓo�^
                    resolvedCache.put(fieldReference, availableField.getType());

                    return availableField.getType();
                }
            }

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(fieldReference);
            return null;

            // �t�B�[���h�Q��(a)�����\�b�h�Ăяo��(c())�ɂ������Ă���ꍇ(c().a)
        } else if (unresolvedFieldOwnerClassType instanceof UnresolvedMethodCall) {

            // (c)�̃N���X��`���擾
            final TypeInfo fieldOwnerClassType = NameResolver.resolveMethodCall(
                    (UnresolvedMethodCall) unresolvedFieldOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
            if (null == fieldOwnerClassType){
                
                // ������Ȃ������������s��
                usingMethod.addUnresolvedUsage(fieldReference);
                return null;
            }
            
            // ���p�\�ȃt�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                    (TargetClassInfo) fieldOwnerClassType, usingClass);

            // ���p�\�ȃt�B�[���h�ꗗ���C�������t�B�[���h���Ō���
            for (TargetFieldInfo availableField : availableFields) {

                // �t�B�[���h�������������ꍇ
                if (fieldName.equals(availableField.getName())) {
                    usingMethod.addReferencee(availableField);
                    availableField.addReferencer(usingMethod);

                    // �����ς݃L���b�V���ɂɓo�^
                    resolvedCache.put(fieldReference, availableField.getType());

                    return availableField.getType();
                }
            }

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(fieldReference);
            return null;

            // �t�B�[���h�Q��(a)���G���e�B�e�B�g�p�ɂ������Ă���ꍇ
        } else if (unresolvedFieldOwnerClassType instanceof UnresolvedEntityUsage) {

            // �G���e�B�e�B�̃N���X��`���擾
            final TypeInfo fieldOwnerClassType = NameResolver.resolveEntityUsage(
                    (UnresolvedEntityUsage) unresolvedFieldOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
            if (null == fieldOwnerClassType){
                
                // ������Ȃ������������s��
                usingMethod.addUnresolvedUsage(fieldReference);
                return null;
            }
            
            // ���p�\�ȃt�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                    (TargetClassInfo) fieldOwnerClassType, usingClass);

            // ���p�\�ȃt�B�[���h�ꗗ���C�������t�B�[���h���Ō���
            for (TargetFieldInfo availableField : availableFields) {

                // �t�B�[���h�������������ꍇ
                if (fieldName.equals(availableField.getName())) {
                    usingMethod.addReferencee(availableField);
                    availableField.addReferencer(usingMethod);

                    // �����ς݃L���b�V���ɓo�^
                    resolvedCache.put(fieldReference, availableField.getType());
                }
            }

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(fieldReference);
            return null;

            // �t�B�[���h�g�p(a)�����I�u�W�F�N�g�ɂ������Ă���ꍇ(a or this.a or super.a )
        } else if (unresolvedFieldOwnerClassType instanceof UnresolvedClassInfo) {

            // �g�p�\�ȃt�B�[���h������C�������t�B�[���h�g�p������
            {
                // ���p�\�ȃt�B�[���h�ꗗ���擾
                final List<TargetFieldInfo> availableFields = NameResolver
                        .getAvailableFields(usingClass);

                // ���p�\�ȃt�B�[���h�ꗗ���C�������t�B�[���h���Ō���
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

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(fieldReference);
            return null;
        }

        throw new IllegalArgumentException(fieldReference.toString() + " is wrong!");
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

        // �t�B�[���h���C�y�уt�B�[���h������������Ă��関��`�^���擾
        final String fieldName = fieldAssignment.getFieldName();
        final UnresolvedTypeInfo unresolvedFieldOwnerClassType = fieldAssignment
                .getOwnerClassType();

        // �t�B�[���h���(a)���t�B�[���h�Q��(b)�ɂ������Ă���ꍇ (b.a)
        if (unresolvedFieldOwnerClassType instanceof UnresolvedFieldUsage) {

            // (b)�̃N���X��`���擾
            final TypeInfo fieldOwnerClassType = NameResolver.resolveFieldReference(
                    (UnresolvedFieldUsage) unresolvedFieldOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
            if (null == fieldOwnerClassType){
                
                // ������Ȃ������������s��
                usingMethod.addUnresolvedUsage(fieldAssignment);
                return null;
            }
            
            // ���p�\�ȃt�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                    (TargetClassInfo) fieldOwnerClassType, usingClass);

            // ���p�\�ȃt�B�[���h���C�������G���e�B�e�B���Ō���
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

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(fieldAssignment);
            return null;

            // �t�B�[���h���(a)�����\�b�h�Ăяo��(c())�ɂ������Ă���ꍇ(c().a)
        } else if (unresolvedFieldOwnerClassType instanceof UnresolvedMethodCall) {

            // (c)�̃N���X��`���擾
            final TypeInfo fieldOwnerClassType = NameResolver.resolveMethodCall(
                    (UnresolvedMethodCall) unresolvedFieldOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
            if (null == fieldOwnerClassType){
                
                // ������Ȃ������������s��
                usingMethod.addUnresolvedUsage(fieldAssignment);
                return null;
            }
            
            // ���p�\�ȃt�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                    (TargetClassInfo) fieldOwnerClassType, usingClass);

            // ���p�\�ȃt�B�[���h�ꗗ���C�������t�B�[���h���Ō���
            for (TargetFieldInfo availableField : availableFields) {

                // �t�B�[���h�������������ꍇ
                if (fieldName.equals(availableField.getName())) {
                    usingMethod.addAssignmentee(availableField);
                    availableField.addAssignmenter(usingMethod);

                    // �����ς݃L���b�V���ɂɓo�^
                    resolvedCache.put(fieldAssignment, availableField.getType());

                    return availableField.getType();
                }
            }

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(fieldAssignment);
            return null;

            // �t�B�[���h���(a)���G���e�B�e�B�g�p�ɂ������Ă���ꍇ
        } else if (unresolvedFieldOwnerClassType instanceof UnresolvedEntityUsage) {

            // �G���e�B�e�B�̃N���X��`���擾
            final TypeInfo fieldOwnerClassType = NameResolver.resolveEntityUsage(
                    (UnresolvedEntityUsage) unresolvedFieldOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
            if (null == fieldOwnerClassType){
                
                // ������Ȃ������������s��
                usingMethod.addUnresolvedUsage(fieldAssignment);
                return null;
            }
            
            // ���p�\�ȃt�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFields = NameResolver.getAvailableFields(
                    (TargetClassInfo) fieldOwnerClassType, usingClass);

            // ���p�\�ȃt�B�[���h�ꗗ���C�������t�B�[���h���Ō���
            for (TargetFieldInfo availableField : availableFields) {

                // �t�B�[���h�������������ꍇ
                if (fieldName.equals(availableField.getName())) {
                    usingMethod.addAssignmentee(availableField);
                    availableField.addAssignmenter(usingMethod);

                    // �����ς݃L���b�V���ɓo�^
                    resolvedCache.put(fieldAssignment, availableField.getType());
                }
            }

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(fieldAssignment);
            return null;

            // �t�B�[���h���(a)�����I�u�W�F�N�g�ɂ������Ă���ꍇ(a or this.a or super.a )
        } else if (unresolvedFieldOwnerClassType instanceof UnresolvedClassInfo) {

            // �g�p�\�ȃt�B�[���h������C�������t�B�[���h���������
            {
                // ���p�\�ȃt�B�[���h�ꗗ���擾
                final List<TargetFieldInfo> availableFields = NameResolver
                        .getAvailableFields(usingClass);

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

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(fieldAssignment);
            return null;
        }

        throw new IllegalArgumentException(fieldAssignment.toString() + " is wrong!");
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

        // ���\�b�h�̃V�O�l�`���C�y�у��\�b�h�g�p���������Ă��関��`�Q�ƌ^���擾
        final String methodName = methodCall.getMethodName();
        final List<UnresolvedTypeInfo> unresolvedParameterTypes = methodCall.getParameterTypes();
        final UnresolvedTypeInfo unresolvedMethodOwnerClassType = methodCall.getOwnerClassType();

        // ���\�b�h�̖���������������
        final List<TypeInfo> parameterTypes = new LinkedList<TypeInfo>();
        for (UnresolvedTypeInfo unresolvedParameterType : unresolvedParameterTypes) {

            if (unresolvedParameterType instanceof UnresolvedFieldUsage) {
                final TypeInfo parameterType = NameResolver.resolveFieldReference(
                        (UnresolvedFieldUsage) unresolvedParameterType, usingClass, usingMethod,
                        classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);
                parameterTypes.add(parameterType);
            }
        }

        // ���\�b�h�Ăяo��(a())���t�B�[���h�g�p(b)�ɂ������Ă���ꍇ (b.a())
        if (unresolvedMethodOwnerClassType instanceof UnresolvedFieldUsage) {

            // (b)�̃N���X��`���擾
            final TypeInfo methodOwnerClassType = NameResolver.resolveFieldReference(
                    (UnresolvedFieldUsage) unresolvedMethodOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
            if (null == methodOwnerClassType){
                
                // ������Ȃ������������s��
                usingMethod.addUnresolvedUsage(methodCall);
                return null;
            }
            
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

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(methodCall);
            return null;

            // ���\�b�h�Ăяo��(a())�����\�b�h�Ăяo��(c())�ɂ������Ă���ꍇ(c().a())
        } else if (unresolvedMethodOwnerClassType instanceof UnresolvedMethodCall) {

            // (c)�̃N���X��`���擾
            final TypeInfo methodOwnerClassType = NameResolver.resolveMethodCall(
                    (UnresolvedMethodCall) unresolvedMethodOwnerClassType, usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager, resolvedCache);

            // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
            if (null == methodOwnerClassType){
                
                // ������Ȃ������������s��
                usingMethod.addUnresolvedUsage(methodCall);
                return null;
            }
            
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

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(methodCall);
            return null;

            // ���\�b�h�Ăяo��(a())���G���e�B�e�B�g�p�ɂ������Ă���ꍇ
        } else if (unresolvedMethodOwnerClassType instanceof UnresolvedEntityUsage) {

            // �G���e�B�e�B�̃N���X��`���擾
            final TypeInfo methodOwnerClassType = NameResolver.resolveEntityUsage(
                    (UnresolvedEntityUsage) unresolvedMethodOwnerClassType, usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager,
                    resolvedCache);

            // �e�������ł��Ȃ������ꍇ�͂ǂ����悤���Ȃ�
            if (null == methodOwnerClassType){
                
                // ������Ȃ������������s��
                usingMethod.addUnresolvedUsage(methodCall);
                return null;
            }
            
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

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(methodCall);
            return null;

            // ���\�b�h�Ăяo��(a())�����I�u�W�F�N�g�ɂ������Ă���ꍇ(a or this.a or super.a )
        } else if (unresolvedMethodOwnerClassType instanceof UnresolvedClassInfo) {

            // ���p�\�ȃ��\�b�h�ꗗ���擾
            final List<TargetMethodInfo> availableMethods = NameResolver
                    .getAvailableMethods(usingClass);

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

            // ������Ȃ������������s��
            usingMethod.addUnresolvedUsage(methodCall);
            return null;
        }

        throw new IllegalArgumentException(methodCall.toString() + " is wrong!");
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
            return type;
        }

        // �G���e�B�e�B�Q�Ɩ����擾
        final String[] name = entityUsage.getName();

        // ���p�\�ȃt�B�[���h������G���e�B�e�B��������
        {
            // ���̃N���X�ŗ��p�\�ȃt�B�[���h�ꗗ���擾
            final List<TargetFieldInfo> availableFieldsOfThisClass = NameResolver
                    .getAvailableFields(usingClass);

            for (TargetFieldInfo availableFieldOfThisClass : availableFieldsOfThisClass) {

                // ��v����t�B�[���h�������������ꍇ
                if (name[0].equals(availableFieldOfThisClass.getName())) {
                    usingMethod.addReferencee(availableFieldOfThisClass);
                    availableFieldOfThisClass.addReferencer(usingMethod);

                    // availableField.getType() ���玟��word(name[i])�𖼑O����
                    TypeInfo ownerTypeInfo = availableFieldOfThisClass.getType();
                    for (int i = 1; i < name.length; i++) {

                        // ���p�\�ȃt�B�[���h�ꗗ���擾
                        final List<TargetFieldInfo> availableFields = NameResolver
                                .getAvailableFields((TargetClassInfo) ownerTypeInfo, usingClass);

                        boolean found = false;
                        for (TargetFieldInfo availableField : availableFields) {

                            // ��v����t�B�[���h�������������ꍇ
                            if (name[i].equals(availableField.getName())) {
                                usingMethod.addReferencee(availableField);
                                availableField.addReferencer(usingMethod);

                                ownerTypeInfo = availableField.getType();
                                found = true;
                            }
                        }

                        if (!found) {
                            
                            // ������Ȃ������������s��
                            usingMethod.addUnresolvedUsage(entityUsage);
                            return null;
                        }
                    }

                    // �����ς݃L���b�V���ɓo�^
                    resolvedCache.put(entityUsage, ownerTypeInfo);

                    return ownerTypeInfo;
                }
            }
        }

        // ���p�\�ȃN���X������G���e�B�e�B��������
        {

            for (int length = 1; length <= name.length; length++) {

                // �������閼�O(String[])���쐬
                final String[] searchingName = new String[length];
                System.arraycopy(name, 0, searchingName, 0, length);

                final ClassInfo searchingClass = classInfoManager.getClassInfo(searchingName);
                if (null != searchingClass) {

                    TypeInfo ownerTypeInfo = searchingClass;
                    for (int i = length; i < name.length; i++) {

                        // ���p�\�ȃt�B�[���h�ꗗ���擾
                        final List<TargetFieldInfo> availableFields = NameResolver
                                .getAvailableFields((TargetClassInfo) ownerTypeInfo, usingClass);

                        boolean found = false;
                        for (TargetFieldInfo availableField : availableFields) {

                            // ��v����t�B�[���h�����������ꍇ
                            if (name[i].equals(availableField.getName())) {
                                usingMethod.addReferencee(availableField);
                                availableField.addReferencer(usingMethod);

                                ownerTypeInfo = availableField.getType();
                                found = true;
                            }
                        }

                        if (!found) {

                            // ������Ȃ������������s��
                            usingMethod.addUnresolvedUsage(entityUsage);
                            return null;
                        }
                    }

                    // �����ς݃L���b�V���ɓo�^
                    resolvedCache.put(entityUsage, ownerTypeInfo);

                    return ownerTypeInfo;
                }
            }
        }

        // ������Ȃ������������s��
        usingMethod.addUnresolvedUsage(entityUsage);
        return null;
    }

    /**
     * �������t�B�[���h��񂩂�C�Ή�����FieldInfo ��Ԃ��D�Y�����郁�\�b�h���Ȃ��ꍇ�́C IllegalArgumentException ����������
     * 
     * @param unresolvedFieldInfo �������t�B�[���h���
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @return �Ή����� FieldInfo
     */
    public static TargetFieldInfo resolveFieldInfo(final UnresolvedFieldInfo unresolvedFieldInfo,
            final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == unresolvedFieldInfo) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // ���L�N���X���擾�C�擾�����N���X���O���N���X�ł���ꍇ�́C�����̏�񂪂�������
        final UnresolvedClassInfo unresolvedOwnerClass = unresolvedFieldInfo.getOwnerClass();
        final ClassInfo ownerClass = NameResolver.resolveClassInfo(unresolvedOwnerClass,
                classInfoManager);
        if (!(ownerClass instanceof TargetClassInfo)) {
            throw new IllegalArgumentException(unresolvedFieldInfo.toString() + " is wrong!");
        }

        // UnresolvedFieldInfo ����t�B�[���h���C�^�����擾
        final String fieldName = unresolvedFieldInfo.getName();
        final UnresolvedTypeInfo unresolvedFieldType = unresolvedFieldInfo.getType();
        TypeInfo fieldType = NameResolver.resolveTypeInfo(unresolvedFieldType, classInfoManager);
        if (null == fieldType) {
            fieldType = NameResolver
                    .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedFieldType);
            classInfoManager.add((ExternalClassInfo) fieldType);
        }

        for (TargetFieldInfo fieldInfo : ((TargetClassInfo) ownerClass).getDefinedFields()) {

            // �t�B�[���h�����Ⴄ�ꍇ�́C�Y���t�B�[���h�ł͂Ȃ�
            if (!fieldName.equals(fieldInfo.getName())) {
                continue;
            }

            // �t�B�[���h�̌^���Ⴄ�ꍇ�́C�Y���t�B�[���h�ł͂Ȃ�
            if (!fieldType.equals(fieldInfo.getType())) {
                continue;
            }

            return fieldInfo;
        }

        throw new IllegalArgumentException(unresolvedFieldInfo.toString() + " is wrong!");
    }

    /**
     * ���������\�b�h��񂩂�C�Ή�����MethodInfo ��Ԃ��D�Y�����郁�\�b�h���Ȃ��ꍇ�� IllegalArgumentException ����������
     * 
     * @param unresolvedMethodInfo ���������\�b�h���
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @return �Ή����� MethodInfo
     */
    public static TargetMethodInfo resolveMethodInfo(
            final UnresolvedMethodInfo unresolvedMethodInfo, final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == unresolvedMethodInfo) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // UnresolvedMethodInfo ���珊�L�N���X���擾�C�擾�����N���X���O���N���X�ł���ꍇ�́C�����̏�񂪂�������
        final UnresolvedClassInfo unresolvedOwnerClass = unresolvedMethodInfo.getOwnerClass();
        final ClassInfo ownerClass = NameResolver.resolveClassInfo(unresolvedOwnerClass,
                classInfoManager);
        if (!(ownerClass instanceof TargetClassInfo)) {
            throw new IllegalArgumentException(unresolvedMethodInfo.toString() + " is wrong!");
        }

        // Unresolved ���\�b�h���C�������擾
        final String methodName = unresolvedMethodInfo.getMethodName();
        final List<UnresolvedParameterInfo> unresolvedParameterInfos = unresolvedMethodInfo
                .getParameterInfos();

        for (TargetMethodInfo methodInfo : ((TargetClassInfo) ownerClass).getDefinedMethods()) {

            // ���\�b�h�����Ⴄ�ꍇ�́C�Y�����\�b�h�ł͂Ȃ�
            if (!methodName.equals(methodInfo.getMethodName())) {
                continue;
            }

            // �����̐����Ⴄ�ꍇ�́C�Y�����\�b�h�ł͂Ȃ�
            final List<ParameterInfo> typeInfos = methodInfo.getParameters();
            if (unresolvedParameterInfos.size() != typeInfos.size()) {
                continue;
            }

            // �S�Ă̈����̌^���`�F�b�N�C1�ł��قȂ�ꍇ�́C�Y�����\�b�h�ł͂Ȃ�
            final Iterator<UnresolvedParameterInfo> unresolvedParameterIterator = unresolvedParameterInfos
                    .iterator();
            final Iterator<ParameterInfo> parameterInfoIterator = typeInfos.iterator();
            boolean same = true;
            while (unresolvedParameterIterator.hasNext() && parameterInfoIterator.hasNext()) {
                final UnresolvedParameterInfo unresolvedParameterInfo = unresolvedParameterIterator
                        .next();
                final UnresolvedTypeInfo unresolvedTypeInfo = unresolvedParameterInfo.getType();
                TypeInfo typeInfo = NameResolver.resolveTypeInfo(unresolvedTypeInfo,
                        classInfoManager);
                if (null == typeInfo) {
                    typeInfo = NameResolver
                            .createExternalClassInfo((UnresolvedReferenceTypeInfo) unresolvedTypeInfo);
                    classInfoManager.add((ExternalClassInfo) typeInfo);
                }
                final ParameterInfo parameterInfo = parameterInfoIterator.next();
                if (!typeInfo.equals(parameterInfo.getType())) {
                    same = false;
                    break;
                }
            }
            if (same) {
                return methodInfo;
            }
        }

        throw new IllegalArgumentException(unresolvedMethodInfo.toString() + " is wrong!");
    }

    /**
     * �������N���X��񂩂�C�Y������ ClassInfo ��Ԃ��D�Y������N���X���Ȃ��ꍇ�� IllegalArgumentException ����������
     * 
     * @param unresolvedClassInfo �������N���X���
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @return �Y������ ClassInfo
     */
    public static TargetClassInfo resolveClassInfo(final UnresolvedClassInfo unresolvedClassInfo,
            final ClassInfoManager classInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == unresolvedClassInfo) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // ���S���薼���擾���CClassInfo ���擾
        final String[] fullQualifiedName = unresolvedClassInfo.getFullQualifiedName();
        final ClassInfo classInfo = classInfoManager.getClassInfo(fullQualifiedName);

        // UnresolvedClassInfo �̃I�u�W�F�N�g�� registClassInfo ���\�b�h�ɂ��C�S�ēo�^�ς݂̂͂��Ȃ̂ŁC
        // null ���Ԃ��Ă����ꍇ�́C�s��
        if (null == classInfo) {
            throw new IllegalArgumentException(unresolvedClassInfo.toString() + " is wrong!");
        }
        // registClassInfo�ɂ��o�^���ꂽ�N���X���� TargetClassInfo �ł���ׂ�
        if (!(classInfo instanceof TargetClassInfo)) {
            throw new IllegalArgumentException(unresolvedClassInfo.toString() + " is wrong!");
        }

        return (TargetClassInfo) classInfo;
    }

    /**
     * �u���݂̃N���X�v�ŗ��p�\�ȃt�B�[���h�ꗗ��Ԃ��D
     * �����ŁC�u���p�\�ȃt�B�[���h�v�Ƃ́C�u���݂̃N���X�v�Œ�`����Ă���t�B�[���h�C�y�т��̐e�N���X�Œ�`����Ă���t�B�[���h�̂����q�N���X����A�N�Z�X���\�ȃt�B�[���h�ł���D
     * ���p�\�ȃt�B�[���h�� List �Ɋi�[����Ă���D ���X�g�̐擪����D�揇�ʂ̍����t�B�[���h�i�܂�C�N���X�K�w�ɂ����ĉ��ʂ̃N���X�ɒ�`����Ă���t�B�[���h�j���i�[����Ă���D
     * 
     * @param thisClass ���݂̃N���X
     * @return ���p�\�ȃt�B�[���h�ꗗ
     */
    public static List<TargetFieldInfo> getAvailableFields(final TargetClassInfo thisClass) {

        if (null == thisClass) {
            throw new NullPointerException();
        }

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // ���̃N���X�Œ�`����Ă���t�B�[���h�ꗗ���擾����
        availableFields.addAll(thisClass.getDefinedFields());

        // �e�N���X�Œ�`����Ă���C���̃N���X����A�N�Z�X���\�ȃt�B�[���h���擾
        for (ClassInfo superClass : thisClass.getSuperClasses()) {

            if (superClass instanceof TargetClassInfo) {
                final List<TargetFieldInfo> availableFieldsDefinedInSuperClasses = NameResolver
                        .getAvailableFieldsInSubClasses((TargetClassInfo) superClass);
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
    public static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo thisClass) {

        if (null == thisClass) {
            throw new NullPointerException();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // ���̃N���X�Œ�`����Ă��郁�\�b�h�ꗗ���擾����
        availableMethods.addAll(thisClass.getDefinedMethods());

        // �e�N���X�Œ�`����Ă���C���̃N���X����A�N�Z�X���\�ȃ��\�b�h���擾
        for (ClassInfo superClass : thisClass.getSuperClasses()) {

            if (superClass instanceof TargetClassInfo) {
                final List<TargetMethodInfo> availableMethodsDefinedInSuperClasses = NameResolver
                        .getAvailableMethodsInSubClasses((TargetClassInfo) superClass);
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

        final List<TargetFieldInfo> availableFields = new LinkedList<TargetFieldInfo>();

        // ���̃N���X�Œ�`����Ă���t�B�[���h�̂����C�g�p����N���X�ŗ��p�\�ȃt�B�[���h���擾����
        // 2�̃N���X���������O��Ԃ������Ă���ꍇ
        if (usedClass.getNamespace().equals(usingClass.getNamespace())) {

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
    public static List<TargetMethodInfo> getAvailableMethods(final TargetClassInfo usedClass,
            final TargetClassInfo usingClass) {

        if ((null == usedClass) || (null == usingClass)) {
            throw new NullPointerException();
        }

        final List<TargetMethodInfo> availableMethods = new LinkedList<TargetMethodInfo>();

        // ���̃N���X�Œ�`����Ă��郁�\�b�h�̂����C�g�p����N���X�ŗ��p�\�ȃ��\�b�h���擾����
        // 2�̃N���X���������O��Ԃ������Ă���ꍇ
        if (usedClass.getNamespace().equals(usingClass.getNamespace())) {

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
    public static List<TargetFieldInfo> getAvailableFieldsInSubClasses(
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
    public static List<TargetMethodInfo> getAvailableMethodsInSubClasses(
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
