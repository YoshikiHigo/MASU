package jp.ac.osaka_u.ist.sdl.metricstool.commentremover;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;


public class CommentRemover {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    // �W������(Java�\�[�X)���������W���o��(�R�����g���폜�����\�[�X)
    public static void main(String[] args) throws IOException {

        try {

            //�@�R�}���h���C������������
            final Options options = new Options();

            {
                final Option i = new Option("i", "input", true, "input directory");
                i.setArgName("input");
                i.setArgs(1);
                i.setRequired(true);
                options.addOption(i);
            }

            {
                final Option o = new Option("o", "output", true, "output directory");
                o.setArgName("output");
                o.setArgs(1);
                o.setRequired(true);
                options.addOption(o);
            }

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            final String inputPath = cmd.getOptionValue("i");
            final String outputPath = cmd.getOptionValue("o");

            for (final File file : getFiles(new File(inputPath))) {

                String text = readFile(file);
                text = deleteLineComment(text);
                text = deleteBlockComment(text);
                text = deleteBlankLine(text);

                writeFile(text, file.getAbsolutePath().replace(inputPath, outputPath));
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }

    // �t�@�C����Set���擾
    public static Set<File> getFiles(final File file) {

        final Set<File> files = new HashSet<File>();

        // �f�B���N�g���Ȃ�΁C�ċA�I�ɏ���
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            for (int i = 0; i < subfiles.length; i++) {
                files.addAll(getFiles(subfiles[i]));
            }

            // �t�@�C���Ȃ�΁C�g���q���Ώی���ƈ�v����Γo�^
        } else if (file.isFile()) {

            final String path = file.getAbsolutePath();
            if (path.endsWith(".java")) {
                files.add(file);
            }

            // �f�B���N�g���ł��t�@�C���ł��Ȃ��ꍇ�͕s��
        } else {
            System.err.println("\"" + file.getAbsolutePath() + "\" is not a vaild file!");
            System.exit(0);
        }

        return files;
    }

    // �t�@�C����ǂݍ���
    public static String readFile(final File file) {

        try {

            final StringBuilder text = new StringBuilder();
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                String line = reader.readLine();
                text.append(line);
                text.append(LINE_SEPARATOR);
            }

            return text.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // �s�R�����g�i�u//�v�ȍ~�j�폜
    public static String deleteLineComment(String src) {

        StringBuilder buf = new StringBuilder();

        boolean isLineComment = false;
        for (int i = 0; i < src.length(); i++) {
            char ch = src.charAt(i);

            if (isLineComment) {
                if (ch == LINE_SEPARATOR.charAt(0)) {
                    isLineComment = false;
                }
            } else if (ch == '/' && src.charAt(i + 1) == '/') {
                isLineComment = true;
            } else {
                buf.append(ch);
            }
        }

        return buf.toString();
    }

    // �u���b�N�R�����g�i�u/*�v����u*/�v�܂Łj�폜
    public static String deleteBlockComment(String src) {

        StringBuilder buf = new StringBuilder();

        boolean isBlockComment = false;
        for (int i = 0; i < src.length(); i++) {
            char ch = src.charAt(i);

            if (isBlockComment) {
                if (ch == '/' && src.charAt(i - 1) == '*') {
                    isBlockComment = false;
                }
            } else if (ch == '/' && src.charAt(i + 1) == '*') {
                isBlockComment = true;
            } else {
                buf.append(ch);
            }
        }

        return buf.toString();
    }

    // ��s�폜
    public static String deleteBlankLine(String src) throws IOException {

        StringBuilder buf = new StringBuilder();
        BufferedReader reader = new BufferedReader(new StringReader(src));

        String inLine;
        while ((inLine = reader.readLine()) != null) {
            if (!inLine.matches("^\\s*$")) {
                buf.append(inLine);
                buf.append(LINE_SEPARATOR);
            }
        }

        return buf.toString();
    }

    public static void writeFile(final String text, final String path) {

        try {

            final File file = new File(path);
            file.getParentFile().mkdirs();

            final OutputStream out = new FileOutputStream(path);
            for (int i = 0; i < text.length(); i++) {
                out.write(text.charAt(i));
            }
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
