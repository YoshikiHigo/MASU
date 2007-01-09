package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Member �C���^�[�t�F�[�X�𗘗p�����������s���N���X�D
 * 
 * @author y-higo
 *
 */
public class Members {

    /**
     * �����ŗ^����ꂽ�����o�[�̂����C�X�^�e�B�b�N�Ȃ��̂����𒊏o���ĕԂ��D
     * 
     * @param members �����o�[�� List
     * @return �X�^�e�B�b�N�ȃ����o�[�� List
     */
    public static final List getInstanceMembers(final List<? extends Member> members){
        
        final List<Member> instanceMembers = new ArrayList<Member>();
        for (Member member : members){
            if (member.isInstanceMember()){
                instanceMembers.add(member);
            }
        }
        
        return Collections.unmodifiableList(instanceMembers);
    }
    
    /**
     * �����ŗ^����ꂽ�����o�[�̂����C�C���X�^���X�Ȃ��̂����𒊏o���ĕԂ��D
     * 
     * @param members �����o�[�� List
     * @return �C���X�^���X�����o�[�� List
     */
    public static final List getStaticMembers(final List<? extends Member> members){
        
        final List<Member> staticMembers = new ArrayList<Member>();
        for (Member member : members){
            if (member.isStaticMember()){
                staticMembers.add(member);
            }
        }
        
        return Collections.unmodifiableList(staticMembers);
    }
    
    /**
     * �����ŗ^����ꂽ�����o�[�̂����C�X�^�e�B�b�N�Ȃ��̂����𒊏o���ĕԂ��D
     * 
     * @param members �����o�[�� SortedSet
     * @return �X�^�e�B�b�N�ȃ����o�[�� SortedSet
     */
    public static final SortedSet getInstanceMembers(final SortedSet<? extends Member> members){
        
        final SortedSet<Member> instanceMembers = new TreeSet<Member>();
        for (Member member : members){
            if (member.isInstanceMember()){
                instanceMembers.add(member);
            }
        }
        
        return Collections.unmodifiableSortedSet(instanceMembers);
    }
    
    /**
     * �����ŗ^����ꂽ�����o�[�̂����C�C���X�^���X�Ȃ��̂����𒊏o���ĕԂ��D
     * 
     * @param members �����o�[�� SortedSet
     * @return �C���X�^���X�����o�[�� SortedSet
     */
    public static final SortedSet getStaticMembers(final SortedSet<? extends Member> members){
        
        final SortedSet<Member> staticMembers = new TreeSet<Member>();
        for (Member member : members){
            if (member.isStaticMember()){
                staticMembers.add(member);
            }
        }
        
        return Collections.unmodifiableSortedSet(staticMembers);
    }
}
