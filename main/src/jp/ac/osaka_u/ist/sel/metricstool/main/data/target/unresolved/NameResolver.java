package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Iterator;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolved * Info ���� * Info �𓾂邽�߂̖��O�������[�e�B���e�B�N���X
 * 
 * @author y-higo
 * 
 */
public final class NameResolver {

    /**
     * UnresolvedTypeInfo�ȏ�񂩂� TypeInfo�𐶐�����D�Q�Ƃ���Ă���TypeInfo��classInfoManager�Ɋ܂܂�Ă��Ȃ��ꍇ�͒ǉ�����D
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

        // �v���~�e�B�u�^�̏ꍇ
        if (unresolvedTypeInfo instanceof PrimitiveTypeInfo) {
            return (PrimitiveTypeInfo) unresolvedTypeInfo;

            // void�^�̏ꍇ
        } else if (unresolvedTypeInfo instanceof VoidTypeInfo) {
            return (VoidTypeInfo) unresolvedTypeInfo;

            // �Q�ƌ^�̏ꍇ
        } else if (unresolvedTypeInfo instanceof UnresolvedReferenceTypeInfo) {

            // ���p�\�Ȗ��O��Ԃ���C�^����T��
            String[] referenceName = ((UnresolvedReferenceTypeInfo) unresolvedTypeInfo)
                    .getReferenceName();
            for (AvailableNamespaceInfo availableNamespace : ((UnresolvedReferenceTypeInfo) unresolvedTypeInfo)
                    .getAvailableNamespaces()) {

                // ���O��Ԗ�.* �ƂȂ��Ă���ꍇ
                if (availableNamespace.isAllClasses()) {
                    String[] namespace = availableNamespace.getNamespace();

                    // ���O��Ԃ̉��ɂ���e�N���X�ɑ΂���
                    for (ClassInfo classInfo : classInfoManager.getClassInfos(namespace)) {
                        String className = classInfo.getClassName();

                        // �N���X���ƎQ�Ɩ��̐擪���������ꍇ�́C���̃N���X�����Q�Ɛ�ł���ƌ��肷��
                        if (className.equals(referenceName[0])) {
                            String[] fullQualifiedName = new String[namespace.length
                                    + referenceName.length];
                            System.arraycopy(namespace, 0, fullQualifiedName, 0, namespace.length);
                            System.arraycopy(referenceName, 0, fullQualifiedName, namespace.length,
                                    referenceName.length);
                            ClassInfo specifiedClassInfo = classInfoManager
                                    .getClassInfo(fullQualifiedName);
                            if (null == specifiedClassInfo) {
                                specifiedClassInfo = new ExternalClassInfo(fullQualifiedName);
                                classInfoManager.add((ExternalClassInfo) specifiedClassInfo);
                            }
                            return specifiedClassInfo;
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
                        ClassInfo specifiedClassInfo = classInfoManager
                                .getClassInfo(fullQualifiedName);
                        if (null == specifiedClassInfo) {
                            specifiedClassInfo = new ExternalClassInfo(fullQualifiedName);
                            classInfoManager.add((ExternalClassInfo) specifiedClassInfo);
                        }
                        return specifiedClassInfo;
                    }
                }
            }

            // ������Ȃ������ꍇ�͖��O��Ԗ���UNKNOWN�ȃN���X��o�^����
            ExternalClassInfo classInfo = new ExternalClassInfo(referenceName[referenceName.length - 1]);
            classInfoManager.add(classInfo);
            return classInfo;

            // ����ȊO�̌^�̏ꍇ�̓G���[
        } else {
            throw new IllegalArgumentException(unresolvedTypeInfo.toString()
                    + " is a wrong object!");
        }
    }

    /**
     * �t�B�[���h�g�p��񂩂�C�Y������ FieldInfo ��Ԃ��D�Y������ FieldInfo ���Ȃ��ꍇ�͐�������D
     * 
     * @param fieldUsage �t�B�[���h�g�p���
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @return �t�B�[���h�g�p���ɑΉ����� FieldInfo
     */
    public static FieldInfo resolveFieldUsage(final UnresolvedFieldUsage fieldUsage,
            final ClassInfoManager classInfoManager, final FieldInfoManager fieldInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == fieldUsage) || (null == classInfoManager) || (null == fieldInfoManager)) {
            throw new NullPointerException();
        }

        // �t�B�[���h�����擾
        String fieldName = fieldUsage.getFieldName();

        // �t�B�[���h����`����Ă���N���X�̌^�����
        UnresolvedTypeInfo unresolvedOwnerClassType = fieldUsage.getOwnerClassType();
        TypeInfo ownerClassType = NameResolver.resolveTypeInfo(unresolvedOwnerClassType,
                classInfoManager);

        // �O���N���X�̏ꍇ�́C�Ăяo���t�B�[���h���𐶐����C�t�B�[���h�}�l�[�W���ɒǉ�
        if (ownerClassType instanceof ExternalClassInfo) {

            ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                    (ClassInfo) ownerClassType);
            fieldInfoManager.add(fieldInfo);
            return fieldInfo;

            // �ΏۃN���X�̏ꍇ�́C���̃N���X����Y���t�B�[���h�̒T���C
            // ������Ȃ���΁C�������o�^����
        } else if (ownerClassType instanceof TargetClassInfo) {

            for (TargetFieldInfo fieldInfo : ((TargetClassInfo) ownerClassType).getDefinedFields()) {

                if (fieldName.equals(fieldInfo.getName())) {
                    return fieldInfo;
                }
            }

            // �g�p����Ă���t�B�[���h��ClassInfoManager�ɓo�^����Ă��Ȃ��̂ŁC�ǉ�����D
            // �ΏۃN���X�ɒ�`����Ă��Ȃ��t�B�[���h�Ȃ̂� ExternalFieldInfo �ɂȂ�.
            // �����炭�ΏۃN���X�́i�O���́j�e�N���X�ɒ�`����Ă���t�B�[���h�̎g�p�D
            ExternalFieldInfo fieldInfo = new ExternalFieldInfo(fieldName,
                    (ClassInfo) ownerClassType);
            fieldInfoManager.add(fieldInfo);
            return fieldInfo;
        }

        throw new IllegalArgumentException(fieldUsage.toString() + " is wrong!");
    }

    /**
     * ���\�b�h�Ăяo����񂩂�C�Y������ MethodInfo ��Ԃ��D �Y������ MethodInfo ���Ȃ��ꍇ�͐�������D
     * 
     * @param methodCall ���\�b�h�Ăяo�����
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * @return ���\�b�h�Ăяo�����ɑΉ����� MethodInfo
     */
    public static MethodInfo resolveMethodCall(final UnresolvedMethodCall methodCall,
            final ClassInfoManager classInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodCall) || (null == classInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // ���\�b�h���C�������擾
        String methodName = methodCall.getMethodName();
        List<UnresolvedTypeInfo> unresolvedTypeInfos = methodCall.getParameterTypes();

        // �N���X�����
        String[] ownerClassName = methodCall.getOwnerClassName();
        ClassInfo ownerClass = classInfoManager.getClassInfo(ownerClassName);
        if (null == ownerClass) {
            ownerClass = new ExternalClassInfo(ownerClassName);
            classInfoManager.add((ExternalClassInfo) ownerClass);
        }

        // ���\�b�h�Ăяo�������s����Ă���̂��C�O���̌^�i�N���X�j�̏ꍇ
        if (ownerClass instanceof ExternalClassInfo) {

            // �Ăяo���惁�\�b�h�̃I�u�W�F�N�g�𐶐�
            ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName, ownerClass,
                    methodCall.isConstructor());
            for (UnresolvedTypeInfo unresolvedTypeInfo : methodCall.getParameterTypes()) {
                TypeInfo parameterType = NameResolver.resolveTypeInfo(unresolvedTypeInfo,
                        classInfoManager);
                ExternalParameterInfo parameterInfo = new ExternalParameterInfo(parameterType);
                methodInfo.addParameter(parameterInfo);
            }
            methodInfoManager.add(methodInfo);
            return methodInfo;

            // ���\�b�h�Ăяo�������s����Ă���̂��C�Ώۂ̌^�i�N���X�j�̏ꍇ
        } else if (ownerClass instanceof TargetClassInfo) {

            for (TargetMethodInfo methodInfo : ((TargetClassInfo) ownerClass).getDefinedMethods()) {

                // ���\�b�h�����Ⴄ�ꍇ�́C�Y�����\�b�h�ł͂Ȃ�
                if (!methodName.equals(methodInfo.getMethodName())) {
                    continue;
                }

                // �����̐����Ⴄ�ꍇ�́C�Y�����\�b�h�ł͂Ȃ�
                List<ParameterInfo> typeInfos = methodInfo.getParameters();
                if (unresolvedTypeInfos.size() != typeInfos.size()) {
                    continue;
                }

                // �S�Ă̈����̌^���`�F�b�N�C1�ł��قȂ�ꍇ�́C�Y�����\�b�h�ł͂Ȃ�
                Iterator<UnresolvedTypeInfo> unresolvedTypeInfoIterator = unresolvedTypeInfos
                        .iterator();
                Iterator<ParameterInfo> parameterInfoIterator = typeInfos.iterator();
                boolean same = true;
                while (unresolvedTypeInfoIterator.hasNext() && parameterInfoIterator.hasNext()) {
                    UnresolvedTypeInfo unresolvedTypeInfo = unresolvedTypeInfoIterator.next();
                    TypeInfo typeInfo = NameResolver.resolveTypeInfo(unresolvedTypeInfo,
                            classInfoManager);
                    ParameterInfo parameterInfo = parameterInfoIterator.next();
                    if (!typeInfo.equals(parameterInfo.getType())) {
                        same = false;
                        break;
                    }
                }
                if (same) {
                    return methodInfo;
                }
            }
        }

        // �Ăяo����Ă��郁�\�b�h��ClassInfoManager�ɓo�^����Ă��Ȃ��̂ŁC�ǉ�����D
        // �ΏۃN���X�ɒ�`����Ă��Ȃ����\�b�h�Ȃ̂� ExternalMethodInfo �ɂȂ�.
        // �����炭�ΏۃN���X�́i�O���́j�e�N���X�ɒ�`����Ă��郁�\�b�h�̌Ăяo���D
        ExternalMethodInfo methodInfo = new ExternalMethodInfo(methodName, ownerClass, methodCall
                .isConstructor());
        for (UnresolvedTypeInfo unresolvedTypeInfo : methodCall.getParameterTypes()) {
            TypeInfo parameterType = NameResolver.resolveTypeInfo(unresolvedTypeInfo,
                    classInfoManager);
            ExternalParameterInfo parameterInfo = new ExternalParameterInfo(parameterType);
            methodInfo.addParameter(parameterInfo);
        }
        methodInfoManager.add(methodInfo);
        return methodInfo;
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
        UnresolvedClassInfo unresolvedOwnerClass = unresolvedFieldInfo.getOwnerClass();
        ClassInfo ownerClass = NameResolver
                .resolveClassInfo(unresolvedOwnerClass, classInfoManager);
        if (!(ownerClass instanceof TargetClassInfo)) {
            throw new IllegalArgumentException(unresolvedFieldInfo.toString() + " is wrong!");
        }

        // UnresolvedFieldInfo ����t�B�[���h���C�^�����擾
        String fieldName = unresolvedFieldInfo.getName();
        UnresolvedTypeInfo unresolvedFieldType = unresolvedFieldInfo.getType();
        TypeInfo fieldType = NameResolver.resolveTypeInfo(unresolvedFieldType, classInfoManager);

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
        UnresolvedClassInfo unresolvedOwnerClass = unresolvedMethodInfo.getOwnerClass();
        ClassInfo ownerClass = NameResolver
                .resolveClassInfo(unresolvedOwnerClass, classInfoManager);
        if (!(ownerClass instanceof TargetClassInfo)) {
            throw new IllegalArgumentException(unresolvedMethodInfo.toString() + " is wrong!");
        }

        // Unresolved ���\�b�h���C�������擾
        String methodName = unresolvedMethodInfo.getMethodName();
        List<UnresolvedParameterInfo> unresolvedParameterInfos = unresolvedMethodInfo
                .getParameterInfos();

        for (TargetMethodInfo methodInfo : ((TargetClassInfo) ownerClass).getDefinedMethods()) {

            // ���\�b�h�����Ⴄ�ꍇ�́C�Y�����\�b�h�ł͂Ȃ�
            if (!methodName.equals(methodInfo.getMethodName())) {
                continue;
            }

            // �����̐����Ⴄ�ꍇ�́C�Y�����\�b�h�ł͂Ȃ�
            List<ParameterInfo> typeInfos = methodInfo.getParameters();
            if (unresolvedParameterInfos.size() != typeInfos.size()) {
                continue;
            }

            // �S�Ă̈����̌^���`�F�b�N�C1�ł��قȂ�ꍇ�́C�Y�����\�b�h�ł͂Ȃ�
            Iterator<UnresolvedParameterInfo> unresolvedParameterIterator = unresolvedParameterInfos
                    .iterator();
            Iterator<ParameterInfo> parameterInfoIterator = typeInfos.iterator();
            boolean same = true;
            while (unresolvedParameterIterator.hasNext() && parameterInfoIterator.hasNext()) {
                UnresolvedParameterInfo unresolvedParameterInfo = unresolvedParameterIterator
                        .next();
                UnresolvedTypeInfo unresolvedTypeInfo = unresolvedParameterInfo.getType();
                TypeInfo typeInfo = NameResolver.resolveTypeInfo(unresolvedTypeInfo,
                        classInfoManager);
                ParameterInfo parameterInfo = parameterInfoIterator.next();
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
        String[] fullQualifiedName = unresolvedClassInfo.getFullQualifiedName();
        ClassInfo classInfo = classInfoManager.getClassInfo(fullQualifiedName);

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
}
