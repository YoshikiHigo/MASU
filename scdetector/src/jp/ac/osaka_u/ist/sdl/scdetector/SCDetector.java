package jp.ac.osaka_u.ist.sdl.scdetector;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.MESSAGE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.DefaultPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IntraProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.PDGNode;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;


/**
 * �v���O�����ˑ��O���t�̓��`�ȃT�u�O���t�������R�[�h�N���[���Ƃ��Č��o����v���O����
 * ���o�Ώۂ͈����ŗ^������D
 * 
 * @author higo
 */
public class SCDetector extends MetricsTool {

    public static void main(String[] args) {

        try {

            //�@�R�}���h���C������������
            final Options options = new Options();

            {
                final Option c = new Option("c", "count", true,
                        "count for filtering out repeated statements");
                c.setArgName("count");
                c.setArgs(1);
                c.setRequired(false);
                options.addOption(c);
            }

            {
                final Option d = new Option("d", "directory", true, "target directory");
                d.setArgName("directory");
                d.setArgs(1);
                d.setRequired(true);
                options.addOption(d);
            }

            {
                final Option l = new Option("l", "language", true,
                        "programming language of analyzed source code");
                l.setArgName("language");
                l.setArgs(1);
                l.setRequired(true);
                options.addOption(l);
            }

            {
                final Option o = new Option("o", "output", true, "output file");
                o.setArgName("output file");
                o.setArgs(1);
                o.setRequired(true);
                options.addOption(o);
            }

            {
                final Option r = new Option("r", "reference", false,
                        "use variable reference as dependencies");
                r.setArgName("reference");
                r.setRequired(false);
                options.addOption(r);
            }

            {
                final Option s = new Option("s", "size", true, "lower size of detected clone");
                s.setArgName("size");
                s.setArgs(1);
                s.setRequired(true);
                options.addOption(s);
            }

            {
                final Option pv = new Option("pv", true, "parameterize variables");
                pv.setArgName("variable parameterization level");
                pv.setArgs(1);
                pv.setRequired(false);
                pv.setType(Integer.class);
                options.addOption(pv);
            }

            {
                final Option pi = new Option("pi", true, "parameterize invocations");
                pi.setArgName("invocation parameterization level");
                pi.setArgs(1);
                pi.setRequired(false);
                pi.setType(Integer.class);
                options.addOption(pi);
            }

            {
                final Option po = new Option("po", true, "parameterize operations");
                po.setArgName("operation parameterization level");
                po.setArgs(1);
                po.setRequired(false);
                po.setType(Integer.class);
                options.addOption(po);
            }

            {
                final Option pl = new Option("pl", true, "parameterize literals");
                pl.setArgName("literal parameterization level");
                pl.setArgs(1);
                pl.setRequired(false);
                pl.setType(Integer.class);
                options.addOption(pl);
            }

            {
                final Option pc = new Option("pc", true, "parameterize casts");
                pc.setArgName("cast parameterization level");
                pc.setArgs(1);
                pc.setRequired(false);
                pc.setType(Integer.class);
                options.addOption(pc);
            }

            {
                final Option pr = new Option("pr", false, "parameterize class references");
                pr.setRequired(false);
                options.addOption(pr);
            }

            {
                final Option fi = new Option("fi", false,
                        "filtering out code clones included in other code clones");
                fi.setRequired(false);
                options.addOption(fi);
            }

            {
                final Option fj = new Option("fj", false,
                        "filtering out code clones whose start statement and end statement are the same");
                fj.setRequired(false);
                options.addOption(fj);
            }

            {
                final Option fk = new Option("fk", true,
                        "code clones whose elements are overlapped more than the specified threshold");
                fk.setArgName("threshold");
                fk.setArgs(1);
                fk.setRequired(false);
                fk.setType(Integer.class);
                options.addOption(fk);
            }

            {
                final Option fl = new Option("fl", true,
                        "code clones whose elements sizes are different more than the specified threshold");
                fl.setArgName("threshold");
                fl.setArgs(1);
                fl.setRequired(false);
                fl.setType(Integer.class);
                options.addOption(fl);
            }

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("c")) {
                Configuration.INSTANCE.setC(Integer.valueOf(cmd.getOptionValue("c")));
            }
            Configuration.INSTANCE.setD(cmd.getOptionValue("d"));
            Configuration.INSTANCE.setL(cmd.getOptionValue("l"));
            Configuration.INSTANCE.setO(cmd.getOptionValue("o"));
            Configuration.INSTANCE.setR(!cmd.hasOption("r"));
            Configuration.INSTANCE.setS(Integer.valueOf(cmd.getOptionValue("s")));
            if (cmd.hasOption("pv")) {
                Configuration.INSTANCE.setPV(Integer.valueOf(cmd.getOptionValue("pv")));
            }
            if (cmd.hasOption("pi")) {
                Configuration.INSTANCE.setPI(Integer.valueOf(cmd.getOptionValue("pi")));
            }
            if (cmd.hasOption("po")) {
                Configuration.INSTANCE.setPO(Integer.valueOf(cmd.getOptionValue("po")));
            }
            if (cmd.hasOption("pl")) {
                Configuration.INSTANCE.setPL(Integer.valueOf(cmd.getOptionValue("pl")));
            }
            if (cmd.hasOption("pc")) {
                Configuration.INSTANCE.setPC(Integer.valueOf(cmd.getOptionValue("pc")));
            }
            Configuration.INSTANCE.setPR(cmd.hasOption("pr"));
            Configuration.INSTANCE.setFI(cmd.hasOption("fi"));
            Configuration.INSTANCE.setFJ(cmd.hasOption("fj"));
            if (cmd.hasOption("fk")) {
                Configuration.INSTANCE.setFK(Integer.valueOf(cmd.getOptionValue("fk")));
            }
            if (cmd.hasOption("fl")) {
                Configuration.INSTANCE.setFL(Integer.valueOf(cmd.getOptionValue("fl")));
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        try {

            // ��͗p�ݒ�
            Settings.getInstance().setLanguage(Configuration.INSTANCE.getL());
            Settings.getInstance().setTargetDirectory(Configuration.INSTANCE.getD());
            Settings.getInstance().setVerbose(true);

            // ���\���p�ݒ�
            final Class<?> metricstool = MetricsTool.class;
            final Field out = metricstool.getDeclaredField("out");
            out.setAccessible(true);
            out.set(null, new DefaultMessagePrinter(new MessageSource() {
                public String getMessageSourceName() {
                    return "scdetector";
                }
            }, MESSAGE_TYPE.OUT));
            final Field err = metricstool.getDeclaredField("err");
            err.setAccessible(true);
            err.set(null, new DefaultMessagePrinter(new MessageSource() {
                public String getMessageSourceName() {
                    return "main";
                }
            }, MESSAGE_TYPE.ERROR));
            MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(new MessageListener() {
                public void messageReceived(MessageEvent event) {
                    System.out.print(event.getSource().getMessageSourceName() + " > "
                            + event.getMessage());
                }
            });
            MessagePool.getInstance(MESSAGE_TYPE.ERROR).addMessageListener(new MessageListener() {
                public void messageReceived(MessageEvent event) {
                    System.err.print(event.getSource().getMessageSourceName() + " > "
                            + event.getMessage());
                }
            });

        } catch (NoSuchFieldException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }

        // �Ώۃf�B���N�g���ȉ���Java�t�@�C����o�^���C���
        final SCDetector scdetector = new SCDetector();
        scdetector.readTargetFiles();
        scdetector.analyzeTargetFiles();

        // PDG,CFG�̃m�[�h�W�����`
        out.println("buildeing PDGs ...");
        final long detectionStart = System.nanoTime();
        final IPDGNodeFactory pdgNodeFactory = new DefaultPDGNodeFactory();
        final ICFGNodeFactory cfgNodeFactory = new DefaultCFGNodeFactory();

        // �e���\�b�h��PDG���\�z
        for (final TargetMethodInfo method : DataManager.getInstance().getMethodInfoManager()
                .getTargetMethodInfos()) {
            final IntraProceduralPDG pdg = new IntraProceduralPDG(method, pdgNodeFactory,
                    cfgNodeFactory);
        }

        // �R���X�g���N�^��PDG���\�z
        for (final TargetConstructorInfo constructor : DataManager.getInstance()
                .getMethodInfoManager().getTargetConstructorInfos()) {
            final IntraProceduralPDG pdg = new IntraProceduralPDG(constructor, pdgNodeFactory,
                    cfgNodeFactory);
        }

        //ExecutableElement�P�ʂŐ��K���f�[�^���\�z����
        out.println("constructing statement hashtable ...");
        final NormalizedElementHashMap elementHash = new NormalizedElementHashMap();
        for (final TargetMethodInfo method : DataManager.getInstance().getMethodInfoManager()
                .getTargetMethodInfos()) {
            elementHash.addStatement(method);
        }

        // �n�b�V���l������2��StatementInfo����_�ɂ��ăR�[�h�N���[�������o
        out.println("detecting code clones from PDGs ...");
        final Set<ClonePairInfo> clonePairs = new HashSet<ClonePairInfo>();
        for (final List<ExecutableElementInfo> elements : elementHash.values()) {

            // �����n�b�V���l��������臒l�ȏ゠��ꍇ�́C���̕����X���C�X��_�ɂ��Ȃ��D
            //�@�������CReturn���͗�O�Ƃ���D
            if ((Configuration.INSTANCE.getC() < elements.size())
                    && !(elements.get(0) instanceof ReturnStatementInfo)) {
                continue;
            }

            for (int i = 0; i < elements.size(); i++) {
                for (int j = i + 1; j < elements.size(); j++) {

                    final ExecutableElementInfo elementA = elements.get(i);
                    final ExecutableElementInfo elementB = elements.get(j);
                    final PDGNode<?> nodeA = pdgNodeFactory.getNode(elementA);
                    final PDGNode<?> nodeB = pdgNodeFactory.getNode(elementB);
                    assert null != nodeA : "nodeA is null!";
                    assert null != nodeB : "nodeB is null!";

                    final ClonePairInfo clonePair = new ClonePairInfo(elementA, elementB);

                    final HashSet<PDGNode<?>> checkedNodesA = new HashSet<PDGNode<?>>();
                    final HashSet<PDGNode<?>> checkedNodesB = new HashSet<PDGNode<?>>();
                    checkedNodesA.add(nodeA);
                    checkedNodesB.add(nodeB);

                    ProgramSlice.addDuplicatedElementsWithBackwordSlice(nodeA, nodeB,
                            pdgNodeFactory, clonePair, checkedNodesA, checkedNodesB);

                    clonePairs.add(clonePair);
                }
            }
        }

        final long detectionEnd = System.nanoTime();
        out.println("elapsed time: " + (detectionEnd - detectionStart) / 1000000000);

        out.println("filtering out uninterested clone pairs ...");
        final Set<ClonePairInfo> refinedClonePairs = new HashSet<ClonePairInfo>();
        CLONEPAIR: for (final ClonePairInfo clonePair : clonePairs) {

            // �R�[�h�Ђ̃T�C�Y��臒l�ȏ�Ⴄ�ꍇ�̓t�B���^�����O����
            /*
            {
                final CodeFragmentInfo cloneA = clonePair.getCloneA();
                final CodeFragmentInfo cloneB = clonePair.getCloneB();
                if (((cloneA.size() + Configuration.INSTANCE.getFL()) < cloneB.size())
                        || ((cloneA.size() - Configuration.INSTANCE.getFL()) > cloneB.size())) {
                    continue CLONEPAIR;
                }
            }*/

            // �͂��߂ƏI�肪��v���Ă���R�[�h�Ђ̓t�B���^�����O����
            /*
            if (Configuration.INSTANCE.getFJ()) {
                final CodeFragmentInfo cloneA = clonePair.getCloneA();
                final CodeFragmentInfo cloneB = clonePair.getCloneB();
                if ((cloneA.first() == cloneB.first()) && (cloneA.last() == cloneB.last())) {
                    continue CLONEPAIR;
                }
            }*/

            // 臒l�ȏ�d�����Ă���R�[�h�Ђ̓t�B���^�����O����
            /*
            {
                final CodeFragmentInfo cloneA = clonePair.getCloneA();
                final CodeFragmentInfo cloneB = clonePair.getCloneB();
                int sharedElementCount = 0;
                for (final Position element : cloneA) {
                    if (cloneB.contains(element)) {
                        sharedElementCount++;
                    }
                }
                if (Configuration.INSTANCE.getFK() < ((100 * 2 * sharedElementCount) / (cloneA
                        .size() + cloneB.size()))) {
                    continue CLONEPAIR;
                }
            }
            */

            //���̃N���[���y�A�ɓ�����N���[���y�A����������
            /*
            if (Configuration.INSTANCE.getFI()) {
                COUNTERCLONEPAIR: for (final ClonePairInfo counterClonePair : clonePairs) {

                    if (clonePair == counterClonePair) {
                        continue COUNTERCLONEPAIR;
                    }

                    if (clonePair.includedBy(counterClonePair)) {
                        continue CLONEPAIR;
                    }
                }
            }
            */

            refinedClonePairs.add(clonePair);
        }

        System.out.println(refinedClonePairs.size() + ":" + clonePairs.size());

        /*
        {
            final Map<CodeFragmentInfo, Set<CodeFragmentInfo>> cloneSets = new HashMap<CodeFragmentInfo, Set<CodeFragmentInfo>>();
            for (final ClonePairInfo clonePair : refinedClonePairs) {

                final CodeFragmentInfo cloneA = clonePair.getCloneA();
                final CodeFragmentInfo cloneB = clonePair.getCloneB();

                Set<CodeFragmentInfo> cloneSetA = cloneSets.get(cloneA);
                Set<CodeFragmentInfo> cloneSetB = cloneSets.get(cloneB);

                // �R�[�h��A�CB�Ƃ����łɓo�^����Ă���ꍇ
                if ((null != cloneSetA) && (null != cloneSetB)) {

                    //A �� B�̏�������N���[���Z�b�g���Ⴄ�ꍇ�́C��������
                    if (cloneSetA != cloneSetB) {
                        final Set<CodeFragmentInfo> cloneSetC = new HashSet<CodeFragmentInfo>();
                        cloneSetC.addAll(cloneSetA);
                        cloneSetC.addAll(cloneSetB);

                        for (final CodeFragmentInfo codeFragment : cloneSetA) {
                            cloneSets.remove(codeFragment);
                        }
                        for (final CodeFragmentInfo codeFragment : cloneSetB) {
                            cloneSets.remove(codeFragment);
                        }

                        for (final CodeFragmentInfo codeFragment : cloneSetC) {
                            cloneSets.put(codeFragment, cloneSetC);
                        }

                    }

                    cloneSetA.addAll(cloneSetB);
                    cloneSetB.addAll(cloneSetA);

                } else if ((null != cloneSetA) && (null == cloneSetB)) {

                    cloneSetA.add(cloneB);
                    cloneSets.put(cloneB, cloneSetA);

                } else if ((null == cloneSetA) && (null != cloneSetB)) {

                    cloneSetB.add(cloneA);
                    cloneSets.put(cloneA, cloneSetB);

                } else {

                    final Set<CodeFragmentInfo> cloneSet = new HashSet<CodeFragmentInfo>();
                    cloneSet.add(cloneA);
                    cloneSet.add(cloneB);

                    cloneSets.put(cloneA, cloneSet);
                    cloneSets.put(cloneB, cloneSet);

                }
            }
        }
        */

        try {

            out.println("outputing clone pairs ...");
            final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                    Configuration.INSTANCE.getO()));
            oos.writeObject(refinedClonePairs);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println("successifully finished.");
    }
}
