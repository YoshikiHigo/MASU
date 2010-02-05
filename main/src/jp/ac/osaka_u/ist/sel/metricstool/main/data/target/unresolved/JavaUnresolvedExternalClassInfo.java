package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �o�C�g�R�[�h����͂�����Ԃł̖��������C�u�����N���X���i�[���邽�߂̏��D
 * ���݂́C�Ώی��ꂪJava�݂̂̂��߁C���̂悤�ȊȒP�Ȏ����ɂȂ��Ă��邪�C
 * ���̌���ɑ΂��Ă����C�u�����N���X���쐬����ꍇ�́C
 * ���ʂ̒��ۃN���X���쐬����ȂǁC�A�[�L�e�N�`���̕ύX���K�v�D
 * 
 * @author higo
 *
 */
public class JavaUnresolvedExternalClassInfo {

    public JavaUnresolvedExternalClassInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.name = null;
        this.superName = null;
        this.interfaces = new HashSet<String>();
        this.methods = new HashSet<JavaUnresolvedExternalMethodInfo>();
        this.fields = new HashSet<JavaUnresolvedExternalFieldInfo>();
    }

    public void setName(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public void setSuperName(final String superName) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superName) {
            throw new IllegalArgumentException();
        }

        this.superName = superName;
    }

    public void addInterface(final String interfaceName) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == interfaceName) {
            throw new IllegalArgumentException();
        }

        this.interfaces.add(interfaceName);
    }

    public void addMethod(final JavaUnresolvedExternalMethodInfo method) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == method) {
            throw new IllegalArgumentException();
        }

        this.methods.add(method);
    }

    public void addField(final JavaUnresolvedExternalFieldInfo field) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == field) {
            throw new IllegalArgumentException();
        }

        this.fields.add(field);
    }

    public String getName() {
        return this.name;
    }

    public String getSuperName() {
        return this.superName;
    }

    public Set<String> getInterfaces() {
        return Collections.unmodifiableSet(this.interfaces);
    }

    public Set<JavaUnresolvedExternalMethodInfo> getMethods() {
        return Collections.unmodifiableSet(this.methods);
    }

    public Set<JavaUnresolvedExternalFieldInfo> getFields() {
        return Collections.unmodifiableSet(this.fields);
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof JavaUnresolvedExternalClassInfo) {
            return this.getName().equals(((JavaUnresolvedExternalClassInfo) o).getName());
        }

        return false;
    }

    private String name;

    private String superName;

    private final Set<String> interfaces;

    private final Set<JavaUnresolvedExternalMethodInfo> methods;

    private final Set<JavaUnresolvedExternalFieldInfo> fields;
}
