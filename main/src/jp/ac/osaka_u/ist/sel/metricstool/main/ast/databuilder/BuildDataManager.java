package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfoSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMemberCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;


/**
 * �r���_�[���\�z��������Ǘ����āC���S�̂̐����������C���^�t�F�[�X�D
 * �ȉ���3��ނ̋@�\��A�g���čs��.
 * 
 * 1. �\�z���̃f�[�^�Ɋւ�����̊Ǘ��C�񋟋y�э\�z��Ԃ̊Ǘ�
 * 
 * 2. ���O��ԁC�G�C���A�X�C�ϐ��Ȃǂ̃X�R�[�v�Ǘ�
 * 
 * 3. �N���X���C���\�b�h���C�ϐ�����C�ϐ��Q�ƁC���\�b�h�Ăяo�����Ȃǂ̓o�^��Ƃ̑�s
 * 
 * @author kou-tngt
 *
 */
public interface BuildDataManager {
    
    /**
     * �\�z���̃N���X�Ƀt�B�[���h����ǉ�����
     * 
     * @param field
     */
    public void addField(UnresolvedFieldInfo field);

    /**
     * �\�z���̃��\�b�h�Ƀt�B�[���h�������ǉ�����
     * 
     * @param usage
     */
    public void addFieldAssignment(UnresolvedFieldUsageInfo usage);

    /**
     * �\�z���̃��\�b�h�Ƀt�B�[���h�Q�Ə���ǉ�����
     * 
     * @param usage
     */
    public void addFieldReference(UnresolvedFieldUsageInfo usage);

    /**
     * �\�z���̃��\�b�h�Ƀ��[�J���p�����[�^�ifor�����Ő錾�����ϐ��̂悤�ɁC
     * �錾���ꂽ�ꏊ���玟�̃u���b�N�̏I���܂ŃX�R�[�v���L���ȕϐ��j����ǉ�����
     * 
     * @param localParameter
     */
    public void addLocalParameter(UnresolvedLocalVariableInfo localParameter);

    /**
     * �\�z���̃��\�b�h�Ƀ��[�J���ϐ�����ǉ�����
     * 
     * @param localVariable
     */
    public void addLocalVariable(UnresolvedLocalVariableInfo localVariable);

    /**
     * �\�z���̃��\�b�h�Ƀ��\�b�h�Ăяo������ǉ�����
     * @param memberCall
     */
    public void addMethodCall(UnresolvedMemberCallInfo memberCall);

    /**
     * �\�z���̃��\�b�h�Ɉ�������ǉ�����
     * 
     * @param parameter
     */
    public void addMethodParameter(UnresolvedParameterInfo parameter);

    
    /**
     * �\�z���̃f�[�^�ɓK�؂Ɍ^�p�����[�^���Z�b�g����D
     * @param typeParameter�@�Z�b�g����^�p�����[�^
     */
    public void addTypeParameger(UnresolvedTypeParameterInfo typeParameter);
    
    
    /**
     * ���݂̃u���b�N�X�R�[�v���ŗL���Ȗ��O�G�C���A�X��ǉ�����
     * 
     * @param aliase
     * @param realName
     */
    public void addUsingAliase(String aliase, String[] realName);

    /**
     * ���݂̃u���b�N�X�R�[�v���ŗL���ȁC���O��ԗ��p����ǉ�����
     * 
     * @param nameSpace
     */
    public void addUsingNameSpace(String[] nameSpace);

    /**
     * �N���X�\�z���I�����鎞�ɌĂ΂��D
     * �\�z����
     * 
     * @return
     */
    public UnresolvedClassInfo endClassDefinition();

    public UnresolvedMethodInfo endMethodDefinition();

    public UnresolvedBlockInfo endInnerBlockDefinition();
    
    public void endScopedBlock();

    public void enterClassBlock();

    public void enterMethodBlock();

    public AvailableNamespaceInfoSet getAvailableNameSpaceSet();

    public AvailableNamespaceInfoSet getAvailableAliasSet();

    public AvailableNamespaceInfoSet getAllAvaliableNames();

    public String[] getAliasedName(String name);

    public int getAnonymousClassCount(UnresolvedClassInfo classInfo);

    public UnresolvedClassInfo getCurrentClass();

    public String[] getCurrentNameSpace();

    public UnresolvedMethodInfo getCurrentMethod();

    public UnresolvedVariableInfo getCurrentScopeVariable(String name);
    
    public UnresolvedTypeParameterInfo getTypeParameter(String name);

    public boolean hasAlias(String name);

    public void reset();

    public String[] popNameSpace();

    public void pushNewNameSpace(String[] nameSpace);

    public String[] resolveAliase(String[] name);

    public void startScopedBlock();

    public void startClassDefinition(UnresolvedClassInfo classInfo);

    public void startMethodDefinition(UnresolvedMethodInfo methodInfo);
    
    public void startInnerBlockDefinition(UnresolvedBlockInfo blockInfo);

}
