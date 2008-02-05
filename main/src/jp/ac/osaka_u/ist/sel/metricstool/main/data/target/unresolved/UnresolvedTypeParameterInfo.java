package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �������^�p�����[�^��\�����ۃN���X
 * 
 * @author higo
 * 
 */
public class UnresolvedTypeParameterInfo implements UnresolvedTypeInfo {

    /**
     * �^�p�����[�^����^���ăI�u�W�F�N�g������������
     * 
     * @param name �^�p�����[�^��
     * @param extends ���������N���X�^
     */
    public UnresolvedTypeParameterInfo(final String name, final UnresolvedTypeInfo extendsType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
        this.extendsType = extendsType;
    }

    /**
     * ���ɖ��O��������Ă��邩�ǂ�����Ԃ�
     * 
     * @return ���ɖ��O��������Ă���ꍇ�� true, �����łȂ��ꍇ�� false
     */
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * ���O�������ꂽ����Ԃ�
     * 
     * @return ���O�������ꂽ���
     * @throws NotResolvedException
     */
    public final TypeInfo getResolvedType() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * ���O�������s��
     * 
     * @param usingClass ���O�������s���G���e�B�e�B������N���X
     * @param usingMethod ���O�������s���G���e�B�e�B�����郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * 
     * @return �����ς݂̃G���e�B�e�B
     */
    public TypeInfo resolveType(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // �s���ȌĂяo���łȂ������`�F�b�N
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        final String name = this.getName();

        if (this.hasExtendsType()) {

            final UnresolvedTypeInfo unresolvedExtendsType = this.getExtendsType();
            final TypeInfo extendsType = unresolvedExtendsType.resolveType(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            this.resolvedInfo = new TypeParameterInfo(name, extendsType);

        } else {

            this.resolvedInfo = new TypeParameterInfo(name, null);
        }

        return this.resolvedInfo;
    }

    /**
     * �^�p�����[�^����Ԃ�
     * 
     * @return �^�p�����[�^��
     */
    public final String getName() {
        return this.name;
    }

    /**
     * ���N���X�̖������^����Ԃ�
     * 
     * @return ���N���X�̖������^���
     */
    public final UnresolvedTypeInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * ���N���X�������ǂ�����Ԃ�
     * 
     * @return ���N���X�����ꍇ�� true, �����Ȃ��ꍇ�� false
     */
    public final boolean hasExtendsType() {
        return null != this.extendsType;
    }

    /**
     * �^�p�����[�^����ۑ����邽�߂̕ϐ�
     */
    private final String name;

    /**
     * ���N���X��ۑ����邽�߂̕ϐ�
     */
    private final UnresolvedTypeInfo extendsType;

    /**
     * ���O�������ꂽ����ۑ����邽�߂̕ϐ�
     */
    protected TypeParameterInfo resolvedInfo;
}
